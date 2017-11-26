import argparse
import matplotlib.pyplot as plt
import numpy as np
import os
import tensorflow as tf

os.environ['TF_CPP_MIN_LOG_LEVEL'] = '2'
os.environ['CUDA_VISIBLE_DEVICES'] = '0'


def scale(X, X_min, X_max):
    '''
    Scale and normalize input data.
    '''
    return (X - X_min)/(X_max - X_min)


def normalize(X, X_mean, X_std):
    return (X - X_mean)/X_std


def shuffle_data(samples, labels):
    idx = np.arange(samples.shape[0])
    np.random.shuffle(idx)
    samples, labels = samples[idx], labels[idx]
    return samples, labels


def batch_data(i, batch_size, maxlen, data):
    start = int(i * batch_size)
    end = start + batch_size
    if end > maxlen:
        end = maxlen
    return data[start:end]


np.random.seed(10)
parser = argparse.ArgumentParser()

parser.add_argument(
    '-lr',
    '--learning-rate',
    dest='learning_rate',
    type=float,
    metavar='<float>',
    default=0.00001,
    help='Learning rate. (Default=0.00001)')

parser.add_argument(
    '-hn',
    '--hidden-neuron',
    dest='hidden_neuron',
    type=int,
    metavar='<int>',
    default=60,
    help='Number of hidden neurons. (Default=60)')

parser.add_argument(
    '-l',
    '--layer',
    dest='layer',
    type=int,
    metavar='<int>',
    default=3,
    help='Number of hidden neurons. (Default=3)')

parser.add_argument(
    '-cv',
    '--cross-validation',
    dest='cross_validation',
    action='store_true')

args = parser.parse_args()

filename = '_' + str(args.layer) + 'layer' '_nr' + str(args.hidden_neuron)
if args.cross_validation:
    filename += '_cv'
epochs = 1000
batch_size = 32

# Read and divide data into test and train sets
cal_housing = np.loadtxt('cal_housing.data', delimiter=',')
X_data, Y_data = cal_housing[:, :8], cal_housing[:, -1]
Y_data = (np.asmatrix(Y_data)).transpose()
X_data, Y_data = shuffle_data(X_data, Y_data)

# Separate train and test data
m = 3 * X_data.shape[0] // 10
test_x, test_y = X_data[:m], Y_data[:m]
train_x, train_y = X_data[m:], Y_data[m:]

# Scale and normalize data
train_x_max, train_x_min = np.max(train_x, axis=0), np.min(train_x, axis=0)
test_x_max, test_x_min = np.max(test_x, axis=0), np.min(test_x, axis=0)
train_x = scale(train_x, train_x_min, train_x_max)
test_x = scale(test_x, test_x_min, test_x_max)
train_x_mean, train_x_std = np.mean(train_x, axis=0), np.std(train_x, axis=0)
test_x_mean, test_x_std = np.mean(test_x, axis=0), np.std(test_x, axis=0)
train_x = normalize(train_x, train_x_mean, train_x_std)
test_x = normalize(test_x, test_x_mean, test_x_std)

# Data size and shape
xdim = train_x.shape[1]
ydim = train_y.shape[1]
maxlen_train = len(train_x)
maxlen_test = len(test_x)

# Split training set into 5 folds
fold_size = int(len(train_x) / 5) + 1
folds_x, folds_y = [], []
for i in range(0, maxlen_train, fold_size):
    folds_x.append(train_x[i:i + fold_size])
    folds_y.append(train_y[i:i + fold_size])
folds_x = np.array(folds_x)
folds_y = np.array(folds_y)

# Model
if args.layer == 3:
    print('3layer_feed_forward_net')
    with tf.name_scope('feed_forward_net'):
        x = tf.placeholder(tf.float32, [None, xdim], 'x')
        y = tf.placeholder(tf.float32, [None, ydim], 'y')

        w1 = tf.Variable(tf.truncated_normal([xdim, args.hidden_neuron],
                                             stddev=0.01))
        b1 = tf.Variable(tf.truncated_normal([args.hidden_neuron],
                                             stddev=0.01))
        layer1 = tf.nn.sigmoid(tf.matmul(x, w1) + b1)

        wo = tf.Variable(tf.truncated_normal([args.hidden_neuron, ydim],
                                             stddev=0.01))
        bo = tf.Variable(tf.truncated_normal([ydim], stddev=0.01))
        out = tf.matmul(layer1, wo) + bo

        cost = tf.abs(tf.reduce_mean(tf.square(y - out)))
        accuracy = tf.reduce_mean(y - out)
elif args.layer == 4:
    print('4layer_feed_forward_net')
    with tf.name_scope('feed_forward_net'):
        x = tf.placeholder(tf.float32, [None, xdim], 'x')
        y = tf.placeholder(tf.float32, [None, ydim], 'y')

        w1 = tf.Variable(tf.truncated_normal([xdim, args.hidden_neuron],
                                             stddev=0.01))
        b1 = tf.Variable(tf.truncated_normal([args.hidden_neuron],
                                             stddev=0.01))
        layer1 = tf.nn.sigmoid(tf.matmul(x, w1) + b1)

        w2 = tf.Variable(tf.truncated_normal([args.hidden_neuron, 20],
                                             stddev=0.01))
        b2 = tf.Variable(tf.truncated_normal([20], stddev=0.01))
        layer2 = tf.nn.sigmoid(tf.matmul(layer1, w2) + b2)

        wo = tf.Variable(tf.truncated_normal([20, ydim], stddev=0.01))
        bo = tf.Variable(tf.truncated_normal([ydim], stddev=0.01))
        out = tf.matmul(layer2, wo) + bo

        cost = tf.abs(tf.reduce_mean(tf.square(y - out)))
        accuracy = tf.reduce_mean(y - out)
