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
curl -H "Content-Type: application/json" -X POST -d '{"text":"Arkeologiaa sanotaan joskus myös muinaistutkimukseksi tai muinaistieteeksi. Se on humanistinen tiede tai oikeammin joukko tieteitä, jotka tutkivat ihmisen menneisyyttä. Tutkimusta tehdään analysoimalla muinaisjäännöksiä eli niitä jälkiä, joita ihmisten toiminta on jättänyt maaperään tai vesistöjen pohjaan."}' http://localhost:8080/maui/test/analyze

curl -H "Content-Type: application/x-www-form-urlencoded" -X POST -d 'text=Arkeologiaa sanotaan joskus myös muinaistutkimukseksi tai muinaistieteeksi. Se on humanistinen tiede tai oikeammin joukko tieteitä, jotka tutkivat ihmisen menneisyyttä. Tutkimusta tehdään analysoimalla muinaisjäännöksiä eli niitä jälkiä, joita ihmisten toiminta on jättänyt maaperään tai vesistöjen pohjaan' http://localhost:8080/maui/test/analyze
```



## Deploying a snapshot to Maven central


## Deploying a release to Maven central


# Caveats in the implementation

The Maui API is not thread safe, at least yet.
