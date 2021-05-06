from django.db import models
from profileapp.models import Profile
from django.db import models


class Maximum(models.Model):
    profile = models.OneToOneField(
        Profile, on_delete=models.CASCADE, related_name="profile"
    )
    maximum_place = models.IntegerField(default=0)
    maximum_document = models.IntegerField(default=0)
    maximum_comment = models.IntegerField(default=0)
    maximum_like = models.IntegerField(default=0)
    maximum_save = models.IntegerField(default=0)

    class Meta:
        def __str__(self):
            return self.profile
