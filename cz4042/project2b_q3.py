import numpy as np
import os
import pylab
import tensorflow as tf
from load import mnist

os.environ['TF_CPP_MIN_LOG_LEVEL'] = '2'
os.environ['CUDA_VISIBLE_DEVICES'] = '0'
np.random.seed(123)


def init_w(n_visible, n_hidden):
    return np.asarray(
        np.random.uniform(
            low=-4 * np.sqrt(6. / (n_hidden + n_visible)),
            high=4 * np.sqrt(6. / (n_hidden + n_visible)),
            size=(n_visible, n_hidden)),
        dtype=np.float32)


def init_b(n):
    return np.zeros(n, dtype=np.float32)


def corrupt(x):
    '''Take an input tensor and add uniform masking.
    Parameters
    ----------
    x : Tensor/Placeholder
        Input to corrupt.
    Returns
    -------
    x_corrupted : Tensor
        50 pct of values corrupted.
    '''
    return tf.multiply(x, tf.cast(tf.random_uniform(shape=tf.shape(x),
                                                    minval=0,
                                                    maxval=2,
                                                    dtype=tf.int32),
                                  tf.float32))


corruption_level = 0.1
training_epochs = 25
learning_rate = 0.1
batch_size = 128
momentum = 0.1
beta = 0.5  # penalty
rho = 0.05  # sparsity

num_hidden1 = 900
num_hidden2 = 625
num_hidden3 = 400

trX, teX, trY, teY = mnist()
trX, trY = trX[:12000], trY[:12000]
teX, teY = teX[:2000], teY[:2000]
x_shape = trX.shape[1]
y_shape = trY.shape[1]

x = tf.placeholder(tf.float32, [None, x_shape], 'x')
y = tf.placeholder(tf.float32, [None, y_shape], 'y')

with tf.variable_scope('dae1'):
    tilde_x = corrupt(x) * corruption_level + x * (1 - corruption_level)
    W1 = tf.get_variable(
        'weight', initializer=tf.constant(init_w(x_shape, num_hidden1)))
    tf.add_to_collection('dae1', W1)
    b1 = tf.get_variable(
        'bias', initializer=tf.constant(init_b(num_hidden1)))
    tf.add_to_collection('dae1', b1)
    b1t = tf.get_variable(
        'bias_t', initializer=tf.constant(init_b(x_shape)))
    tf.add_to_collection('dae1', b1t)
    W1t = tf.transpose(W1)
    y1 = tf.nn.sigmoid(tf.matmul(tilde_x, W1) + b1)
    z1 = tf.nn.sigmoid(tf.matmul(y1, W1t) + b1t)
    cost1 = -tf.reduce_mean(tf.reduce_sum(x * tf.log(z1) + (1 - x) *
                                          tf.log(1 - z1), axis=1)) + \
        beta * num_hidden1 * (rho * tf.log(rho) + (1 - rho) *
                              tf.log(1 - rho)) - \
        beta * rho * tf.reduce_sum(tf.log(tf.reduce_mean(y1, axis=0)+1e-6)) - \
        beta * (1 - rho) * tf.reduce_sum(tf.log(1 -
                                                tf.reduce_mean(y1, axis=0) +
                                                1e-6))

with tf.variable_scope('dae2'):
    W2 = tf.get_variable(
        'weight', initializer=tf.constant(init_w(num_hidden1, num_hidden2)))
    tf.add_to_collection('dae2', W2)
    b2 = tf.get_variable(
        'bias', initializer=tf.constant(init_b(num_hidden2)))
    tf.add_to_collection('dae2', b2)
    b2t = tf.get_variable(
        'bias_t', initializer=tf.constant(init_b(num_hidden1)))
    tf.add_to_collection('dae2', b2t)
    W2t = tf.transpose(W2)
    y2 = tf.nn.sigmoid(tf.matmul(y1, W2) + b2)
    z2 = tf.nn.sigmoid(tf.matmul(y2, W2t) + b2t)
    cost2 = -tf.reduce_mean(tf.reduce_sum(y1 * tf.log(z2) + (1 - y1) *
                                          tf.log(1 - z2), axis=1)) + \
        beta * num_hidden2 * (rho * tf.log(rho) + (1 - rho) *
                              tf.log(1 - rho)) - \
        beta * rho * tf.reduce_sum(tf.log(tf.reduce_mean(y2, axis=0)+1e-6)) - \
        beta * (1 - rho) * tf.reduce_sum(tf.log(1 -
                                                tf.reduce_mean(y2, axis=0) +
                                                1e-6))

