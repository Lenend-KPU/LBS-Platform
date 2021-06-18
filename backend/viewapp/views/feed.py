import sys

sys.path.append("..")
from utils import utils, responses, decorators
from django.http import HttpResponse, HttpRequest
from django.views import View
from rest_framework.views import APIView  # For swagger
from profileapp.models import Profile
from documentapp.models import Document
from placeapp.models import Place
from pathapp.models import Path
from commentapp.models import Comment
from likeapp.models import Like

# from maximumapp.models import Maximum

# Create your views here.
class Rootview(APIView):
    @decorators.elastic
    def get(self, request: HttpRequest) -> HttpResponse:
        # 리미트: TODO
        documents = Document.objects.all().order_by("-pk")
        documents = utils.to_dict(documents)

        result = responses.ok
        result["result"] = documents

        if len(documents) == 0:
            return utils.send_json(result)

        for key, document in enumerate(documents):
            place_dict = []
            profile_pk = document["fields"]["profile"]
            document_pk = document["pk"]

            profile = Profile.objects.filter(pk=profile_pk)
            profile = utils.to_dict(profile)[0]
            result["result"][key]["profile"] = profile

            comments = Comment.objects.filter(document=document_pk)
            comments = utils.to_dict(comments)
            result["result"][key]["comments"] = comments

            likes = Like.objects.filter(document=document_pk)
            likes = utils.to_dict(likes)
            result["result"][key]["likes"] = likes

            paths = Path.objects.filter(document=document_pk).order_by("path_order")

            for p in paths:
                place = Place.objects.filter(pk=p.place.id)
                place_dict.append(utils.to_dict(place)[0])

            result["result"][key]["places"] = place_dict

        # document 안에 place들, comment, like들 추가되어야 함
        return utils.send_json(result)

    def post(self, request: HttpRequest) -> HttpResponse:
        # 생성 TODO
        return self.get()
