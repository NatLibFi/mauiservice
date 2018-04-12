# Development notes

## Building and running

To build and run, run the following commands:

```shell
mvn clean install
java -jar target/mauiservice-0.0.1-SNAPSHOT.jar --configuration=src/test/resources/test-config/test.ini
```

.. or by just using maven

```shell
mvn spring-boot:run -Dspring-boot.run.arguments="--configuration=src/test/resources/test-config/test.ini"
```

## Testing the service

Below are examples of sending a text analysis request to the service. The first example uses JSON encoding and the second the classic application/x-www-form-urlencoded


```shell
curl -H "Content-Type: application/json" -X POST -d '{"text":"Arkeologiaa sanotaan joskus myös muinaistutkimukseksi tai muinaistieteeksi. Se on humanistinen tiede tai oikeammin joukko tieteitä, jotka tutkivat ihmisen menneisyyttä. Tutkimusta tehdään analysoimalla muinaisjäännöksiä eli niitä jälkiä, joita ihmisten toiminta on jättänyt maaperään tai vesistöjen pohjaan."}' http://localhost:8080/maui/test/analyze

curl -H "Content-Type: application/x-www-form-urlencoded" -X POST -d 'text=Arkeologiaa sanotaan joskus myös muinaistutkimukseksi tai muinaistieteeksi. Se on humanistinen tiede tai oikeammin joukko tieteitä, jotka tutkivat ihmisen menneisyyttä. Tutkimusta tehdään analysoimalla muinaisjäännöksiä eli niitä jälkiä, joita ihmisten toiminta on jättänyt maaperään tai vesistöjen pohjaan' http://localhost:8080/maui/test/analyze
```



## Deploying a snapshot to Maven central


## Deploying a release to Maven central

