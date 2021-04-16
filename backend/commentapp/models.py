from documentapp.models import Document
from profileapp.models import Profile
from django.db import models


class Comment(models.Model):
    document = models.ForeignKey(
        Document, on_delete=models.CASCADE, related_name="comment"
    )
    profile = models.ForeignKey(Profile, on_delete=models.CASCADE)
    comment_text = models.TextField()
    comment_date = models.DateTimeField(auto_now_add=True)