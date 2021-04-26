from django.views import View
import sys

sys.path.append("..")

from utils.utils import send_json
from utils.responses import *

# Create your views here.
class IndexView(View):
    def get(self, request):
        return send_json(APIOnly)

    def post(self, request):
        return self.get(request)