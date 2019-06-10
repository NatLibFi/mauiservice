FROM tomcat:8-jdk11

RUN apt-get update \
	## Voikko:
	&& apt-get install -y --no-install-recommends \
		libvoikko1 \
		voikko-fi \
	## Clean up:
	&& rm -rf /var/lib/apt/lists/* /usr/include/*


RUN mkdir /srv/maui/ \
	&& wget https://search.maven.org/remotecontent?filepath=fi/nationallibrary/maui/1.4.5/maui-1.4.5-jar-with-dependencies.jar \
		-O /srv/maui/maui-1.4.5-jar-with-dependencies.jar -q \
	&& wget https://search.maven.org/remotecontent?filepath=fi/nationallibrary/mauiservice/1.0.2/mauiservice-1.0.2.war \
		-O /usr/local/tomcat/webapps/mauiservice.war -q

COPY mauiservice.ini /srv/maui/

ENV JAVA_OPTS="-Djava.awt.headless=true -Xmx2G -XX:+UseConcMarkSweepGC -DMAUISERVICE_CONFIGURATION=/srv/maui/mauiservice.ini"

WORKDIR /srv/maui
