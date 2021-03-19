FROM python:3.9
ENV PYTHONUNBUFFERED 1

RUN apt-get -y update 

ARG Django_secret_key
ENV Django_secret_key $Django_secret_key
ENV BOARD_DEBUG 1

# 유저, 그룹 나중에 수정 TODO
# the user to run as
ENV USER root

# how many worker processes should Gunicorn spawn
ENV NUM_WORKERS 3

# which settings file should Django use
ENV DJANGO_SETTINGS_MODULE backend.settings

# WSGI module name
ENV DJANGO_WSGI_MODULE backend.wsgi

ENV PORT 8000

RUN echo "Starting $NAME as $(whoami)"

WORKDIR /code
COPY . /code/

RUN pip install pipenv
RUN pipenv install --deploy --ignore-pipfile

# ENTRYPOINT ["sh -c", "pipenv run python manage.py runserver 0.0.0.0:8000"]
# RUN chown root board/migrations
# RUN chown root ./

# ENTRYPOINT ["sh", "/code/bin/gunicorn_start"]