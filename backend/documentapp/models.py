from django.db import models
from profileapp.models import Profile


class Document(models.Model):
    profile = models.ForeignKey(
        Profile, on_delete=models.CASCADE, related_name="document_profile"
    )
    profile_friend = models.ForeignKey(
        Profile,
        on_delete=models.CASCADE,
        related_name="document_profile_friend",
        null=True,
    )
    document_name = models.CharField(max_length=20)
    document_color = models.CharField(max_length=20)
    document_map = models.CharField(max_length=100)  # 이미지 경로
    document_date = models.DateTimeField(auto_now_add=True)
    document_liked = models.IntegerField(default=0)
    document_private = models.BooleanField(default=False)

    def __str__(self):
        return str(self.pk)
