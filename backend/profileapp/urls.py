import sys

sys.path.append("..")

from django.urls import path, include
from .views import ElementView, RootView
from saveapp.views import ListView

# 댓글 불러오기, 댓글 쓰기, 댓글 수정, 댓글 삭제

urlpatterns = [
    path("", RootView.as_view(), name="root"),
    path("<int:profile_pk>/", ElementView.as_view(), name="child"),
    path("<int:profile_pk>/save/", ListView.as_view()),
    path("<int:profile_pk>/friends/", include("friendapp.urls")),
    path("<int:profile_pk>/places/", include("placeapp.urls")),
    path("<int:profile_pk>/documents/", include("documentapp.urls")),
]
