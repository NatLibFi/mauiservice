# Development notes

## Building and running

To build and run, run the following commands:

```shell
mvn clean install
java -jar target/mauiservice-0.0.1-SNAPSHOT.war --configuration=src/test/resources/test-config/test.ini
```

.. or by just using maven

```shell
mvn spring-boot:run -DMAUISERVICE_CONFIGURATION="src/test/resources/test-config/test.ini"
```

## Deploying on a servlet container

Building the project will result in a .war file (under target/). That war file can be deployed in a servlet container. However,
you need to configure where the configuration file is. To do this, you need to add the following option into options given to the JVM:

```
-DMAUISERVICE_CONFIGURATION="/the/path/to/the/configuration.ini"
```

For tomcat8 on Ubuntu, you can set JVM options like this in `/etc/default/tomcat8`:

```
JAVA_OPTS="-Djava.awt.headless=true -Xmx2G -XX:+UseConcMarkSweepGC -DMAUISERVICE_CONFIGURATION=/etc/mauiservice/service.ini"
```

Absolute paths are recommended for servlet containers as relative paths are resolved based on the CWD of the process which might not be what you expect it to be. Note that you can use relative paths within the INI file as mauiservice will resolve them based on the configuration path.

## Configuration file

The configuration file is INI style and specifies any number of configurations. Each configuration specifies a language, pre-trained Maui model file (built using MauiModelBuilder), stemmer, stopwords, vocabulary and vocabulary format. These correspond exactly to Maui options.

Example:

```
[jyu-fin]
language = fi
model = /var/lib/maui/model/jyu-fin
stemmer = CachingFinnishStemmer
stopwords = StopwordsFinnish
vocab = /var/lib/maui/vocab/yso-skos.rdf
vocabformat = skos

[jyu-eng]
language = en
model = /var/lib/maui/model/jyu-eng
stemmer = PorterStemmer
stopwords = StopwordsEnglish
vocab = /var/lib/maui/vocab/yso-skos.rdf
vocabformat = skos

[jyu-swe]
language = sv
model = /var/lib/maui/model/jyu-swe
stemmer = SwedishStemmer
stopwords = StopwordsSwedish
vocab = /var/lib/maui/vocab/yso-skos.rdf
vocabformat = skos
```

## Testing the service

Below are examples of sending a text analysis request to the service. The first example uses JSON encoding and the second the classic application/x-www-form-urlencoded


```shell
# JSON request
curl -H "Content-Type: application/json" -X POST -d '{"text":"Arkeologiaa sanotaan joskus myös muinaistutkimukseksi tai muinaistieteeksi. Se on humanistinen tiede tai oikeammin joukko tieteitä, jotka tutkivat ihmisen menneisyyttä. Tutkimusta tehdään analysoimalla muinaisjäännöksiä eli niitä jälkiä, joita ihmisten toiminta on jättänyt maaperään tai vesistöjen pohjaan."}' http://localhost:8080/maui/test/analyze

# JSON request with limit
curl -H "Content-Type: application/json" -X POST -d '{"text":"Arkeologiaa sanotaan joskus myös muinaistutkimukseksi tai muinaistieteeksi. Se on humanistinen tiede tai oikeammin joukko tieteitä, jotka tutkivat ihmisen menneisyyttä. Tutkimusta tehdään analysoimalla muinaisjäännöksiä eli niitä jälkiä, joita ihmisten toiminta on jättänyt maaperään tai vesistöjen pohjaan.","parameters":{"limit":1}}' http://localhost:8080/maui/test/analyze

# form-urlencoded request
curl -H "Content-Type: application/x-www-form-urlencoded" -X POST -d 'text=Arkeologiaa sanotaan joskus myös muinaistutkimukseksi tai muinaistieteeksi. Se on humanistinen tiede tai oikeammin joukko tieteitä, jotka tutkivat ihmisen menneisyyttä. Tutkimusta tehdään analysoimalla muinaisjäännöksiä eli niitä jälkiä, joita ihmisten toiminta on jättänyt maaperään tai vesistöjen pohjaan' http://localhost:8080/maui/test/analyze

# form-urlencoded request with limit
curl -H "Content-Type: application/x-www-form-urlencoded" -X POST -d 'text=Arkeologiaa sanotaan joskus myös muinaistutkimukseksi tai muinaistieteeksi. Se on humanistinen tiede tai oikeammin joukko tieteitä, jotka tutkivat ihmisen menneisyyttä. Tutkimusta tehdään analysoimalla muinaisjäännöksiä eli niitä jälkiä, joita ihmisten toiminta on jättänyt maaperään tai vesistöjen pohjaan&parameters.limit=1' http://localhost:8080/maui/test/analyze
```

To send files from your local disk, you can use multipart/form-data. The example below expects you have a file called input.txt in your current working directory.
```shell
# File upload (default charset utf-8)
curl -F text=@input.txt http://localhost:8080/maui/test/analyze

# File upload with specified character set
curl -F charset=iso-8859-15 -F text=@input.txt http://localhost:8080/maui/test/analyze

# File upload with result limit
curl -F limit=1 -F text=@input.txt http://localhost:8080/maui/test/analyze
```

## Testing thread safety

You need JMeter (version 4.0 was used) to run the threading test. 

First, start the server:

```shell
mvn spring-boot:run -DMAUISERVICE_CONFIGURATION="src/test/resources/test-config/threading-test.ini"
```

Second, start jmeter using the test plan misc/threading-test.jmx

Third, start the test. Any errors appearing in the listeners ("Errors" and "Aggregate Report") need to be investigated.

## Deploying a snapshot to Sonatype repository

```shell
mvn clean deploy
```

## Deploying a release to Maven central

This has been tested to work with Maven 3.5.2

```shell
mvn clean
mvn release:prepare
mvn release:perform
```

If the release:perform phase fails for some reason, run the following commands before attempting the next release. Note, these instructions are untested and it is possible that release:rollback removes the tags.

```shell
mvn release:rollback
git tag -d mauiservice-[version]
git push origin :mauiservice-[version]
```

# Caveats in the implementation

The Maui API is not thread safe. However the REST API is protected internally so that parallel annotation tasks are never run on the same configuration. Parallel annotation is possible between different configurations. As such, ther is no risk to the user of the service, but it is important to note that this means annotating cannot be scaled to parallel processor unless multiple instances of mauiservice are deployed.


# Usage with Docker

The docker image for Mauiservice can be build with
```shell
docker build -t mauiservice .
```

A model can be trained with 
```shell
docker run -v /path/to/annif-projects/:/annif-projects/ --rm mauiservice \
  java -Xmx4G -cp maui-1.4.5-jar-with-dependencies.jar com.entopix.maui.main.MauiModelBuilder -l /annif-projects/Annif-corpora/fulltext/kirjastonhoitaja/maui-train/ -m /annif-projects/kirjastonhoitaja -v /annif-projects/Annif-corpora/vocab/yso-skos.rdf -f skos -i fi -s StopwordsFinnish -t CachingFinnishStemmer
```
Here the training data (`kirjastonhoitaja` (Ask a Librarian) collection) and vocabulary (SKOS) files are bind-mounted into the container from the host system in `/path/to/annif-projects/Annif-corpora/`. 

The service can then be started with 
```shell
docker run --name mauiservice -v /path/to/annif-projects/:/annif-projects/ --rm --network="host" mauiservice
```

Here the use of `--network="host"` allows connecting to the service from the host system, e.g. [using Annif](https://github.com/NatLibFi/Annif/wiki/Usage-with-Docker#connecting-to-mauiservice-in-docker-container).

