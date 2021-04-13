# Generated by Django 3.1.6 on 2021-04-12 14:33

from django.db import migrations, models
import django.db.models.deletion


class Migration(migrations.Migration):

    initial = True

    dependencies = [
        ('profileapp', '0002_auto_20210412_1424'),
    ]

    operations = [
        migrations.CreateModel(
            name='Friend',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('date', models.DateTimeField(auto_now_add=True)),
                ('status', models.IntegerField(default=0)),
                ('follower', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, related_name='friend_follower', to='profileapp.profile')),
                ('profile', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, related_name='friend_profile', to='profileapp.profile')),
            ],
            options={
                'unique_together': {('profile', 'follower')},
            },
        ),
    ]