plugins {
    id("java")
}

group = "com.moveit"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.apache.logging.log4j:log4j-api:2.20.0")
    implementation("org.apache.logging.log4j:log4j-core:2.20.0")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
    testImplementation("org.mockito:mockito-core:3.+")
    testImplementation("org.mockito:mockito-junit-jupiter:2.23.4")
    implementation("mysql:mysql-connector-java:8.0.17")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}