from django.urls import path
from .views import ChildView

# 댓글 불러오기, 댓글 쓰기, 댓글 수정, 댓글 삭제

urlpatterns = [path("", ChildView.as_view(), name="/")]
