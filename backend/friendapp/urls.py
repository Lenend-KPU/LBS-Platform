from django.urls import path
from .views import ElementView, GetView


urlpatterns = [
    path("", GetView.as_view()),
    path("<int:following_pk>/", ElementView.as_view()),
]
