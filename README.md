# sun-moon-java-platform-delivery

Delivery assignment/tracking — part of the `sun-moon-java-platform` family
(alongside [`-order`](https://github.com/schware/sun-moon-java-platform-order)
and [`-kds`](https://github.com/schware/sun-moon-java-platform-kds), tied
together as git submodules under
[`sun-moon-java-platform`](https://github.com/schware/sun-moon-java-platform)).

Spring Boot, deployed as a WAR to the shared Jetty instance, same pattern
as `-order` (see that repo's `docs/adr/0004` and `0005` for the
Spring/Jetty/WAR deployment reasoning and a classpath bug worth knowing
about before touching this repo's dependencies).

## Why MongoDB

Deliveries map naturally onto documents, and courier-assignment logic can
later lean on MongoDB's geospatial indexes (`$near`) without a storage
migration. See the parent repo's ADR for the full per-service database
reasoning.

## API

- `POST /deliveries` — assign a delivery (`{"orderId": "..."}`) in
  `ASSIGNED` status.

## Build & run

Requires JDK 21+, and MongoDB reachable at `localhost:27017` (see
`src/main/resources/application.yml`).

```
./gradlew test
./gradlew bootWar
```

Deploy: copy `build/libs/delivery-0.1.0.war` to the Jetty `webapps/`
directory as `delivery.war`. Needs the same kind of external deployment
descriptor as `-order` (see [`Debian-Setting/docs/jetty.md`](https://github.com/schware/Debian-Setting/blob/master/docs/jetty.md)).

```
curl http://localhost:8080/delivery/actuator/health
curl -X POST http://localhost:8080/delivery/deliveries \
  -H "Content-Type: application/json" \
  -d '{"orderId":"order-1"}'
```

Swagger UI: `http://localhost:8080/delivery/swagger-ui/index.html`
