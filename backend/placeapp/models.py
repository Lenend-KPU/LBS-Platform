from profileapp.models import Profile
from django.db import models

class Place(models.Model):
    profile = models.ForeignKey(Profile, on_delete=models.CASCADE, related_name='place')
    name = models.CharField(max_length=20)
    rate = models.CharField(max_length=20)
    photo = models.CharField(max_length=20)
    date = models.DateTimeField(auto_now_add=True)
    private = models.BooleanField(default=False)

    def __str__(self):
        return self.name
