import pytest
from django.urls import reverse
from django.test import RequestFactory
from hypothesis import assume, given, strategies as st, settings
from .views import RootView
from .models import Profile
from utils import responses, utils
from hypothesis.extra.django import from_model
import json
import urllib.parse
from userapp.models import User

# Create your tests here.


@given(
    user=from_model(User),
    name=st.text(min_size=1),
    photo=st.integers(),
    private=st.sampled_from(["true", "false"]),
)
@pytest.mark.django_db(transaction=True, reset_sequences=True)
@settings(deadline=500)
def test_root_post(user, **kwargs):
    Profile.objects.filter(profile_name=kwargs["name"]).delete()
    kwargs["userid"] = user.pk
    path = reverse("profileapp:root")
    request = RequestFactory().post(
        path,
        data=urllib.parse.urlencode(kwargs),
        content_type="application/x-www-form-urlencoded",
    )
    rootview = RootView()
    response = rootview.post(request)
    Profile.objects.filter(profile_name=kwargs["name"]).delete()
    assert json.loads(response.content) == responses.ok


@given(
    user=from_model(User),
    name=st.text(min_size=1),
    photo=st.integers(),
    private=st.sampled_from(["true", "false"]),
)
@pytest.mark.django_db(transaction=True, reset_sequences=True)
def test_duplicate_root_post(user, **kwargs):
    Profile.objects.filter(profile_name=kwargs["name"]).delete()
    kwargs["userid"] = user.pk
    path = reverse("profileapp:root")
    request = RequestFactory().post(
        path,
        data=urllib.parse.urlencode(kwargs),
        content_type="application/x-www-form-urlencoded",
    )
    rootview = RootView()
    response = rootview.post(request)
    response = rootview.post(request)
    Profile.objects.filter(profile_name=kwargs["name"]).delete()
    assert json.loads(response.content) == responses.profileAlreadyRegistered
