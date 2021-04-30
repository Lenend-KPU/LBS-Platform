import sys
sys.path.append("..")
from utils import utils, responses
from django.http import HttpResponse
from django.views import View
from .models import Comment

# Create your views here.
class RootView(View):
    def get(self, request) -> HttpResponse:
        comments = Comment.objects.all()
        comments = utils.to_dict(comments)
        result = responses.ok
        result['result'] = comments
        result = utils.send_json(result)
        return result

    def post(self, request) -> HttpResponse:
        return self.get()


class ElementView(View):
    def get(self, request, profile_id: int) -> HttpResponse:
        print(profile_id)
        return utils.send_json(responses.ok)

    def post(self, request, profile_id: int) -> HttpResponse:
        print(profile_id)
        return utils.send_json(responses.ok)
