from documentapp.models import Document
from profileapp.models import Profile
from django.db import models

class Like(models.Model):
    document = models.ForeignKey(Document, on_delete=models.CASCADE, related_name='like')
    profile = models.ForeignKey(Profile, on_delete=models.CASCADE)

    class Meta:
        unique_together = ('document', 'profile')