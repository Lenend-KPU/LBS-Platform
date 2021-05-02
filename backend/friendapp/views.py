import sys

sys.path.append("..")
from utils import utils, responses
from django.http import HttpResponse, HttpRequest
from django.views import View
from .models import Friend
from profileapp.models import Profile

# Create your views here.
class GetView(View):
    def get(self, request: HttpRequest, pk: int) -> HttpResponse:
        me = Profile.objects.filter(pk=pk).first()

        if me is None:
            return utils.send_json(responses.noProfile)

        followers = Friend.objects.filter(friend_profile=me)
        followings = Friend.objects.filter(profile=me)

        followers = utils.to_dict(followers)
        followings = utils.to_dict(followings)

        for lst in [followers, followings]:
            for elem in lst:
                elem["fields"]["profile"] = utils.pk_to_dict(
                    Profile.objects, elem["fields"]["profile"]
                )[0]
                elem["fields"]["friend_profile"] = utils.pk_to_dict(
                    Profile.objects, elem["fields"]["friend_profile"]
                )[0]

        result = responses.ok
        result["result"] = {}
        result["result"]["followers"] = followers
        result["result"]["followings"] = followings
        return utils.send_json(result)


class ElementView(View):
    def post(self, request: HttpRequest, pk: int, following_pk: int) -> HttpResponse:
        me = Profile.objects.filter(pk=pk).first()
        friend = Profile.objects.filter(pk=following_pk).first()

        if None in [me, friend]:
            return utils.send_json(responses.noFriend)

        filtered = Friend.objects.filter(profile=me, friend_profile=friend)
        if filtered.count():
            return utils.send_json(responses.friendExists)

        Friend.objects.create(profile=me, friend_profile=friend)

        result = responses.createFriendSucceed
        return utils.send_json(result)

    def delete(self, request: HttpRequest, pk: int, following_pk: int) -> HttpResponse:
        me = Profile.objects.filter(pk=pk).first()
        friend = Profile.objects.filter(pk=following_pk).first()

        if None in [me, friend]:
            return utils.send_json(responses.noFriend)

        filtered = Friend.objects.filter(profile=me, friend_profile=friend)
        if not filtered.count():
            return utils.send_json(responses.noFriend)

        filtered.delete()

        result = responses.deleteFriendSucceed
        return utils.send_json(result)


"""
following
- 팔로우 요청 POST
- 내가 팔로잉 하고 있는 목록(user pk) GET, - 내 팔로워 목록(user pk) GET
"""
