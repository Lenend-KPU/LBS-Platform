from saveapp.models import Save
from django.contrib import admin


class SaveAdmin(admin.ModelAdmin):
    list_display = ["id", "profile", "document", "save_date"]


admin.site.register(Save, SaveAdmin)
