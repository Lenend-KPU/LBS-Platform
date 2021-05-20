#!/bin/bash

export Django_secret_key='_44xeg*1w4*jn^l=c$9p=hh*b#=2vz1z%+_wuk6ch*v!tz!=$*'
export DJANGO_SUPERUSER_PASSWORD='123qwe'
export DJANGO_SUPERUSER_USERNAME='wooogy'
export DJANGO_SUPERUSER_EMAIL='hswook5347@gmail.com'
export POSTGRES_USER='django'
export POSTGRES_PASSWORD='postgres'
export POSTGRES_DB='lbs_platform'
export IS_LOCAL='1'

sudo mkdir -p /var/lib/postgresql/data/django/
sudo chmod -R 777 /var/lib/postgresql/data/django/
sudo mkdir -p /var/jenkins_home/workspace/prometheus/data
sudo chmod -R 777 /var/jenkins_home/workspace/prometheus/data
sudo mkdir -p /var/jenkins_home/workspace/data/db
sudo chmod -R 777 /var/jenkins_home/workspace/data/db
sudo mkdir -p /var/jenkins_home/workspace/grafana
sudo chmod -R 777 /var/jenkins_home/workspace/grafana
sudo mkdir -p /var/jenkins_home/workspace/logs
sudo chmod -R 777 /var/jenkins_home/workspace/logs
sudo mkdir -p ${pwd}/prometheus
sudo chmod -R 777 ${pwd}/prometheus
sudo mkdir -p /var/jenkins_home/workspace
sudo chmod -R 777 /var/jenkins_home/workspace
sudo mkdir -p /var/cgroup
sudo chmod -R 777 /var/cgroup
sudo mkdir -p /var/proc
sudo chmod -R 777 /var/proc
export CURDIR=$(pwd);cat deployment.yml | envsubst | kubectl apply -f -
