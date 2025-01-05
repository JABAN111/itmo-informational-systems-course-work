plugins {
    id("org.springframework.boot") version "3.0.6"
    id("io.spring.dependency-management") version "1.1.3"
    kotlin("jvm") version "2.1.0"
    kotlin("plugin.spring") version "1.8.10"
    kotlin("plugin.allopen") version "2.1.0"
//    kotlin("plugin.lombok") version "2.1.0"
//    id("io.freefair.lombok") version "8.10"
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
    implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    testImplementation("org.springframework.security:spring-security-test")
    runtimeOnly("org.postgresql:postgresql")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
allOpen {
    annotation("jakarta.persistence.Entity")
    // annotations("com.another.Annotation", "com.third.Annotation")
}