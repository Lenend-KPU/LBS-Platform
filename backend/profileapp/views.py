import sys

sys.path.append("..")
from userapp.models import User
from utils import utils, responses
from django.http import HttpResponse, HttpRequest
from django.views import View
from .models import Profile
from django.contrib.auth.hashers import check_password
from django.contrib.auth.hashers import make_password


# Create your views here.
class RootView(View):
    def get(self, request: HttpRequest) -> HttpResponse:
        # 리미트 TODO
        profiles = Profile.objects.all()
        profiles = utils.to_dict(profiles)
        result = responses.ok
        result["result"] = profiles

        return utils.send_json(result)

    def post(self, request: HttpRequest) -> HttpResponse:
        keys = ["userid", "name", "photo", "private"]
        request_dict = utils.byte_to_dict(request.body)
        dic = utils.pop_args(request_dict, *keys)

        invalid_flag = False

        invalid_flag = invalid_flag and (
            dic["userid"] is None or type(dic["userid"]) != int
        )
        invalid_flag = invalid_flag and (
            dic["name"] is None or type(dic["name"]) != str
        )
        invalid_flag = invalid_flag and (
            dic["photo"] is None or type(dic["photo"]) != str
        )
        invalid_flag = invalid_flag and (
            dic["private"] is None or dic["private"] in ["true", "false"]
        )

        if invalid_flag:
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


class ElementView(View):
    def get(self, request: HttpRequest, pk: int) -> HttpResponse:
        profile = Profile.objects.filter(pk=pk)
        if len(profile) != 1:
            return utils.send_json(responses.noProfile)
        profile = utils.to_dict(profile)
        profile = profile[0]
        result = responses.ok
        result["result"] = profile
        return utils.send_json(result)

    def put(self, request: HttpRequest, pk: int) -> HttpResponse:
        keys = ["userid", "name", "photo", "private"]
        request_dict = utils.byte_to_dict(request.body)
        dic = utils.pop_args(request_dict, *keys)
        # username, address, email, password 파라미터 없이 온다면

        if [None] * len(keys) == list(dic.values()):
            return utils.send_json(responses.illegalArgument)

        invalid_flag = False

        invalid_flag = invalid_flag and (
            dic["userid"] is not None and type(dic["userid"]) != int
        )
        invalid_flag = invalid_flag and (
            dic["name"] is not None and type(dic["name"]) != str
        )
        invalid_flag = invalid_flag and (
            dic["photo"] is not None and type(dic["photo"]) != str
        )
        invalid_flag = invalid_flag and (
            dic["private"] is not None and dic["private"] in ["true", "false"]
        )

        if invalid_flag:
            return utils.send_json(responses.illegalArgument)

        filtered = Profile.objects.filter(pk=pk)
        if not filtered.count():
            return utils.send_json(responses.noProfile)

        original_filtered = filtered
        filtered = filtered.first()

        # 변경하려는 email이 기존에 존재하는지 처리 분기문
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
        # modifyProfileSucceed TODO
        return utils.send_json(responses.modifyProfileSucceed)

    def delete(self, request: HttpRequest, pk: int) -> HttpResponse:
        # 삭제, 수정은 해당 유저나 관리자가 할 수 있어야 하는데 해당 부분은 TODO
        profile = Profile.objects.filter(pk=pk)
        if len(profile) != 1:
            return utils.send_json(responses.noProfile)
        profile.delete()
        result = responses.deleteProfileSucceed
        return utils.send_json(result)
