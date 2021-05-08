# Generated by Django 3.2 on 2021-05-05 02:56

from django.db import migrations, models
import django.db.models.deletion


class Migration(migrations.Migration):

    initial = True

    dependencies = [
        ('profileapp', '0001_initial'),
    ]

    operations = [
        migrations.CreateModel(
            name='Maximum',
            fields=[
                ('id', models.BigAutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('maximum_place', models.IntegerField(default=0)),
                ('maximum_document', models.IntegerField(default=0)),
                ('maximum_comment', models.IntegerField(default=0)),
                ('maximum_like', models.IntegerField(default=0)),
                ('maximum_save', models.IntegerField(default=0)),
                ('profile', models.OneToOneField(on_delete=django.db.models.deletion.CASCADE, related_name='profile', to='profileapp.profile')),
            ],
        ),
    ]