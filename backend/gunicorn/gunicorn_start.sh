#!/bin/bash
# Name of the application
NAME="backend"

# https://stackoverflow.com/questions/4774054/reliable-way-for-a-bash-script-to-get-the-full-path-to-itself
SCRIPT_PATH=$(dirname `which $0`)

# Django project directory
# . 경로
DJANGODIR=$SCRIPT_PATH

# /Users/Han/programming/DjangoCRUDBoard
PARENT_PATH=$(cd $SCRIPT_PATH ; cd .. ; pwd)

# we will communicte using this unix socket
SOCKFILE=$PARENT_PATH/run/gunicorn.sock

echo $PARENT_PATH

# Activate the virtual environment
export DJANGO_SETTINGS_MODULE=$DJANGO_SETTINGS_MODULE
export PYTHONPATH=$DJANGODIR:$PYTHONPATH

# Create the run directory if it doesn't exist
RUNDIR=$(dirname $SOCKFILE)
test -d $RUNDIR || mkdir -p $RUNDIR

cd $PARENT_PATH

# # DB 연결될때까지 블로킹 (미그레이션은 DB가 연결되어야 가능하다)
while ! nc -z database 5432; do sleep 1; done;


pip install --upgrade pip
pip install pipenv
pipenv install
pipenv run python manage.py makemigrations
pipenv run python manage.py migrate
pipenv run python manage.py collectstatic --noinput
pipenv run python manage.py createsuperuserwithpassword \
        --username $DJANGO_SUPERUSER_USERNAME \
        --password $DJANGO_SUPERUSER_PASSWORD \
        --email $DJANGO_SUPERUSER_EMAIL \
        --preserve
# pipenv run python manage.py createsuperuser --username $DJANGO_SUPERUSER_USERNAME --noinput --password $DJANGO_SUPERUSER_PASSWORD --email $DJANGO_SUPERUSER_EMAIL

# Start your Django Unicorn
# Programs meant to be run under supervisor should not daemonize themselves (do not use --daemon)
# pipenv 사용
exec pipenv run gunicorn ${DJANGO_WSGI_MODULE}:application \
-b 0.0.0.0:$PORT \
--name $NAME \
--workers $NUM_WORKERS \
--user=$USER \
--bind=unix:$SOCKFILE \
--log-level=debug \
--log-file=-