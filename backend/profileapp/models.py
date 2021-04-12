from userapp.models import User
from django.db import models

class Profile(models.Model):
    nickname = models.CharField(max_length=20)
    photo = models.CharField(max_length=20)
    private = models.BooleanField(default=False)
    user = models.OneToOneField(User, on_delete=models.CASCADE, related_name='profile')
    follower = models.IntegerField(default=0)
    following = models.IntegerField(default=0)
