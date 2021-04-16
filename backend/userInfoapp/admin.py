from userInfoapp.models import UserInfo
from django.contrib import admin


class UserInfoAdmin(admin.ModelAdmin):
    list_display = ["id", "user", "user_visit", "user_fdate", "user_ldate"]


admin.site.register(UserInfo, UserInfoAdmin)
