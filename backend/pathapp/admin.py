from pathapp.models import Path
from django.contrib import admin

class PathAdmin(admin.ModelAdmin):
    list_display = ['id', 'document', 'place', 'order']

admin.site.register(Path, PathAdmin)