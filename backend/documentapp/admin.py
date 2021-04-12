from documentapp.models import Document
from django.contrib import admin

class DocumentAdmin(admin.ModelAdmin):
    list_display = ['id', 'profile', 'name', 'like', 'date', 'private', 'color']

admin.site.register(Document, DocumentAdmin)
