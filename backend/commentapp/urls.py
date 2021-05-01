from django.conf.urls import url
from django.urls import path
from .views import ElementView, RootView

# 댓글 불러오기, 댓글 쓰기, 댓글 수정, 댓글 삭제

urlpatterns = [
    path("", RootView.as_view(), name="root"),
    path("<int:pk>/", ElementView.as_view(), name="child"),
]
