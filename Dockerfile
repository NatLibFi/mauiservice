FROM tomcat:8-jdk11

RUN apt-get update \
	## Voikko:
	&& apt-get install -y --no-install-recommends \
		libvoikko1 \
		voikko-fi \
	## Clean up:
	&& rm -rf /var/lib/apt/lists/* /usr/include/*

COPY maui-1.4.5-jar-with-dependencies.jar /srv/maui/
COPY mauiservice.ini kirjastonhoitaja /srv/maui/
COPY mauiservice-1.0.2.war /usr/local/tomcat/webapps/mauiservice.war

ENV JAVA_OPTS="-Djava.awt.headless=true -Xmx2G -XX:+UseConcMarkSweepGC -DMAUISERVICE_CONFIGURATION=/srv/maui/mauiservice.ini"
WORKDIR /srv/maui
