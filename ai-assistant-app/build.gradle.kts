plugins {
    id("org.springframework.boot")
}

dependencies {
    implementation(project(":ai-assistant-common"))
    implementation(project(":ai-assistant-core"))
    implementation(project(":ai-assistant-modules"))
    implementation("com.github.xiaoymin:knife4j-openapi3-jakarta-spring-boot-starter:4.5.0")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.flywaydb:flyway-mysql")
    runtimeOnly("com.mysql:mysql-connector-j")
}
