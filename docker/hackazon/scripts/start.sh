#!/bin/bash

#mysql has to be started this way as it doesn't work to call from /etc/init.d
/usr/bin/mysqld_safe &
sleep 10s
# Here we generate random passwords (thank you pwgen!). The first two are for mysql users, the last batch for random keys in wp-config.php
MYSQL_USER="root"
MYSQL_PASSWORD="hackmesilly"
HACKAZON_DB="hackazon"
HACKAZON_USER="hackazon"
HACKAZON_PASSWORD='hackmesilly'

#This is so the passwords show up in logs.
echo hackazon password: $HACKAZON_PASSWORD
echo $MYSQL_PASSWORD > /mysql-root-pw.txt
echo $HACKAZON_PASSWORD > /hackazon-db-pw.txt
#there used to be a huge ugly line of sed and cat and pipe and stuff below,
#but thanks to @djfiander's thing at https://gist.github.com/djfiander/6141138
#there isn't now.

sudo mysqladmin -u root password $MYSQL_PASSWORD
sudo mysql -uroot -p$MYSQL_PASSWORD -e "CREATE DATABASE $HACKAZON_DB; GRANT ALL PRIVILEGES ON $HACKAZON_DB.* TO '$HACKAZON_USER'@'localhost' IDENTIFIED BY '$HACKAZON_PASSWORD'; FLUSH PRIVILEGES;"
killall mysqld
sleep 10s

supervisord -n
