import enchant
import requests
from collections import Counter
from django.shortcuts import render
from .forms import BasicSearchForm
from .forms import AdvancedSearchForm
from crawler.crawler import crawl_new_data
from crawler.model import train
from crawler.preprocessor import process_tweets, word_feats


def index(request):
    basicform = BasicSearchForm()
    advancedform = AdvancedSearchForm()
    return render(request, 'index.html', {'basicform': basicform, 'advancedform': advancedform})


def basic_search(request):
    if request.method == 'POST':
        basicform = BasicSearchForm(request.POST)

        if basicform.is_valid():
            keyword = basicform.cleaned_data['keyword']
            if keyword is None or keyword == "":
                keyword = '*'
            else:
                spellchecker = enchant.Dict('en')
                words = keyword.replace('*', '').split()
                suggestions = []
                for word in words:
                    suggest = spellchecker.suggest(word)
                    suggestions.append([w for w in suggest if w != word])

            # Query from solr
            url = 'http://localhost:8983/solr/gettingstarted/query?q=(Tweet:*' + keyword + '*)AND(Category:*)&rows=100'
            r = requests.post(url, data={'Tweet': '*' + keyword + '*', 'Category': '*'})
            result = r.json()['response']['docs']
            if len(result) > 10:
                suggestions = None

            basicform = BasicSearchForm()
            advancedform = AdvancedSearchForm()
            return render(request, 'index.html', {'basicform': basicform,
                                                  'advancedform': advancedform,
                                                  'suggestions': suggestions,
                                                  'query_result': result})

    basicform = BasicSearchForm()
    advancedform = AdvancedSearchForm()
    return render(request, 'index.html', {'basicform': basicform,
                                          'advancedform': advancedform})


def advanced_search(request):
    if request.method == 'POST':
        advancedform = AdvancedSearchForm(request.POST)

        if advancedform.is_valid():
            keyword = advancedform.cleaned_data['keyword']
            if keyword is None or keyword == "":
                keyword = '*'
            else:
                spellchecker = enchant.Dict('en')
                words = keyword.replace('*', '').split()
                suggestions = []
                for word in words:
                    suggest = spellchecker.suggest(word)
                    suggestions.append([w for w in suggest if w != word])

            category = advancedform.cleaned_data['category']
            twitter_id = advancedform.cleaned_data['twitter_id']
            if twitter_id is None or twitter_id == "":
                twitter_id = '*'

            # Query from solr
            url = 'http://localhost:8983/solr/gettingstarted/query?q=(Tweet:*' + keyword + \
                  '*)AND(Category:' + category + ')AND(Username:'+twitter_id+')&rows=100&fl=*,score'
            r = requests.post(url, data={'Tweet': '*' + keyword + '*', 'Category': category})
            result = r.json()['response']['docs']
            if len(result) > 10:
                suggestions = None

            basicform = BasicSearchForm()
            advancedform = AdvancedSearchForm()
            return render(request, 'index.html', {'basicform': basicform,
                                                  'advancedform': advancedform,
                                                  'suggestions': suggestions,
                                                  'query_result': result})

    basicform = BasicSearchForm()
    advancedform = AdvancedSearchForm()
    return render(request, 'index.html', {'basicform': basicform,
                                          'advancedform': advancedform})


def crawl(request):
    location = request.GET["location"]

    word_dict, classifier = train()
    user_list, tweet_list = crawl_new_data(location)

    # Predict
    processed_tweets = process_tweets(tweet_list)

    result = []
    category = []
    for user, text, tweet in zip(user_list, tweet_list, processed_tweets):
        feature = word_feats(tweet, word_dict)
        cat = classifier.classify(feature).title()
        category.append(cat)
        result.append({"Username": user, "Tweet": text, "Category": cat})

    counts = Counter(category)

    basicform = BasicSearchForm()
    advancedform = AdvancedSearchForm()

    return render(request, 'index.html', {'basicform': basicform,
                                          'advancedform': advancedform,
                                          'category': dict(counts),
                                          'crawl_result': result})
