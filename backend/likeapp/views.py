import sys

sys.path.append("..")
from utils import utils, responses
from django.http import HttpResponse, HttpRequest
from django.views import View
from rest_framework.views import APIView  # For swagger
from documentapp.models import Document
from profileapp.models import Profile
from .models import Like

# Create your views here.
class RootView(APIView):
    def get(
        self, request: HttpRequest, profile_pk: int, document_pk: int
    ) -> HttpResponse:
        # 리미트 TODO
        document = Document.objects.filter(profile=profile_pk, pk=document_pk).first()
        if document is None:
            return utils.send_json(responses.noDocument)

        likes = Like.objects.filter(document=document)
        if len(likes) == 0:
            return utils.send_json(responses.likeDoesNotExists)
        likes = utils.to_dict(likes)
        result = responses.ok.copy()
        result["result"] = likes
        result = utils.send_json(result)
        return result

    def post(
        self, request: HttpRequest, profile_pk: int, document_pk: int
    ) -> HttpResponse:
        document = Document.objects.filter(profile=profile_pk, pk=document_pk).first()
        if document is None:
            return utils.send_json(responses.noDocument)

        keys = ["me"]
        request_dict = utils.byte_to_dict(request.body)
        dic = utils.pop_args(request_dict, *keys)

        if None in dic.values():
            return utils.send_json(responses.illegalArgument)

        # 이 부분 추후 세션 등으로 처리 가능
        me = Profile.objects.filter(pk=dic["me"]).first()
        if me is None:
            return utils.send_json(responses.noMyProfile)

        # like 기존에 존재한다면 생성 x -> 삭제
        like = Like.objects.filter(profile=me, document=document)
        if len(like) != 0:
            # return utils.send_json(responses.likeAlreadyExists)
            return self.delete(request, profile_pk, document_pk)

        Like.objects.create(profile=me, document=document)

        return utils.send_json(responses.createLikeSucceed)

    def delete(
        self, request: HttpRequest, profile_pk: int, document_pk: int
    ) -> HttpResponse:
        document = Document.objects.filter(profile=profile_pk, pk=document_pk).first()
        if document is None:
            return utils.send_json(responses.noDocument)

        keys = ["me"]
        request_dict = utils.byte_to_dict(request.body)
        dic = utils.pop_args(request_dict, *keys)

        if None in dic.values():
            return utils.send_json(responses.illegalArgument)

        # 이 부분 추후 세션 등으로 처리 가능
        me = Profile.objects.filter(pk=dic["me"]).first()
        if me is None:
            return utils.send_json(responses.noMyProfile)

        like = Like.objects.filter(profile=me, document=document)
        if len(like) != 1:
            return utils.send_json(responses.likeDoesNotExists)

        like.delete()
        result = responses.deleteLikeSucceed
        return utils.send_json(result)
