from django.views import View
from rest_framework.views import APIView  # For swagger
import sys

sys.path.append("..")

from utils import utils, responses

# Create your views here.
class IndexView(APIView):
    def get(self, request):
        return utils.send_json(responses.APIOnly)

    def post(self, request):
        return self.get(request)
