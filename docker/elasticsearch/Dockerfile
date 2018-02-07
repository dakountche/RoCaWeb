FROM docker.elastic.co/elasticsearch/elasticsearch:5.6.3

MAINTAINER Djibrilla AMADOU KOUNTCHE <djibrilla.amadoukountche@gmail.com>

RUN bin/elasticsearch-plugin remove ingest-geoip
#RUN /usr/share/elasticsearch/bin/elasticsearch-plugin install lmenezes/elasticsearch-kopf
ENV ES_JAVA_OPTS="-Dlog4j2.disable.jmx=true -Xms512m -Xmx512m"
