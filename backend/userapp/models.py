from django.db import models

max_length = 100


class User(models.Model):
    user_name = models.CharField(max_length=max_length)
    # 헬퍼 함수를 통해 해시로 변환한 값
    user_password = models.CharField(max_length=max_length)
    user_email = models.CharField(max_length=max_length, unique=True)
    user_address = models.CharField(max_length=max_length)

    def __str__(self):
        return str(super().pk)
