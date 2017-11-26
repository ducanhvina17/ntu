import geocoder
from TwitterSearch import *


def convert_to_geocode(location):
    g = geocoder.google(location)
    return g.latlng


def crawl_new_data(location=None):
    try:
        # Setup
        tso = TwitterSearchOrder()
        tso.set_result_type("recent")
        tso.set_keywords(["Politics", "Entertainment", "Sports", "Technology", "Economy", "Health",
                          "Environmental"], True)
        tso.set_language("en")
        tso.set_include_entities(False)

        if location is not None and location != "":
            geocode = convert_to_geocode(location)
            tso.set_geocode(geocode[0], geocode[1], 100)

        ts = TwitterSearch(consumer_key="cZz3zVZVsG5oguVYerXKXdCKe",
                           consumer_secret="u4s7xWLkkP52q8zxbdjtWkeC2bPNRls0jdVDTjW9ut4lUvPJ0J",
                           access_token="828520002038804481-VIzTQJyon2taOYjxyginZDnAX6LUx3P",
                           access_token_secret="aPbekORDQEnXh6ERw88fpk2PcS6ju644xO2y2ITaRDPOg")

        i = 0
        user_list = []
        tweet_list = []
        for tweet in ts.search_tweets_iterable(tso):
            if i == 50:
                break
            user_list.insert(-1, tweet["user"]["screen_name"])
            text = tweet["text"].replace("&amp;", "&")
            text = text.replace("&lt;", "<")
            text = text.replace("&gt;", ">")
            tweet_list.insert(-1, text)
            i += 1

        return user_list, tweet_list
    except TwitterSearchException as e:
        print(e)


if __name__ == "__main__":
    import pandas
    user_list, tweet_list = crawl_new_data()
    result = pandas.DataFrame({"Category": "Category", "Tweet": tweet_list, "Username": user_list})
    result.to_csv("data/Category.csv", index=False)
