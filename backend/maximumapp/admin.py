from .models import Maximum
from django.contrib import admin


class MaximumAdmin(admin.ModelAdmin):
    list_display = [
        "id",
        "profile",
        "maximum_place",
        "maximum_document",
        "maximum_comment",
        "maximum_like",
        "maximum_save",
    ]


admin.site.register(Maximum, MaximumAdmin)
