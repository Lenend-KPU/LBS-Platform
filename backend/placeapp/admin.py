from placeapp.models import Place
from django.contrib import admin

class PlaceAdmin(admin.ModelAdmin):
    list_display = ['id', 'profile', 'name', 'photo', 'date', 'private']

admin.site.register(Place, PlaceAdmin)
