from django.conf.urls import url
from django.urls import path
from .views.index import IndexView
from .views.feed import Rootview

# 인덱스 CR

urlpatterns = [
    path("", IndexView.as_view(), name="root"),
    path("feed/", Rootview.as_view(), name="root"),
]