with tf.variable_scope('dae3'):
    W3 = tf.get_variable(
        'weight', initializer=tf.constant(init_w(num_hidden2, num_hidden3)))
    tf.add_to_collection('dae3', W3)
    b3 = tf.get_variable(
        'bias', initializer=tf.constant(init_b(num_hidden3)))
    tf.add_to_collection('dae3', b3)
    b3t = tf.get_variable(
        'bias_t', initializer=tf.constant(init_b(num_hidden2)))
    tf.add_to_collection('dae3', b3t)
    W3t = tf.transpose(W3)
    y3 = tf.nn.sigmoid(tf.matmul(y2, W3) + b3)
    z3 = tf.nn.sigmoid(tf.matmul(y3, W3t) + b3t)
    cost3 = -tf.reduce_mean(tf.reduce_sum(y2 * tf.log(z3) + (1 - y2) *
                                          tf.log(1 - z3), axis=1)) + \
        beta * num_hidden3 * (rho * tf.log(rho) + (1 - rho) *
                              tf.log(1 - rho)) - \
        beta * rho * tf.reduce_sum(tf.log(tf.reduce_mean(y3, axis=0)+1e-6)) - \
        beta * (1 - rho) * tf.reduce_sum(tf.log(1 -
                                                tf.reduce_mean(y3, axis=0) +
                                                1e-6))

with tf.variable_scope('ffn'):
    W_ffn = tf.get_variable(
        'weight', initializer=tf.constant(init_w(num_hidden3, y_shape)))
    tf.add_to_collection('ffn', W_ffn)
    b_ffn = tf.get_variable(
        'bias', initializer=tf.constant(init_b(y_shape)))
    tf.add_to_collection('ffn', b_ffn)
    output = tf.matmul(y3, W_ffn) + b_ffn
    preds = tf.argmax(tf.nn.softmax(output), axis=1)  # tf.nn.softmax
    cost_ffn = tf.losses.softmax_cross_entropy(y, output)

with tf.variable_scope('recon_img'):
    rimg1 = z3
    rimg2 = tf.nn.sigmoid(tf.matmul(z3, W2t) + b2t)
    rimg3 = tf.nn.sigmoid(tf.matmul(y1, W1t) + b1t)

with tf.name_scope('optimizer'):
    train_op1 = tf.train.MomentumOptimizer(learning_rate, momentum).minimize(
        cost1, var_list=tf.get_collection('dae1'))
    train_op2 = tf.train.MomentumOptimizer(learning_rate, momentum).minimize(
        cost2, var_list=tf.get_collection('dae2'))
    train_op3 = tf.train.MomentumOptimizer(learning_rate, momentum).minimize(
        cost3, var_list=tf.get_collection('dae3'))
    train_op_ffn = tf.train.MomentumOptimizer(
        learning_rate, momentum).minimize(cost_ffn)

train_ops = [train_op1, train_op2, train_op3]
costs = [cost1, cost2, cost3]
losses, weights, h_activation = [], [], []

