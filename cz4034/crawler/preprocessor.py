import re
from nltk.corpus import stopwords
from nltk.stem.wordnet import WordNetLemmatizer
from nltk.tokenize import RegexpTokenizer


def process_tweets(tweet_list):
    stop = set(stopwords.words("english"))
    tokenizer = RegexpTokenizer(r'\w+')
    lmtzr = WordNetLemmatizer()
    processed_tweets = []

    for tweet in tweet_list:
        tweet = re.sub(r'\d+', "", tweet.lower())
        while (tweet.find("http") != -1):
            tweet = tweet.replace(tweet[tweet.find("http"):].split()[0],
                                  "")
        while (tweet.find("rt") != -1):
            tweet = tweet.replace(tweet[tweet.find("rt"):].split()[0], "")
        words = tokenizer.tokenize(tweet)
        removed_stop = [w for w in words if w not in stop]
        lemmatized_tweet = []
        for word in " ".join(removed_stop).split():
            if len(word) > 1:
                lemmatized_tweet.append(lmtzr.lemmatize(word))
        processed_tweets.append(" ".join(lemmatized_tweet))

    return processed_tweets


def word_feats(words, word_dict):
    return dict([(word, word in word_dict) for word in words.split()])
