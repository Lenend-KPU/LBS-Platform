from django.urls import path, include
from .views import ElementView, RootView

# 댓글 불러오기, 댓글 쓰기, 댓글 수정, 댓글 삭제

urlpatterns = [
    path("", RootView.as_view(), name="root"),
    path("<int:pk>/", ElementView.as_view(), name="child"),
    path("<int:pk>/friends/", include("friendapp.urls")),
    path("<int:pk>/places/", include("placeapp.urls")),
    path("<int:pk>/documents/", include("documentapp.urls")),
]
