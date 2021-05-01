from django.conf.urls import url
from django.urls import path
from .views.index import IndexView

# 인덱스 CR

urlpatterns = [
    path("", IndexView.as_view(), name="root"),
]
