from django.db import models


class User(models.Model):
    user_name = models.CharField(max_length=20)
    user_password = models.CharField(max_length=20)
    user_email = models.CharField(max_length=20, unique=True)
    user_address = models.CharField(max_length=20)

    def __str__(self):
        return self.email