import sys

sys.path.append("..")
from utils import utils, responses
from django.http import HttpResponse, HttpRequest
from django.views import View
from rest_framework.views import APIView  # For swagger
from profileapp.models import Profile
from maximumapp.models import Maximum
from .models import Place

# Create your views here.
# /profile/3/place/
class Rootview(APIView):
    def get(self, request: HttpRequest, profile_pk: int) -> HttpResponse:
        # 리미트: TODO
        profile = Profile.objects.filter(pk=profile_pk).first()

        if profile is None:
            return utils.send_json(responses.noProfile)

        places = Place.objects.filter(profile=profile)

        if len(places) == 0:
            return utils.send_json(responses.noPlace)

        places = utils.to_dict(places)
        result = responses.ok
        result["result"] = places

        return utils.send_json(result)

    def post(self, request: HttpRequest, profile_pk: int) -> HttpResponse:
        profile = Profile.objects.filter(pk=profile_pk).first()
        if profile is None:
            return utils.send_json(responses.noProfile)

        keys = ["name", "rate", "photo", "latitude", "longitude", "private"]
        request_dict = utils.byte_to_dict(request.body)
        dic = utils.pop_args(request_dict, *keys)

        if None in dic.values():
            return utils.send_json(responses.illegalArgument)

        if dic["private"]:
            dic["private"] = ["true", "false"].index(dic["private"]) == 0

        Place.objects.create(
            profile=profile,
            place_name=dic["name"],
            place_rate=dic["rate"],
            place_photo=dic["photo"],
            place_latitude=dic["latitude"],
            place_longitude=dic["longitude"],
            place_private=dic["private"],
        )
        result = responses.createPlaceSucceed
        return utils.send_json(result)


class ElementView(APIView):
    def get(self, request: HttpRequest, profile_pk: int, place_pk: int) -> HttpResponse:
        profile = Profile.objects.filter(pk=profile_pk).first()

        if profile is None:
            return utils.send_json(responses.noProfile)

        place = Place.objects.filter(profile=profile, pk=place_pk)

        if len(place) == 0:
            return utils.send_json(responses.noPlace)

        place = utils.to_dict(place)
        result = responses.ok
        result["result"] = place

        return utils.send_json(result)

    def put(self, request: HttpRequest, profile_pk: int, place_pk: int) -> HttpResponse:
        profile = Profile.objects.filter(pk=profile_pk).first()
        if profile is None:
            return utils.send_json(responses.noProfile)

        keys = ["name", "rate", "photo", "latitude", "longitude", "private"]
        request_dict = utils.byte_to_dict(request.body)
        dic = utils.pop_args(request_dict, *keys)

        if [None] * len(keys) == list(dic.values()):
            return utils.send_json(responses.illegalArgument)

        place = Place.objects.filter(profile=profile, pk=place_pk)
        if not place.count():
            return utils.send_json(responses.noPlace)

        original_place = place
        filtered = place.first()

        if dic["name"] is None:
            dic["name"] = filtered.place_name
        if dic["rate"] is None:
            dic["rate"] = filtered.place_rate
        if dic["photo"] is None:
            dic["photo"] = filtered.place_photo
        if dic["latitude"] is None:
            dic["latitude"] = filtered.place_latitude
        if dic["longitude"] is None:
            dic["longitude"] = filtered.place_longitude
        if dic["private"]:
            dic["private"] = ["true", "false"].index(dic["private"]) == 0
        if dic["private"] is None:
            dic["private"] = filtered.place_private
        original_place.update(
            profile=profile,
            place_name=dic["name"],
            place_rate=dic["rate"],
            place_photo=dic["photo"],
            place_latitude=dic["latitude"],
            place_longitude=dic["longitude"],
            place_private=dic["private"],
        )
        # modifyProfileSucceed TODO
        return utils.send_json(responses.modifyPlaceSucceed)

    def delete(self, request: HttpRequest, profile_pk: int, place_pk: int) -> HttpResponse:
        # 삭제, 수정은 해당 유저나 관리자가 할 수 있어야 하는데 해당 부분은 TODO
        profile = Profile.objects.filter(pk=profile_pk).first()

        if profile is None:
            return utils.send_json(responses.noProfile)

        place = Place.objects.filter(profile=profile, pk=place_pk)

        if len(place) == 0:
            return utils.send_json(responses.noPlace)

        place.delete()
        result = responses.deletePlaceSucceed
        return utils.send_json(result)


"""
place
- 유저 place 생성: Rootview
- 각 유저의 place 전체 조회: Rootview
- place 조회, 수정 삭제: Elementview
"""
