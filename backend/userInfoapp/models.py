from userapp.models import User
from django.db import models


class UserInfo(models.Model):
    user = models.OneToOneField(User, on_delete=models.CASCADE, related_name="userInfo")
    user_visit = models.CharField(max_length=20)
    user_fdate = models.DateTimeField()
    user_ldate = models.DateTimeField()
