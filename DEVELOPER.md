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

Absolute paths are recommended for servlet containers as relative paths are resolved based on the CWD of the process which might not be what you expect it to be. Note that you can use relative paths within the INI file as mauiservice will resolve them based on the configuration path.


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

## Deploying a snapshot to Maven central


## Deploying a release to Maven central


# Caveats in the implementation

The Maui API is not thread safe, at least yet.
