from userapp.models import User
from django.contrib import admin

class UserAdmin(admin.ModelAdmin):
    list_display = ['id', 'name', 'email', 'address']

admin.site.register(User, UserAdmin)
