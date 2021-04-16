from profileapp.models import Profile
from django.contrib import admin


class ProfileAdmin(admin.ModelAdmin):
    list_display = [
        "id",
        "user",
        "profile_name",
        "profile_photo",
        "profile_follower",
        "profile_following",
        "profile_private",
    ]


admin.site.register(Profile, ProfileAdmin)
