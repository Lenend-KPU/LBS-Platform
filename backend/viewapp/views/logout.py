from django.views import View
from rest_framework.views import APIView  # For swagger
import sys

sys.path.append("..")

from utils import utils, responses


class LogoutView(APIView):
    pass
