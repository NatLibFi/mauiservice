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
curl -H "Content-Type: application/json" -X POST -d '{"text":"Puolustusvoimien ortoilmakuvat ovat koko maan kattava oikaistu ilmakuva-aineisto. Ortoilmakuva vastaa geometrialtaan karttaa. Maastoresoluutio on 1 m. Puolustusvoimien ortoilmakuvia ei enää päivitetä. Ortoilmakuvia käytetään kartoituksessa, ympäristön suunnittelussa ja muutosten seurannassa. Mustavalkoinen ortoilmakuva sopii suunnitteluun tai tausta-aineistoksi erilaisille karttaesityksille."}' http://localhost:8080/maui/test/analyze

curl -H "Content-Type: application/x-www-form-urlencoded" -X POST -d 'text=Puolustusvoimien%20ortoilmakuvat%20ovat%20koko%20maan%20kattava%20oikaistu%20ilmakuva-aineisto.%20Ortoilmakuva%20vastaa%20geometrialtaan%20karttaa.%20Maastoresoluutio%20on%201%20m.%20Puolustusvoimien%20ortoilmakuvia%20ei%20en%C3%A4%C3%A4%20p%C3%A4ivitet%C3%A4.%20Ortoilmakuvia%20k%C3%A4ytet%C3%A4%C3%A4n%20kartoituksessa%2C%20ymp%C3%A4rist%C3%B6n%20suunnittelussa%20ja%20muutosten%20seurannassa.%20Mustavalkoinen%20ortoilmakuva%20sopii%20suunnitteluun%20tai%20tausta-aineistoksi%20erilaisille%20karttaesityksille.' http://localhost:8080/maui/test/analyze
```



## Deploying a snapshot to Maven central


## Deploying a release to Maven central

