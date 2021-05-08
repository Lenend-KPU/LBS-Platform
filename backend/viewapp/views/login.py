from django.views import View
from rest_framework.views import APIView  # For swagger
from django.contrib.auth.hashers import check_password
import sys

sys.path.append("..")

from utils import utils, responses
from userapp.models import User
from profileapp.models import Profile

"""
userAlreadyLogin = {
    "success": True,
    "status": 200,
    "comment": "User has already logged in",
    "is_login": True,
}
"""


class LoginView(APIView):
    def post(self, request):
        keys = ["email", "password"]
        request_dict = utils.byte_to_dict(request.body)
        dic = utils.pop_args(request_dict, *keys)

        if None in list(dic.values()):
            return utils.send_json(responses.illegalArgument)

        ### 이메일 유효성 검사
        if not utils.validateEmail(dic["email"]):
            return utils.send_json(responses.noEmail)

        user = User.objects.filter(user_email=dic["email"]).first()
        if user is None:
            return utils.send_json(responses.userDoesNotExist)

        if not check_password(dic["password"], user.user_password):
            return utils.send_json(responses.userDoesNotMatch)
        
        profile = Profile.objects.filter(user=user)
        

        result = responses.userLogin
        result["userid"] = user.pk
        return utils.send_json(result)

    def get(self, request):
        pass
        # 로그인 확인 TODO, jwt 기반
