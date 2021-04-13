from documentapp.models import Document
from profileapp.models import Profile
from django.db import models

class Save(models.Model):
    profile = models.ForeignKey(Profile, on_delete=models.CASCADE, related_name='saved')
    document = models.ForeignKey(Document, on_delete=models.CASCADE)
    date = models.DateTimeField(auto_now_add=True)
    comment = models.CharField(max_length=20)