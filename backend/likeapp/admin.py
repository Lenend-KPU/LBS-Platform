from likeapp.models import Like
from django.contrib import admin

class LikeAdmin(admin.ModelAdmin):
    list_display = ['id', 'document', 'profile']

admin.site.register(Like, LikeAdmin)
