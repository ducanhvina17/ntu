from django.conf.urls import url
from . import views


urlpatterns = [
    url(r'^$', views.index, name='index'),
    url(r'^basic_search/$', views.basic_search, name='basic_search'),
    url(r'^advanced_search/$', views.advanced_search, name='advanced_search'),
    url(r'^crawl/$', views.crawl, name='crawl'),
]
