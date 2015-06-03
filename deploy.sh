#Deployement Script
set -e
mvn clean install
/usr/share/apache-tomcat-7.0.57/bin/catalina.sh stop
rm -rf /usr/share/apache-tomcat-7.0.57/webapps/smartserver/
/usr/share/apache-tomcat-7.0.57/bin/catalina.sh start
mvn tomcat7:redeploy