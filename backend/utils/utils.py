from django.http import HttpResponse
import json
import jwt

import sys

sys.path.append("..")
# from backend.settings import SECRET_KEY, JWT_ALGORITHM
from urllib import parse
from .responses import *
# from .models import User, Default_User
from django.core.serializers import serialize


# 딕셔너리를 JSON으로 전송하는 헬퍼 함수
def send_json(data):
    res = json.dumps(data)
    return HttpResponse(res, content_type="application/json", status=data["status"])


# 가변 인자로 추출된 딕셔너리를 받아오는 헬퍼 함수
def pop_args(dic, *args):
    return {arg: dic[arg] if arg in dic else None for arg in args}


# def encode_jwt(dic):
#     return jwt.encode(dic, SECRET_KEY, JWT_ALGORITHM)


# # 세션에서 디코딩된 JWT 딕셔너리를 가져오는 헬퍼 함수
# def decode_jwt(session):
#     return jwt.decode(session["JWT_TOKEN"], SECRET_KEY, JWT_ALGORITHM)


# def append_jwt_param(session, **kargs):
#     decoded = decode_jwt(session)
#     for (k, v) in kargs:
#         decoded[k] = v
#     return encode_jwt(decoded)


# # 원본 session을 변경시키는 함수
# def delete_jwt_param(session, *args):
#     decoded = decode_jwt(session)
#     session["JWT_TOKEN"] = encode_jwt(dict(filter(lambda x: x not in args, decoded)))
#     return True


# request.body로 받아왔을때 binary를 dict로 변환하는 함수
def byte_to_dict(data):
    body_unicode = data.decode("utf-8")
    body = body_unicode.replace("&", "=")
    body_list = body.split("=")
    body_key = []
    body_value = []
    for i in range(len(body_list)):
        if i % 2 == 0:
            body_key.append(body_list[i])
        else:
            body_value.append(parse.unquote(body_list[i]))
    dict_body = dict(zip(body_key, body_value))
    return dict_body


def get_user(users):
    if users.count() != 1:
        return userDoesNotMatch
    user = users[0]
    user_dict = json.loads(serialize("json", users))
    default_user = Default_User.objects.filter(pk=user.pk)
    default_user_dict = json.loads(serialize("json", default_user))
    default_user_dict = default_user_dict[0]
    del default_user_dict["fields"]["password"]
    user_dict = user_dict[0]
    user_dict["fields"].update(default_user_dict["fields"])
    print("user_dict:", user_dict)
    result = getSucceedFunc("user")
    result["data"] = user_dict
    return result