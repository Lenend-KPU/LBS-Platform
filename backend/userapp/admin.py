from userapp.models import User
from django.contrib import admin


class UserAdmin(admin.ModelAdmin):
    list_display = ["id", "user_name", "user_password", "user_email", "user_address"]


admin.site.register(User, UserAdmin)
