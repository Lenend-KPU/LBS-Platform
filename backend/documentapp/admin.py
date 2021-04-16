from documentapp.models import Document
from django.contrib import admin


class DocumentAdmin(admin.ModelAdmin):
    list_display = [
        "id",
        "profile",
        "document_name",
        "document_color",
        "document_map",
        "document_date",
        "document_liked",
        "document_private",
    ]


admin.site.register(Document, DocumentAdmin)
