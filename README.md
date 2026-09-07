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

## Why Postgres + JSONB, not MongoDB

Originally planned as MongoDB (deliveries map naturally onto documents,
and courier-assignment could later lean on geospatial `$near` queries).
**Switched to Postgres + JSONB**: MongoDB 5.0+ requires AVX, and this
homelab box's CPU (Core i5 M 480, 2010) doesn't have it — `mongod` won't
even start (`SIGILL`), and it turned out MongoDB no longer ships server
packages for the last pre-AVX version (4.4, EOL since Feb 2024) either.
JSONB gives the same "whole object as one blob, `order_id` indexed for
lookups" access pattern a document store would, on hardware that actually
runs it — see `JdbcDeliveryRepository` and `schema.sql`. See the parent
repo's ADR for the full per-service database reasoning and this
hardware-driven pivot.

## API

- `POST /deliveries` — assign a delivery (`{"orderId": "..."}`) in
  `ASSIGNED` status.

## Build & run

Requires JDK 21+, and PostgreSQL reachable at `localhost:5432` with a
`delivery_service` database and `sunmoon` role (see
`src/main/resources/application.yml`). `schema.sql` runs automatically on
startup (`spring.sql.init.mode: always`).

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
