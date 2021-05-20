#!/bin/bash

export Django_secret_key='_44xeg*1w4*jn^l=c$9p=hh*b#=2vz1z%+_wuk6ch*v!tz!=$*'
export DJANGO_SUPERUSER_PASSWORD='123qwe'
export DJANGO_SUPERUSER_USERNAME='wooogy'
export DJANGO_SUPERUSER_EMAIL='hswook5347@gmail.com'
export POSTGRES_USER='django'
export POSTGRES_PASSWORD='postgres'
export POSTGRES_DB='lbs_platform'
export CURDIR=$(pwd);cat deployment.yml | envsubst | kubectl apply -f -
