from profileapp.models import Profile
from django.contrib import admin

class ProfileAdmin(admin.ModelAdmin):
    list_display = ['id', 'user', 'nickname', 'photo', 'following', 'follower', 'private']

admin.site.register(Profile, ProfileAdmin)
