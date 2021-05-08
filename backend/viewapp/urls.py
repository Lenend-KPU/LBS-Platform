from django.conf.urls import url
from django.urls import path
from .views.index import IndexView
from .views.feed import Rootview
from .views.login import LoginView
from .views.logout import LogoutView
from .views.upload import ImageUploadView

# 인덱스 CR

urlpatterns = [
    path("", IndexView.as_view(), name="root"),
    path("feed/", Rootview.as_view(), name="root"),
    path("login/", LoginView.as_view(), name="root"),
    path("logout/", LogoutView.as_view(), name="root"),
    path("upload/", ImageUploadView.as_view(), name="root"),
]