elif args.layer == 5:
    print('5layer_feed_forward_net')
    with tf.name_scope('feed_forward_net'):
        x = tf.placeholder(tf.float32, [None, xdim], 'x')
        y = tf.placeholder(tf.float32, [None, ydim], 'y')

        w1 = tf.Variable(tf.truncated_normal([xdim, args.hidden_neuron],
                                             stddev=0.01))
        b1 = tf.Variable(tf.truncated_normal([args.hidden_neuron],
                                             stddev=0.01))
        layer1 = tf.nn.sigmoid(tf.matmul(x, w1) + b1)

        w2 = tf.Variable(tf.truncated_normal([args.hidden_neuron, 20],
                                             stddev=0.01))
        b2 = tf.Variable(tf.truncated_normal([20], stddev=0.01))
        layer2 = tf.nn.sigmoid(tf.matmul(layer1, w2) + b2)

        w3 = tf.Variable(tf.truncated_normal([20, 20], stddev=0.01))
        b3 = tf.Variable(tf.truncated_normal([20], stddev=0.01))
        layer3 = tf.nn.sigmoid(tf.matmul(layer2, w3) + b3)

        wo = tf.Variable(tf.truncated_normal([20, ydim], stddev=0.01))
        bo = tf.Variable(tf.truncated_normal([ydim], stddev=0.01))
        out = tf.matmul(layer3, wo) + bo

        cost = tf.abs(tf.reduce_mean(tf.square(y - out)))
        accuracy = tf.reduce_mean(y - out)

# Optimizer
with tf.name_scope('optimizer'):
    train_op = tf.train.GradientDescentOptimizer(
        args.learning_rate).minimize(cost)

# Training
best_cost, best_accuracy, best_iter = 9999999999999999999999999, 0, 0

if args.cross_validation:
    losses_train, losses_test, test_accuracy = \
        np.zeros([epochs]), np.zeros([epochs]), np.zeros([epochs])
    for i in range(5):
        print('Fold %d as test set.' % i)
        # Use 4 folds as training set and 1 fold as test set
        f_x = np.concatenate(np.delete(folds_x, i))
        t_x = folds_x[i]
        f_y = np.concatenate(np.delete(folds_y, i))
        t_y = folds_y[i]
        batch_num_train = int(len(f_x) / batch_size) + 1
        with tf.Session() as sess:
            sess.run(tf.global_variables_initializer())
            for e in range(epochs):
                if e % 100 == 0:
                    print(e)
                test_start = 0
                test_end = fold_size
                loss_train = []
                for i in range(batch_num_train):
                    inputs = batch_data(i, batch_size, maxlen_train, f_x)
                    outputs = batch_data(i, batch_size, maxlen_train, f_y)
                    _, l_train = sess.run([train_op, cost],
                                          feed_dict={x: inputs, y: outputs})
                    loss_train.append(l_train)
                losses_train[e] += np.mean(loss_train)
                l_test, acc = sess.run([cost, accuracy],
                                       feed_dict={x: t_x, y: t_y})
                losses_test[e] += l_test
                test_accuracy[e] += acc
    losses_train /= 5
    losses_test /= 5
    test_accuracy /= 5
    for e in range(epochs):
        if losses_test[e] < best_cost:
            best_cost = losses_test[e]
            best_accuracy = test_accuracy[e]
            best_iter = e
else:
    batch_num_train = int(len(train_x) / batch_size) + 1
    losses_train, losses_test, test_accuracy = [], [], []
    with tf.Session() as sess:
        sess.run(tf.global_variables_initializer())
        for e in range(epochs):
            if e % 100 == 0:
                print(e)
            train_x, train_y = shuffle_data(train_x, train_y)
            loss_train = []
            for i in range(batch_num_train):
                inputs = batch_data(i, batch_size, maxlen_train, train_x)
                outputs = batch_data(i, batch_size, maxlen_train, train_y)
                _, l_train = sess.run([train_op, cost],
                                      feed_dict={x: inputs, y: outputs})
                loss_train.append(l_train)
            losses_train.append(np.mean(loss_train))
            l_test, acc = sess.run([cost, accuracy],
                                   feed_dict={x: test_x, y: test_y})
            losses_test.append(l_test)
            test_accuracy.append(acc)
            if losses_test[e] < best_cost:
                best_cost = losses_test[e]
                best_accuracy = test_accuracy[e]
                best_iter = e

# Summary
print('Minimum error: %.1f, Best accuracy %.1f, Number of Iterations: %d' %
      (best_cost, best_accuracy, best_iter))
filename += '_lr' + str(args.learning_rate) + '.png'
plt.figure()
plt.plot(range(epochs), losses_train, label='train error')
plt.plot(range(epochs), losses_test, label='test error')
plt.xlabel('Time (s)')
plt.ylabel('Mean Squared Error')
plt.title('Training & Test Errors at Alpha=%g' % args.learning_rate)
plt.legend()
plt.savefig('p_1b_mse' + filename)
plt.show()
if not args.cross_validation:
    plt.figure()
    plt.plot(range(epochs), test_accuracy)
    plt.xlabel('Epochs')
    plt.ylabel('Accuracy')
    plt.title('Test Accuracy')
    plt.savefig('p_1b_accuracy' + filename)
    plt.show()
