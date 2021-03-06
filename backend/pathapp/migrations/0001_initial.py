# Generated by Django 3.1.6 on 2021-04-25 11:46

from django.db import migrations, models
import django.db.models.deletion


class Migration(migrations.Migration):

    initial = True

    dependencies = [
        ('documentapp', '0001_initial'),
        ('placeapp', '0001_initial'),
    ]

    operations = [
        migrations.CreateModel(
            name='Path',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('path_order', models.IntegerField()),
                ('document', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, related_name='path', to='documentapp.document')),
                ('place', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to='placeapp.place')),
            ],
            options={
                'unique_together': {('document', 'place')},
            },
        ),
    ]
