FROM tomcat:10.1-jdk17-corretto

RUN rm -rf /usr/local/tomcat/webapps/*

COPY target/social-war.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080

CMD ["catalina.sh", "run"]