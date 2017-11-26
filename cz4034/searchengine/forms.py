from django import forms


class BasicSearchForm(forms.Form):
    keyword = forms.CharField(label="",
                              required=False,
                              max_length=100,
                              widget=forms.TextInput(attrs={'placeholder': 'Search for keywords',
                                                            'class': "form-control"}))


class AdvancedSearchForm(forms.Form):
    keyword = forms.CharField(label="",
                              required=False,
                              max_length=100,
                              widget=forms.TextInput(attrs={'placeholder': 'Search for keywords',
                                                            'class': "form-group form-control nested-form"}))

    types = (('*', 'All Tweets'),
             ('Technology', 'Technology'),
             ('Sports', 'Sports'),
             ('Politics', 'Politics'),
             ('Health', 'Health'),
             ('Environmental', 'Environmental'),
             ('Entertainment', 'Entertainment'),
             ('Economy', 'Economy'))
    category = forms.ChoiceField(label="",
                                 choices=types,
                                 widget=forms.Select(attrs={'class': "form-group form-control nested-form"}))

    twitter_id = forms.CharField(label="",
                                 required=False,
                                 max_length=100,
                                 widget=forms.TextInput(
                                    attrs={'placeholder': 'Search for Twitter ID',
                                           'class': "form-group form-control nested-form"}))
