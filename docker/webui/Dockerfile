FROM openjdk:8

MAINTAINER Djibrilla  Amadou Kountché  <djibrilla.amadoukountche@gmail.com>

RUN apt-get update --yes && apt-get install --yes nodejs

COPY rocaweb-web-user-interface-0.1-SNAPSHOT.tar.xz /tmp

RUN tar xf  /tmp/rocaweb-web-user-interface-0.1-SNAPSHOT.tar.xz -C /opt/


ENV PATH /opt/rocaweb-web-user-interface-0.1-SNAPSHOT/bin:$PATH

ENV SBT_OPTS "${SBT_OPTS} -Dsbt.jse.engineType=Node -Dsbt.jse.command=$(which node)"
