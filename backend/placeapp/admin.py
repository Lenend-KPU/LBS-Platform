from placeapp.models import Place
from django.contrib import admin


class PlaceAdmin(admin.ModelAdmin):
    list_display = [
        "id",
        "profile",
        "place_name",
        "place_rate",
        "place_photo",
        "place_date",
        "place_latitude",
        "place_longitude",
        "place_private",
    ]


admin.site.register(Place, PlaceAdmin)
