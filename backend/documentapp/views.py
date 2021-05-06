import sys

sys.path.append("..")
from utils import utils, responses
from django.http import HttpResponse, HttpRequest
from django.views import View
from rest_framework.views import APIView  # For swagger
from profileapp.models import Profile
from placeapp.models import Place
from pathapp.models import Path
from commentapp.models import Comment
from likeapp.models import Like
from maximumapp.models import Maximum
from .models import Document


def createPath(document_pk, place_pk, order):
    place = Place.objects.filter(pk=place_pk).first()
    Path.objects.create(document=document_pk, place=place, path_order=order)
    print(f"document {document_pk}, place {place_pk}의 path 생성")


# Create your views here.
class Rootview(APIView):
    def get(self, request: HttpRequest, pk: int) -> HttpResponse:
        # 리미트: TODO
        profile = Profile.objects.filter(pk=pk).first()

        if profile is None:
            return utils.send_json(responses.noProfile)

        documents = Document.objects.filter(profile=profile)

        if len(documents) == 0:
            return utils.send_json(responses.noDocument)

        documents = utils.to_dict(documents)

        result = responses.ok
        result["result"] = documents

        for key, document in enumerate(documents):
            place_dict = []
            document_pk = document["pk"]

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

    def post(self, request: HttpRequest, pk: int) -> HttpResponse:
        profile = Profile.objects.filter(pk=pk).first()
        if profile is None:
            return utils.send_json(responses.noProfile)

        keys = [
            "name",
            "color",
            "private",
        ]
        # place1 ~ place10
        place_keys = list(map(lambda x: f"place{x}", range(1, 11)))
        friend_keys = ["friend"]

        request_dict = utils.byte_to_dict(request.body)
        dic = utils.pop_args(request_dict, *keys)
        places_dic = utils.pop_args(request_dict, *place_keys)
        friends_dic = utils.pop_args(request_dict, *friend_keys)

        if None in dic.values() or places_dic["place1"] is None:
            return utils.send_json(responses.illegalArgument)

        i = list(places_dic.values()).index(None)
        places_dic = dict(list(places_dic.items())[:i])

        dic |= friends_dic

        if dic["private"]:
            dic["private"] = ["true", "false"].index(dic["private"]) == 0

        # 실제 친구를 태그 했는지 확인하는 로직
        """
        프론트엔드 입장에서 선택 가능한 친구를 "팔로잉"으로 한정 지어서 뿌려준다면 잘못된 데이터가 들어올리 없음.
        해당 예외처리는 혹시 모를 오류 데이터로 인한 문제인 profile이 존재하는지 유무만 검사해주면 될 것으로 보임.
        """
        friend = None
        if dic["friend"]:
            friend = Profile.objects.filter(pk=dic["friend"]).first()
            if friend is None:
                return utils.send_json(responses.noFriend)

        # document_map 그림그려주는 것 TODO
        document_map = "/path/"

        # place 존재 여부 확인
        # 해당 유저의 place로 이중 필터링 TODO
        for v in places_dic.values():
            if v.isdigit():
                place = Place.objects.filter(pk=v)
                if not place.count():
                    result = responses.noPlace
                    result["pk"] = v
                    return utils.send_json(result)
            else:
                result = responses.noPlaceNum
                result["id"] = v
                return utils.send_json(result)

        document_created = Document.objects.create(
            profile=profile,
            profile_friend=friend,
            document_name=dic["name"],
            document_color=dic["color"],
            document_map=document_map,
            document_private=dic["private"],
        )

        for key, value in places_dic.items():
            createPath(document_created, value, key[5:])  # key[5:]는 1 ~ 10

        del document_created

        return utils.send_json(responses.createDocumentSucceed)


class ElementView(APIView):
    def get(self, request: HttpRequest, pk: int, document_pk: int) -> HttpResponse:
        # 리미트: TODO
        profile = Profile.objects.filter(pk=pk).first()

        if profile is None:
            return utils.send_json(responses.noProfile)

        document = Document.objects.filter(profile=profile, pk=document_pk)

        if len(document) == 0:
            return utils.send_json(responses.noDocument)

        document = utils.to_dict(document)

        result = responses.ok
        result["result"] = document

        comments = Comment.objects.filter(document=document_pk)
        comments = utils.to_dict(comments)
        result["result"][0]["comments"] = comments

        likes = Like.objects.filter(document=document_pk)
        likes = utils.to_dict(likes)
        result["result"][0]["likes"] = likes

        paths = Path.objects.filter(document=document_pk).order_by("path_order")

        place_dict = []
        for p in paths:
            place = Place.objects.filter(pk=p.place.id)
            place_dict.append(utils.to_dict(place)[0])

        result["result"][0]["places"] = place_dict

        del place_dict
        return utils.send_json(result)

    def put(self, request: HttpRequest, pk: int, document_pk: int) -> HttpResponse:
        pass
        # profile = Profile.objects.filter(pk=pk).first()
        # if profile is None:
        #     return utils.send_json(responses.noProfile)

        # keys = ["name", "rate", "photo", "latitude", "longitude", "private"]
        # request_dict = utils.byte_to_dict(request.body)
        # dic = utils.pop_args(request_dict, *keys)
        # print(dic)

        # if [None] * len(keys) == list(dic.values()):
        #     return utils.send_json(responses.illegalArgument)

        # place = Place.objects.filter(profile=profile, pk=document_pk)
        # if not place.count():
        #     return utils.send_json(responses.noPlace)

        # original_place = place
        # filtered = place.first()

        # if dic["name"] is None:
        #     dic["name"] = filtered.place_name
        # if dic["rate"] is None:
        #     dic["rate"] = filtered.place_rate
        # if dic["photo"] is None:
        #     dic["photo"] = filtered.place_photo
        # if dic["latitude"] is None:
        #     dic["latitude"] = filtered.place_latitude
        # if dic["longitude"] is None:
        #     dic["longitude"] = filtered.place_longitude
        # if dic["private"]:
        #     dic["private"] = ["true", "false"].index(dic["private"]) == 0
        # if dic["private"] is None:
        #     dic["private"] = filtered.place_private
        # original_place.update(
        #     profile=profile,
        #     place_name=dic["name"],
        #     place_rate=dic["rate"],
        #     place_photo=dic["photo"],
        #     place_latitude=dic["latitude"],
        #     place_longitude=dic["longitude"],
        #     place_private=dic["private"],
        # )
        # # modifyProfileSucceed TODO
        return utils.send_json(responses.modifyDocumentSucceed)

    def delete(self, request: HttpRequest, pk: int, document_pk: int) -> HttpResponse:
        # 삭제, 수정은 해당 유저나 관리자가 할 수 있어야 하는데 해당 부분은 TODO
        profile = Profile.objects.filter(pk=pk).first()

        if profile is None:
            return utils.send_json(responses.noProfile)

        document = Document.objects.filter(profile=profile, pk=document_pk)

        if len(document) == 0:
            return utils.send_json(responses.noDocument)

        paths = Path.objects.filter(document=document_pk).order_by("path_order")

        for p in paths:
            p.delete()

        document.delete()

        return utils.send_json(responses.deleteDocumentSucceed)
