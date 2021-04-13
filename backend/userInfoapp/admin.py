from userInfoapp.models import UserInfo
from django.contrib import admin

class UserInfoAdmin(admin.ModelAdmin):
    list_display = ['id', 'user', 'visit', 'fdate', 'ldate']

admin.site.register(UserInfo, UserInfoAdmin)
