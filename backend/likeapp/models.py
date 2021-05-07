from documentapp.models import Document
from profileapp.models import Profile
from django.db import models


class Like(models.Model):
    profile = models.ForeignKey(Profile, on_delete=models.CASCADE)
    document = models.ForeignKey(
        Document, on_delete=models.CASCADE, related_name="like"
    )
    like_data = models.DateTimeField(auto_now_add=True)

    class Meta:
        unique_together = ("profile", "document")
