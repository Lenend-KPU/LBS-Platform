from profileapp.models import Profile
from django.db import models


class Place(models.Model):
    profile = models.ForeignKey(Profile, on_delete=models.CASCADE, related_name="place")
    place_name = models.CharField(max_length=20)
    place_rate = models.CharField(max_length=20)
    place_photo = models.CharField(max_length=100)
    place_date = models.DateTimeField(auto_now_add=True)
    place_map = models.CharField(max_length=100)  # 이미지 경로
    place_latitude = models.DecimalField(max_digits=9, decimal_places=6)
    place_longitude = models.DecimalField(max_digits=9, decimal_places=6)
    place_private = models.BooleanField(default=True)

    def __str__(self):
        return self.name
