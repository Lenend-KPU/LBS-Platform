from likeapp.models import Like
from django.contrib import admin


class LikeAdmin(admin.ModelAdmin):
    list_display = ["id", "document", "profile", "like_data"]


admin.site.register(Like, LikeAdmin)
