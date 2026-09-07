plugins {
    java
    war
    id("org.springframework.boot") version "3.3.4"
    id("io.spring.dependency-management") version "1.1.6"
}

group = "com.sunmoon.delivery"
version = "0.1.0"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

repositories {
    mavenCentral()
}

// Delivery service — sun-moon-java-platform family.
// Originally planned as MongoDB (deliveries map naturally to documents,
// and courier-assignment could later lean on geospatial $near queries).
// Switched to Postgres + JSONB instead: MongoDB 5.0+ requires AVX, and
// this homelab box's CPU (Core i5 M 480, 2010) doesn't have it — not
// fixable by config, the server binary won't run at all. JSONB gives the
// same "whole object as one blob" access pattern on hardware that
// actually works here. See docs/adr in the parent sun-moon-java-platform
// repo for the full story.
dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web") {
        exclude(group = "org.springframework.boot", module = "spring-boot-starter-tomcat")
    }
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-jdbc")
    runtimeOnly("org.postgresql:postgresql")

    implementation("io.micrometer:micrometer-registry-prometheus")

    // See sun-moon-java-platform-order's build.gradle.kts / ADR 0005 for
    // why this exact version is pinned and why slf4j-api needs the
    // providedRuntime exclude below — same WAR/Jetty deployment, same bug.
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.6.0")
    implementation("org.slf4j:slf4j-api:2.0.16")

    providedRuntime("org.springframework.boot:spring-boot-starter-jetty") {
        exclude(group = "org.slf4j", module = "slf4j-api")
    }

    // sun-moon-java-platform-order compiles fine without this because
    // spring-boot-starter-websocket happens to pull jakarta.servlet-api
    // in transitively as a normal compile dependency. Without a websocket
    // starter, DeliveryApplication extending SpringBootServletInitializer
    // fails to compile (jakarta.servlet.ServletException unresolvable) —
    // providedCompile (from the `war` plugin) fixes it directly instead
    // of relying on that accident.
    providedCompile("jakarta.servlet:jakarta.servlet-api")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.test {
    useJUnitPlatform()
}
