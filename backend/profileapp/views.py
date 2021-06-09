import sys

sys.path.append("..")
from userapp.models import User
from utils import utils, responses
from utils.decorators import elastic
from django.http import HttpResponse, HttpRequest
from django.views import View
from rest_framework.views import APIView  # For swagger
from .models import Profile


# Create your views here.
class RootView(APIView):
    def get(self, request: HttpRequest) -> HttpResponse:
        if "userid" not in request.GET:
            return utils.send_json(responses.useridRequired)

        userid = request.GET["userid"]
        if not userid.isdigit():
            return utils.send_json(responses.noUseridNum)

        user = User.objects.filter(pk=userid).first()
        if user is None:
            return utils.send_json(responses.noUser)

        profile = Profile.objects.filter(user=user)
        if len(profile) == 0:
            return utils.send_json(responses.noProfile)

        profile = utils.to_dict(profile)
        result = responses.ok
        result["result"] = profile
        return utils.send_json(result)

    def post(self, request: HttpRequest) -> HttpResponse:
        keys = ["userid", "name", "photo", "private"]
        request_dict = utils.byte_to_dict(request.body)
        dic = utils.pop_args(request_dict, *keys)

        if None in dic.values():
            return utils.send_json(responses.illegalArgument)

        if dic["private"]:
            dic["private"] = ["true", "false"].index(dic["private"]) == 0

        filtered = Profile.objects.filter(user=dic["userid"])
        if filtered.count():
            return utils.send_json(responses.profileAlreadyRegistered)

        matched_user = User.objects.filter(pk=dic["userid"])

        if matched_user.count() != 1:
            return utils.send_json(responses.noProfile)

        matched_user = matched_user.first()

        Profile.objects.create(
            user=matched_user,
            profile_name=dic["name"],
            profile_photo=dic["photo"],
            profile_private=dic["private"],
        )
        result = responses.ok
        return utils.send_json(result)


class ElementView(APIView):
    def get(self, request: HttpRequest, profile_pk: int) -> HttpResponse:
        profile = Profile.objects.filter(pk=profile_pk)
        if len(profile) != 1:
            return utils.send_json(responses.noProfile)
        profile = utils.to_dict(profile)
        profile = profile[0]
        result = responses.ok
        result["result"] = profile
        return utils.send_json(result)

    def put(self, request: HttpRequest, profile_pk: int) -> HttpResponse:
        keys = ["userid", "name", "photo", "private"]
        request_dict = utils.byte_to_dict(request.body)
        dic = utils.pop_args(request_dict, *keys)
        # username, address, email, password 파라미터 없이 온다면

        if [None] * len(keys) == list(dic.values()):
            return utils.send_json(responses.illegalArgument)

        # 변경하려는 profile_name이 기존에 존재하는지 처리 분기문
        if dic["name"]:
            profile_already_filtered = Profile.objects.filter(
                profile_name=dic["name"]
            ).first()
            if (
                profile_already_filtered is not None
                and profile_already_filtered.pk != profile_pk
            ):
                return utils.send_json(responses.profileNameAlreadyRegistered)

        filtered = Profile.objects.filter(pk=profile_pk)
        if not filtered.count():
            return utils.send_json(responses.noProfile)

        original_filtered = filtered
        filtered = filtered.first()

        if filtered.user.pk != dic["userid"]:
            matched_user = User.objects.filter(pk=dic["userid"])

            if matched_user.count() != 1:
                return utils.send_json(responses.noProfile)

            matched_user = matched_user.first()
        else:
            matched_user = filtered.user
        if dic["name"] is None:
            dic["name"] = filtered.profile_name
        if dic["photo"] is None:
            dic["photo"] = filtered.profile_photo
        if dic["private"]:
            dic["private"] = ["true", "false"].index(dic["private"]) == 0
        if dic["private"] is None:
            dic["private"] = filtered.profile_private
        original_filtered.update(
            user=matched_user,
            profile_name=dic["name"],
            profile_photo=dic["photo"],
            profile_private=dic["private"],
        )
        return utils.send_json(responses.modifyProfileSucceed)

    def delete(self, request: HttpRequest, profile_pk: int) -> HttpResponse:
        # 삭제, 수정은 해당 유저나 관리자가 할 수 있어야 하는데 해당 부분은 TODO
        profile = Profile.objects.filter(pk=profile_pk)
        if len(profile) != 1:
            return utils.send_json(responses.noProfile)
        profile.delete()
        result = responses.deleteProfileSucceed
        return utils.send_json(result)
