from django.urls import path
from .views import ChildView


urlpatterns = [path("", ChildView.as_view(), name="/")]

# 서버주소/indexapp/