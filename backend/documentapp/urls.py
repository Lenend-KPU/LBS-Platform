from django.urls import path, include
from .views import ElementView, Rootview

# 댓글 불러오기, 댓글 쓰기, 댓글 수정, 댓글 삭제

urlpatterns = [
    path("", Rootview.as_view(), name="root"),
    path("<int:document_pk>/", ElementView.as_view(), name="child"),
    path("<int:document_pk>/comments/", include("commentapp.urls")),
    path("<int:document_pk>/likes/", include("likeapp.urls")),
    path("<int:document_pk>/saves/", include("saveapp.urls")),
]
