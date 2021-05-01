import sys

sys.path.append("..")
from utils import utils, responses
from django.http import HttpResponse, HttpRequest
from django.views import View
from .models import User

# Create your views here.
class RootView(View):
    def get(self, request: HttpRequest) -> HttpResponse:
        # 리미트 TODO
        users = User.objects.all()
        users = utils.to_dict(users)
        result = responses.ok
        result["result"] = users
        result = utils.send_json(result)
        return result

    def post(self, request: HttpRequest) -> HttpResponse:
        # 생성 TODO
        return self.get()


class ElementView(View):
    def get(self, request: HttpRequest, pk: int) -> HttpResponse:
        user = User.objects.filter(pk=pk)
        if len(user) != 1:
            return utils.send_json(responses.noUser)
        user = utils.to_dict(user)
        user = user[0]
        result = responses.ok
        result["result"] = user
        return utils.send_json(result)

    def put(self, request: HttpRequest, pk: int) -> HttpResponse:
        # TODO
        print(pk)
        return utils.send_json(responses.ok)

    def delete(self, request: HttpRequest, pk: int) -> HttpResponse:
        # TODO
        print(pk)
        return utils.send_json(responses.ok)
