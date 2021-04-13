from saveapp.models import Save
from django.contrib import admin

class SaveAdmin(admin.ModelAdmin):
    list_display = ['id', 'profile', 'document', 'date', 'comment']

admin.site.register(Save, SaveAdmin)
