import sys

sys.path.append("..")
from utils import utils, responses
from django.http import HttpResponse, HttpRequest
from django.views import View
from .models import User
from django.contrib.auth.hashers import check_password
from django.contrib.auth.hashers import make_password


# Create your views here.
class RootView(View):
    def get(self, request: HttpRequest) -> HttpResponse:
        # 리미트 TODO
        users = User.objects.all()
        users = utils.to_dict(users)
        result = responses.ok
        result["result"] = users

        return utils.send_json(result)

    def post(self, request: HttpRequest) -> HttpResponse:
        keys = ["username", "address", "email", "password"]
        request_dict = utils.byte_to_dict(request.body)
        dic = utils.pop_args(request_dict, *keys)

        if None in list(dic.values()):
            return utils.send_json(responses.illegalArgument)
        filtered = User.objects.filter(user_email=dic["email"])
        if filtered.count():
            return utils.send_json(responses.userAlreadyRegistered)

        User.objects.create(
            user_name=dic["username"],
            user_email=dic["email"],
            user_address=dic["address"],
            user_password=make_password(dic["password"]),
        )
        result = responses.ok
        return utils.send_json(result)


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
        # password 인증 TODO
        keys = ["username", "address", "email", "password"]
        request_dict = utils.byte_to_dict(request.body)
        dic = utils.pop_args(request_dict, *keys)
        # username, address, email, password 파라미터 없이 온다면

        if [None] * len(keys) == list(dic.values()):
            return utils.send_json(responses.illegalArgument)

        filtered = User.objects.filter(pk=pk)
        if not filtered.count():
            return utils.send_json(responses.noUser)

        original_filtered = filtered
        filtered = utils.to_dict(filtered)[0]
        hashed_password = make_password(dic["password"])

        if not filtered["fields"]["user_password"] != hashed_password:
            return utils.send_json(responses.userDoesNotMatch)

        # 변경하려는 email이 기존에 존재하는지 처리 분기문
        filtered_exist = User.objects.filter(user_email=dic["email"])
        if filtered_exist.count():
            return utils.send_json(responses.userAlreadyRegistered)
        if dic["username"] is None:
            dic["username"] = filtered["fields"]["user_name"]
        if dic["email"] is None:
            dic["email"] = filtered["fields"]["user_email"]
        if dic["password"] is None:
            dic["password"] = filtered["fields"]["user_password"]
        if dic["address"] is None:
            dic["address"] = filtered["fields"]["user_address"]
        original_filtered.update(
            user_name=dic["username"],
            user_email=dic["email"],
            user_address=dic["address"],
            user_password=make_password(dic["password"]),
        )
        return utils.send_json(responses.modifyUserSucceed)

    def delete(self, request: HttpRequest, pk: int) -> HttpResponse:
        # 삭제, 수정은 해당 유저나 관리자가 할 수 있어야 하는데 해당 부분
        user = User.objects.filter(pk=pk)
        if len(user) != 1:
            return utils.send_json(responses.noUser)
        user.delete()
        result = responses.ok
        return utils.send_json(result)
