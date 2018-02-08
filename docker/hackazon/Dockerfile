FROM debian:latest
MAINTAINER Djibrilla AMADOU KOUNTCHE <djibrilla.amadoukountche@gmail.com> 
RUN DEBIAN_FRONTEND=noninteractive apt-get update -y && DEBIAN_FRONTEND=noninteractive  apt-get install -y \
                                   apache2 \
                                   libapache2-mod-php \                                   
                                   mysql-client \
                                   mysql-server \
                                   php-bcmath \
                                   php-ldap \
                                   php-mysql \                                   
                                   pwgen \
                                   python-setuptools \
                                   sudo \
                                   supervisor \
                                   unzip
                                   
ADD ./scripts/start.sh /start.sh
ADD ./scripts/foreground.sh /etc/apache2/foreground.sh
ADD ./configs/supervisord.conf /etc/supervisord.conf
ADD ./configs/000-default.conf /etc/apache2/sites-available/000-default.conf
COPY ./hackazon-master.zip /tmp/hackazon.zip
RUN unzip /tmp/hackazon.zip -d /tmp/
RUN mv /tmp/hackazon-master /var/www/hackazon
RUN chown -R www-data:www-data /var/www/
RUN chown -R www-data:www-data /var/www/hackazon/web/products_pictures/
RUN chown -R www-data:www-data /var/www/hackazon/web/upload
RUN chown -R www-data:www-data /var/www/hackazon/assets/config
RUN chmod 755 /start.sh
RUN chmod 755 /etc/apache2/foreground.sh
RUN a2enmod rewrite
EXPOSE 80
CMD ["/bin/bash", "/start.sh"]
