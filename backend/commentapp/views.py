import sys

sys.path.append("..")
from utils import utils, responses
from django.http import HttpResponse, HttpRequest
from django.views import View
from .models import Comment

# Create your views here.
class RootView(View):
    def get(self, request: HttpRequest) -> HttpResponse:
        # 리미트 TODO
        comments = Comment.objects.all()
        comments = utils.to_dict(comments)
        result = responses.ok
        result["result"] = comments
        result = utils.send_json(result)
        return result

    def post(self, request: HttpRequest) -> HttpResponse:
        # 생성 TODO
        return self.get()


class ElementView(View):
    def get(self, request: HttpRequest, pk: int) -> HttpResponse:
        comment = Comment.objects.filter(pk=pk)
        if len(comment) != 1:
            return utils.send_json(responses.commentDoesNotExists)
        comment = utils.to_dict(comment)
        comment = comment[0]
        result = responses.ok
        result["result"] = comment
        return utils.send_json(result)

    def put(self, request: HttpRequest, pk: int) -> HttpResponse:
        # TODO
        print(pk)
        return utils.send_json(responses.ok)

    def delete(self, request: HttpRequest, pk: int) -> HttpResponse:
        # TODO
        print(pk)
        return utils.send_json(responses.ok)
