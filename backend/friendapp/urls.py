from django.urls import path
from .views import ElementView, GetView


urlpatterns = [
    path("<int:pk>/", GetView.as_view()),
    path("<int:pk>/<int:following_pk>/", ElementView.as_view()),
]
