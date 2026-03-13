plugins {
    java
    id("org.springframework.boot") version "3.5.11" apply false
}

val springBootVersion = "3.5.11"
val springAiVersion = "1.0.0"

allprojects {
    group = "com.aiassistant"
    version = "1.0.0-SNAPSHOT"

    repositories {
        mavenCentral()
        maven { url = uri("https://repo.spring.io/milestone") }
    }
}

subprojects {
    apply(plugin = "java-library")

    java {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    dependencies {
        "api"(platform("org.springframework.boot:spring-boot-dependencies:$springBootVersion"))
        "api"(platform("org.springframework.ai:spring-ai-bom:$springAiVersion"))
        "annotationProcessor"(platform("org.springframework.boot:spring-boot-dependencies:$springBootVersion"))
        "compileOnly"("org.projectlombok:lombok")
        "annotationProcessor"("org.projectlombok:lombok")
        "testImplementation"("org.springframework.boot:spring-boot-starter-test")
    }

    tasks.withType<JavaCompile> {
        options.encoding = "UTF-8"
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }
}
