from django.db import models
from profileapp.models import Profile

class Document(models.Model):
    name = models.CharField(max_length=20)
    profile = models.ForeignKey(Profile, on_delete=models.CASCADE, related_name='document')
    liked = models.IntegerField(default=0)
    date = models.DateTimeField(auto_now_add=True)
    private = models.BooleanField(default=False)
    color = models.CharField(max_length=20)

    def __str__(self):
        return self.name
