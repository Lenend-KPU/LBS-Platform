import sys

sys.path.append("..")
from utils import utils, responses
from django.http import HttpResponse, HttpRequest
from django.views import View
from rest_framework.views import APIView  # For swagger
from documentapp.models import Document
from profileapp.models import Profile
from .models import Save

# Create your views here.
class RootView(APIView):
    def get(
        self, request: HttpRequest, profile_pk: int, document_pk: int
    ) -> HttpResponse:
        # 리미트 TODO
        document = Document.objects.filter(profile=profile_pk, pk=document_pk).first()
        if document is None:
            return utils.send_json(responses.noDocument)

        saves = Save.objects.filter(document=document)
        if len(saves) == 0:
            return utils.send_json(responses.saveDoesNotExists)
        saves = utils.to_dict(saves)
        result = responses.ok
        result["result"] = saves
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

        # save 기존에 존재한다면 생성 x -> 삭제
        save = Save.objects.filter(profile=me, document=document)
        if len(save) != 0:
            # return utils.send_json(responses.saveAlreadyExists)
            return self.delete(request, profile_pk, document_pk)

        Save.objects.create(profile=me, document=document)

        return utils.send_json(responses.createSaveSucceed)

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

        save = Save.objects.filter(profile=me, document=document)
        if len(save) != 1:
            return utils.send_json(responses.saveDoesNotExists)

        save.delete()
        result = responses.deleteSaveSucceed
        return utils.send_json(result)


class ListView(APIView):
    def get(self, request: HttpRequest, profile_pk: int) -> HttpResponse:
        # 리미트 TODO
        profile = Profile.objects.filter(pk=profile_pk).first()
        if profile is None:
            return utils.send_json(responses.noProfile)

        saves = Save.objects.filter(profile=profile)
        if len(saves) == 0:
            return utils.send_json(responses.saveDoesNotExists)

        saves = utils.to_dict(saves)

        result = responses.ok
        documents = []

        for save in saves:
            document_pk = save["fields"]["document"]
            document = Document.objects.filter(pk=document_pk)
            documents.append(document[0])

        documents = utils.to_dict(documents)
        result["documents"] = documents

        return utils.send_json(result)
