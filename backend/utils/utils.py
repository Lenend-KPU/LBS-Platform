from django.http import HttpResponse
import json
from decimal import Decimal

import sys

sys.path.append("..")
from urllib import parse
from .responses import *
from django.core.serializers import serialize
from django.forms.models import model_to_dict


def validateEmail(email):
    from django.core.validators import validate_email
    from django.core.exceptions import ValidationError

    try:
        validate_email(email)
        return True
    except ValidationError as e:
        print("bad email, details:", e)
        return False


class DecimalEncoder(json.JSONEncoder):
    def default(self, obj):
        if isinstance(obj, Decimal):
            return float(obj)
        return json.JSONEncoder.default(self, obj)


"""딕셔너리를 JSON으로 전송하는 헬퍼 함수"""


def send_json(data):
    res = json.dumps(data, cls=DecimalEncoder)
    return HttpResponse(res, content_type="application/json", status=data["status"])


"""QuerySet 객체를 dict 객체로 변환하는 헬퍼 함수"""


def to_dict(queryset):
    return json.loads(serialize("json", queryset))


def pk_to_dict(objects, pk):
    return to_dict(objects.filter(pk=pk))


"""가변 인자로 추출된 딕셔너리를 받아오는 헬퍼 함수"""


def pop_args(dic, *args):
    return {arg: dic[arg] if arg in dic else None for arg in args}


"""가변 인자로 추출된 딕셔너리를 받아오는 헬퍼 함수"""


def byte_to_dict(data):
    body_unicode = data.decode("utf-8")
    body = body_unicode.replace("&", "=")
    body_list = body.split("=")
    body_key = []
    body_value = []
    conv = lambda i: i or None
    for i in range(len(body_list)):
        if i % 2 == 0:
            body_key.append(body_list[i])
        else:
            body_value.append(conv(parse.unquote(body_list[i])))
    dict_body = dict(zip(body_key, body_value))
    return dict_body


"""사옹자를 리턴하는 헬퍼 함수"""


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
