import nltk
import os
import pandas
import pickle
from crawler.preprocessor import process_tweets, word_feats


def train():
    data = pandas.read_csv("crawler/data/database.csv", encoding='latin1')
    data.Tweet = process_tweets(data.Tweet)
    tweets = data.Tweet.values

    if os.path.isfile("crawler/data/word_dict.pkl"):
        f = open("crawler/data/word_dict.pkl", "rb")
        word_dict = pickle.load(f)
        f.close()
    else:
        word_dict = []
        for tweet in tweets:
            for word in tweet.split():
                word_dict.append(word)
        saved_words = open("crawler/data/word_dict.pkl", "wb")
        pickle.dump(word_dict, saved_words)
        saved_words.close()

    if os.path.isfile("crawler/data/classifier.pkl"):
        f = open("crawler/data/classifier.pkl", "rb")
        classifier = pickle.load(f)
        f.close()
    else:
        train = [(word_feats(d[1][1], word_dict), d[1][0].lower()) for d in data.iterrows()]
        test = train[:100]
        train = train[100:]
        classifier = nltk.NaiveBayesClassifier.train(train)
        print("Classifier accuracy percent:", (nltk.classify.accuracy(classifier, test))*100)
        classifier.show_most_informative_features(15)
        saved_classifier = open("crawler/data/classifier.pkl", "wb")
        pickle.dump(classifier, saved_classifier)
        saved_classifier.close()

    return word_dict, classifier


if __name__ == "__main__":
    train()
