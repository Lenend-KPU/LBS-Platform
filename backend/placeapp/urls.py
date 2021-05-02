from django.urls import path
from .views import ElementView, Rootview

# 댓글 불러오기, 댓글 쓰기, 댓글 수정, 댓글 삭제

urlpatterns = [
    path("", Rootview.as_view(), name="root"),
    path("<int:place_pk>/", ElementView.as_view(), name="child"),
]
