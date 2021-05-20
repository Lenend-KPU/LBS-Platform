#!/bin/bash

export CURDIR=$(pwd);cat deployment.yml | envsubst | kubectl apply -f -
