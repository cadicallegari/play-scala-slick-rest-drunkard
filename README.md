# play-scala-slick-rest-drunkard

It supports the following features:

* Generic Data Access layer, using [slick-repo](https://github.com/gonmarques/slick-repo)

The project was thought to be used as an activator template.

## Running

First you need to run the postgres database

```
docker-compose up -d
```

```
sbt run
```

## Testing

To run all tests (routes and persistence tests):

```
sbt test
```

## Using

```
curl --request POST localhost:9000/records -H "Content-type: application/json" --data "{\"pk\" : \"some-pk\",\"score\" : \"54645\"}"
```

```
curl localhost:9000/records/1
```

## Credits

It is based on https://github.com/cdiniz/play-slick-rest
