#Server Deployement Script
mvn clean install
sudo /usr/share/apache-tomcat-7.0.57/bin/catalina.sh stop -force
sudo rm -rf /usr/share/apache-tomcat-7.0.57/webapps/smartserver/
sudo /usr/share/apache-tomcat-7.0.57/bin/catalina.sh start
mvn tomcat7:redeploy