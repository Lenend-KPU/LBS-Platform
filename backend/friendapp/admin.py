from friendapp.models import Friend
from django.contrib import admin


class FreindAdmin(admin.ModelAdmin):
    list_display = ["id", "profile", "friend_profile", "friend_date", "friend_status"]


admin.site.register(Friend, FreindAdmin)
