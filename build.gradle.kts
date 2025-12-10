plugins {
    id("org.springframework.boot") version "3.3.11"
    id("io.spring.dependency-management") version "1.1.3"
    kotlin("jvm") version "2.1.0"
    kotlin("plugin.spring") version "1.8.10"
    kotlin("plugin.allopen") version "2.1.0"
}

group = "lab.is.bank"
version = "0.0.1-SNAPSHOT"
description = "Course project for information systems"

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.google.api-client:google-api-client:2.7.1")
    implementation("com.opencsv:opencsv:5.7.1")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.18.2")
    implementation("org.apache.poi:poi-ooxml:5.4.0")
// https://mvnrepository.com/artifact/org.postgresql/postgresql
    implementation("org.postgresql:postgresql:42.7.7")
    implementation("com.itextpdf:itext7-core:7.2.5")
    implementation("org.bouncycastle:bcprov-jdk18on:1.79")
    implementation("org.bouncycastle:bcpkix-jdk18on:1.79")
    implementation("org.apache.pdfbox:pdfbox:2.0.30")
    implementation("com.google.zxing:core:3.5.3")
    implementation("com.google.zxing:javase:3.5.3")

    implementation("io.jsonwebtoken:jjwt-api:0.12.3")
    implementation("io.jsonwebtoken:jjwt-impl:0.12.3")
    implementation("io.jsonwebtoken:jjwt-jackson:0.12.3")

    implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    // Swagger/OpenAPI documentation
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.7.0")

    testImplementation("org.springframework.security:spring-security-test")
    runtimeOnly("org.postgresql:postgresql")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

allOpen {
    annotation("jakarta.persistence.Entity")
}