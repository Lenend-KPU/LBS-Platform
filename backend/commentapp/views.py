import sys

sys.path.append("..")
from utils import utils, responses
from django.http import HttpResponse, HttpRequest
from django.views import View
from rest_framework.views import APIView  # For swagger
from documentapp.models import Document
from profileapp.models import Profile
from .models import Comment

# Create your views here.
class RootView(APIView):
    def get(
        self, request: HttpRequest, profile_pk: int, document_pk: int
    ) -> HttpResponse:
        # 리미트 TODO
        document = Document.objects.filter(profile=profile_pk, pk=document_pk).first()
        if document is None:
            return utils.send_json(responses.noDocument)

        comments = Comment.objects.filter(document=document)
        if len(comments) == 0:
            return utils.send_json(responses.commentDoesNotExists)
        comments = utils.to_dict(comments)
        result = responses.ok
        result["result"] = comments
        result = utils.send_json(result)
        return result

    def post(
        self, request: HttpRequest, profile_pk: int, document_pk: int
    ) -> HttpResponse:
        document = Document.objects.filter(profile=profile_pk, pk=document_pk).first()
        if document is None:
            return utils.send_json(responses.noDocument)

        keys = ["me", "text"]
        request_dict = utils.byte_to_dict(request.body)
        dic = utils.pop_args(request_dict, *keys)

        if None in dic.values():
            return utils.send_json(responses.illegalArgument)

        # 이 부분 추후 세션 등으로 처리 가능
        me = Profile.objects.filter(pk=dic["me"]).first()
        if me is None:
            return utils.send_json(responses.noMyProfile)

        Comment.objects.create(profile=me, document=document, comment_text=dic["text"])

        return utils.send_json(responses.createCommentSucceed)


class ElementView(View):
    def get(
        self, request: HttpRequest, profile_pk: int, document_pk: int, comment_pk: int
    ) -> HttpResponse:
        document = Document.objects.filter(profile=profile_pk, pk=document_pk).first()
        if document is None:
            return utils.send_json(responses.noDocument)

        comment = Comment.objects.filter(document=document, pk=comment_pk)
        # 추후 pk가 아닌 unique_number를 걸 것을 대비하여 document filter도 추가
        if len(comment) == 0:
            return utils.send_json(responses.commentDoesNotExists)

        comment = utils.to_dict(comment)
        comment = comment[0]
        result = responses.ok
        result["result"] = comment
        return utils.send_json(result)

    def put(
        self, request: HttpRequest, profile_pk: int, document_pk: int, comment_pk: int
    ) -> HttpResponse:
        document = Document.objects.filter(profile=profile_pk, pk=document_pk).first()
        if document is None:
            return utils.send_json(responses.noDocument)

        keys = ["text"]
        request_dict = utils.byte_to_dict(request.body)
        dic = utils.pop_args(request_dict, *keys)
        if None in dic.values():
            return utils.send_json(responses.illegalArgument)

        comment = Comment.objects.filter(document=document, pk=comment_pk)
        # 추후 pk가 아닌 unique_number를 걸 것을 대비하여 document filter도 추가
        if len(comment) == 0:
            return utils.send_json(responses.commentDoesNotExists)

        comment.update(comment_text=dic["text"])

        return utils.send_json(responses.modifyCommentSucceed)

    def delete(
        self, request: HttpRequest, profile_pk: int, document_pk: int, comment_pk: int
    ) -> HttpResponse:
        document = Document.objects.filter(profile=profile_pk, pk=document_pk).first()
        if document is None:
            return utils.send_json(responses.noDocument)

        comment = Comment.objects.filter(pk=comment_pk)
        if len(comment) != 1:
            return utils.send_json(responses.commentDoesNotExists)
        comment.delete()
        result = responses.deleteCommentSucceed
        return utils.send_json(result)
