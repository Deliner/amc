import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    application
    kotlin("jvm") version "1.3.72"
    id("com.github.johnrengelman.shadow") version "5.0.0"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    jcenter()
    maven { url = uri("https://dl.bintray.com/kotlin/ktor") }
    maven { url = uri("https://dl.bintray.com/kotlin/kotlin-eap") }
}

val ktorVersion = "1.3.1"


sourceSets{
    main {
        java.srcDir("src/")
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
}

application {
    mainClassName = "com.chessking.MainKt"
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("io.ktor:ktor-server-netty:$ktorVersion")
    implementation("ch.qos.logback:logback-classic:1.2.3")
    implementation(group = "junit", name = "junit", version = "4.12")
}

//tasks.withType<KotlinCompile>().all {
//    kotlinOptions.jvmTarget = "1.8"
//}
//
//tasks {
//    compileKotlin {
//        kotlinOptions.jvmTarget = "1.8"
//    }
//}

tasks.withType<Jar> {
    manifest {
        attributes(
            mapOf(
                "Main-Class" to application.mainClassName
            )
        )
    }
}
