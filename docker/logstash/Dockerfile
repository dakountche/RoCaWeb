FROM docker.elastic.co/logstash/logstash:5.6.3

MAINTAINER Djibrilla AMADOU KOUNTCHE <djibrilla.amadoukountche@gmail.com>

RUN /usr/share/logstash/bin/logstash-plugin install logstash-filter-translate
RUN /usr/share/logstash/bin/logstash-plugin install logstash-codec-multiline   
RUN /usr/share/logstash/bin/logstash-plugin install --no-verify logstash-filter-multiline  
RUN /usr/share/logstash/bin/logstash-plugin install logstash-filter-urldecode
COPY  logstash_modsecurity_patterns  /usr/share/logstash/vendor/bundle/jruby/1.9/gems/logstash-patterns-core-4.1.2/patterns/logstash_modsecurity_patterns
