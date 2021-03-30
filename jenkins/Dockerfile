FROM jenkins/jenkins:lts

ARG HOST_UID=1004
ARG HOST_GID=999

USER root
RUN curl -fsSL https://get.docker.com -o get-docker.sh && sh get-docker.sh
RUN curl -L "https://github.com/docker/compose/releases/download/1.28.6/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose ; chmod +x /usr/local/bin/docker-compose

#install sudo
RUN apt-get update \
    && apt-get install -y sudo \
    && rm -rf /var/lib/apt/lists/*

#Adding jenkins to sudoers list and making an alias for sudo docker
RUN echo "jenkins ALL=NOPASSWD: ALL" >> /etc/sudoers \
    && printf '#!/bin/bash\nsudo /usr/bin/docker "$@"' > /usr/local/bin/docker \
    && chmod +x /usr/local/bin/docker

USER jenkins