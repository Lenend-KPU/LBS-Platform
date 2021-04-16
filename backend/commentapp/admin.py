from commentapp.models import Comment
from django.contrib import admin


class CommentAdmin(admin.ModelAdmin):
    list_display = ["id", "document", "profile", "comment_text", "comment_date"]


admin.site.register(Comment, CommentAdmin)
