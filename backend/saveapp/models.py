from documentapp.models import Document
from profileapp.models import Profile
from django.db import models


class Save(models.Model):
    profile = models.ForeignKey(Profile, on_delete=models.CASCADE, related_name="saved")
    document = models.ForeignKey(Document, on_delete=models.CASCADE)
    save_date = models.DateTimeField(auto_now_add=True)
    save_comment = models.CharField(max_length=20)

    class Meta:
        unique_together = ("profile" , "document")