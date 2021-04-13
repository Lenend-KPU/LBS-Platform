from userapp.models import User
from django.db import models

class UserInfo(models.Model):
    visit = models.CharField(max_length=20)
    fdate = models.DateTimeField()
    ldate = models.DateTimeField()
    user = models.OneToOneField(User, on_delete=models.CASCADE, related_name='userInfo')
