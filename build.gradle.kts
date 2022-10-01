val kotlinVersion = "1.7.20"
val ktorVersion = "2.1.2"
val logbackVersion = "1.4.1"
val okhttpVersion = "4.10.0"
val jacksonVersion = "2.14.0-rc1"
val mockkVersion = "1.13.2"


plugins {
    application
    kotlin("jvm") version "1.7.20"
    id("com.adarshr.test-logger") version "3.2.0" // better test logs
}

group = "io.ztlpay"
version = "0.0.1"

application {
    mainClass.set("io.ztlpay.ApplicationKt")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-core:$ktorVersion")
    implementation("io.ktor:ktor-server-netty:$ktorVersion")
    implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
    implementation("io.ktor:ktor-serialization-jackson:$ktorVersion")
    implementation("ch.qos.logback:logback-classic:$logbackVersion")
    implementation("com.squareup.okhttp3:okhttp:$okhttpVersion")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:$jacksonVersion")

    testImplementation("io.ktor:ktor-server-test-host:$ktorVersion")
    testImplementation("io.mockk:mockk:${mockkVersion}")
    testImplementation("org.jetbrains.kotlin:kotlin-test:1.7.20")
}