with tf.Session() as sess:
    sess.run(tf.global_variables_initializer())
    sess.run(tf.local_variables_initializer())
    for train_op, cost in zip(train_ops, costs):
        print('training dae...')
        loss = []
        for epoch in range(training_epochs):
            c = []
            for start, end in zip(range(0, len(trX), batch_size),
                                  range(batch_size, len(trX), batch_size)):
                c.append(sess.run([train_op, cost],
                                  feed_dict={x: trX[start:end]})[1])
            loss.append(np.mean(c, dtype='float64'))
            print(loss[epoch])
        losses.append(loss)

    print('\ntraining ffn ...')
    loss, a = [], []
    for epoch in range(training_epochs):
        c = []
        for start, end in zip(range(0, len(trX), batch_size),
                              range(batch_size, len(trX), batch_size)):
            c.append(sess.run([train_op_ffn, cost_ffn],
                              feed_dict={x: trX[start:end],
                                         y: trY[start:end]})[1])
        loss.append(np.mean(c, dtype='float64'))
        a.append(np.mean(
            np.argmax(teY, axis=1) == sess.run(preds, feed_dict={x: teX})))
        print(a[epoch])
    losses.append(loss)
    weights.append(sess.run(W1))
    weights.append(sess.run(W2))
    weights.append(sess.run(W3))
    w_ffn = sess.run(W_ffn)
    h_activation.append(sess.run(y1, feed_dict={x: teX}))
    h_activation.append(sess.run(y2, feed_dict={x: teX}))
    h_activation.append(sess.run(y3, feed_dict={x: teX}))
    corrupted_img, recon_img = sess.run([tilde_x, rimg3], feed_dict={x: teX})

# Learning curves for each layer
pylab.figure()
pylab.suptitle('Learning Curves')
pylab.plot(range(training_epochs), losses[0], label='layer1')
pylab.plot(range(training_epochs), losses[1], label='layer2')
pylab.plot(range(training_epochs), losses[2], label='layer3')
pylab.legend(['layer1', 'layer2', 'layer3'], loc='upper right')
pylab.xlabel('iterations')
pylab.ylabel('cross-entropy')
pylab.savefig('results/q3_learning.png')

# 100 samples of weights at each layer
for j, w in enumerate(weights):
    shape = (int(np.sqrt(w.shape[0])), int(np.sqrt(w.shape[0])))
    pylab.figure()
    pylab.suptitle('Layer ' + str(j + 1) + ' Weight')
    pylab.gray()
    for i in range(100):
        pylab.subplot(10, 10, i+1)
        pylab.axis('off')
        pylab.imshow(w[:, i].reshape(shape))
    pylab.savefig('results/q3_weight' + str(j + 1) + '.png')

# 100 hidden layer activations
for j, h in enumerate(h_activation):
    shape = (int(np.sqrt(h.shape[1])), int(np.sqrt(h.shape[1])))
    pylab.figure()
    pylab.suptitle('Layer ' + str(j + 1) + ' Activation')
    pylab.gray()
    for i in range(100):
        pylab.subplot(10, 10, i+1)
        pylab.axis('off')
        pylab.imshow(h[i].reshape(shape))
    pylab.savefig('results/q3_h_activation' + str(j + 1) + '.png')

# 100 test data
shape = (int(np.sqrt(teX.shape[1])), int(np.sqrt(teX.shape[1])))
pylab.figure()
pylab.suptitle('100 Test Data')
pylab.gray()
for i in range(100):
    pylab.subplot(10, 10, i+1)
    pylab.axis('off')
    pylab.imshow(teX[i].reshape(shape))
pylab.savefig('results/q3_test_input.png')

# 100 corrupted data
pylab.figure()
pylab.suptitle('100 Corrupted Test Data')
pylab.gray()
for i in range(100):
    pylab.subplot(10, 10, i+1)
    pylab.axis('off')
    pylab.imshow(corrupted_img[i].reshape(shape))
pylab.savefig('results/q3_test_corrupted.png')

# 100 reconstructed
pylab.figure()
pylab.suptitle('100 Reconstructed Test Data')
pylab.gray()
for i in range(100):
    pylab.subplot(10, 10, i+1)
    pylab.axis('off')
    pylab.imshow(recon_img[i].reshape(shape))
pylab.savefig('results/q3_test_reconstruct.png')

# FFN training error
pylab.figure()
pylab.suptitle('FFN Learning Curve')
pylab.plot(range(training_epochs), losses[3])
pylab.xlabel('iterations')
pylab.ylabel('cross-entropy')
pylab.savefig('results/q3_train.png')

# FFN test accuracy
pylab.figure()
pylab.suptitle('FFN Accuracy')
pylab.plot(range(training_epochs), a)
pylab.xlabel('iterations')
pylab.ylabel('test accuracy')
pylab.savefig('results/q3_acc.png')
pylab.show()