import sys
sys.path.append("..")
from utils import utils, responses
from django.http import HttpResponse
from django.views import View
from .models import Comment

# Create your views here.
class RootView(View):
    def get(self, request) -> HttpResponse:
        # 리미트 TODO
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
        # 다른 외래키 TODO
        # TODO
        print(profile_id)
        return utils.send_json(responses.ok)

    def post(self, request, profile_id: int) -> HttpResponse:
        # 다른 파라미터 TODO
        # profile_id 매개변수 삭제?
        # TODO
        print(profile_id)
        return utils.send_json(responses.ok)
