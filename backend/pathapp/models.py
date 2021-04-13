from django.db.models.base import Model
from documentapp.models import Document
from placeapp.models import Place
from django.db import models

class Path(models.Model):
    document = models.ForeignKey(Document, on_delete=models.CASCADE, related_name='path')
    place = models.ForeignKey(Place, on_delete=models.CASCADE)
    order = models.IntegerField(null=False)

    class Meta:
        unique_together = ('document', 'place')