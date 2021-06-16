from .utils import send_json
from django.http import HttpRequest, HttpResponse
import sys

sys.path.append("..")
from backend.settings import elastic_search_url
from .responses import loginRequired, userDoesNotMatch, deleteDocumentSucceed
from django.contrib.auth.models import User
import requests
import json


def login_required(func):
    def wrapper(self, request, *args, **kwargs):
        session = request.session
        if "JWT_TOKEN" not in session:
            return send_json(loginRequired)
        decoded = decode_jwt(session)
        if "userid" not in decoded:
            return send_json(loginRequired)
        userid = decoded["userid"]
        try:
            User.objects.get(id=userid)
        except User.DoesNotExist:
            return send_json(userDoesNotMatch)
        return func(self, request, *args, **kwargs)

    return wrapper


def elastic(func):
    def wrapper(self, request: HttpRequest, *args, **kwargs):
        result: HttpResponse = func(self, request, *args, **kwargs)
        if result is not None:
            response = json.loads(result.content)
            for elem in response["result"]:
                pk = elem["pk"]
                elastic_result = requests.put(
                    f"{elastic_search_url}/_doc/{pk}", json=elem
                )
        return result

    return wrapper


def elastic_delete(func):
    def wrapper(
        self, request: HttpRequest, profile_pk: int, document_pk: int, *args, **kwargs
    ):
        result = func(self, request, profile_pk, document_pk, *args, **kwargs)
        response = json.loads(result.content)
        if response == deleteDocumentSucceed:
            requests.delete(f"{elastic_search_url}/_doc/{document_pk}")
        return result

    return wrapper
