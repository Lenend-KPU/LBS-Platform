# Generated by Django 3.1.6 on 2021-04-12 15:06

from django.db import migrations


class Migration(migrations.Migration):

    dependencies = [
        ('documentapp', '0003_auto_20210412_1451'),
        ('profileapp', '0002_auto_20210412_1424'),
        ('likeapp', '0001_initial'),
    ]

    operations = [
        migrations.AlterUniqueTogether(
            name='like',
            unique_together={('document', 'profile')},
        ),
    ]