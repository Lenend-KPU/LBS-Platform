from friendapp.models import Friend
from django.contrib import admin

class FreindAdmin(admin.ModelAdmin):
    list_display = ['id', 'profile', 'follower', 'status', 'date']

admin.site.register(Friend, FreindAdmin)
