from userapp.models import User
from django.db import models


class Profile(models.Model):
    user = models.OneToOneField(User, on_delete=models.CASCADE, related_name="profile")
    profile_name = models.CharField(max_length=20, unique=True)
    profile_photo = models.CharField(max_length=100)
    profile_follower = models.IntegerField(default=0)
    profile_following = models.IntegerField(default=0)
    profile_private = models.BooleanField(default=False)

    def __str__(self):
        return self.profile_name
