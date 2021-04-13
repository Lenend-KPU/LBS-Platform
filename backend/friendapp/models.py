from profileapp.models import Profile
from django.db import models

class Friend(models.Model):
    profile = models.ForeignKey(Profile, on_delete=models.CASCADE, related_name='friend_profile')
    follower = models.ForeignKey(Profile, on_delete=models.CASCADE, related_name='friend_follower')
    date = models.DateTimeField(auto_now_add=True)
    status = models.IntegerField(default=0)

    class Meta:
        unique_together = ('profile', 'follower')

