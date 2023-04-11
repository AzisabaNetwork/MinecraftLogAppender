import org.apache.tools.ant.filters.ReplaceTokens

plugins {
    java
}

group = "net.azisaba"
version = "1.0.0-SNAPSHOT"

java.toolchain.languageVersion.set(JavaLanguageVersion.of(8))

repositories {
    mavenCentral()
    maven { url = uri("https://oss.sonatype.org/content/repositories/snapshots/") }
    maven { url = uri("https://hub.spigotmc.org/nexus/content/repositories/snapshots/") }
    maven { url = uri("https://repo.papermc.io/repository/maven-public/") }
}

dependencies {
    // Velocity
    compileOnly("com.velocitypowered:velocity-api:3.1.1")
    annotationProcessor("com.velocitypowered:velocity-api:3.1.1")
    // Spigot
    compileOnly("org.spigotmc:spigot-api:1.12.2-R0.1-SNAPSHOT")
    // APIs
    compileOnly("org.jetbrains:annotations:23.0.0")
    compileOnly("org.apache.logging.log4j:log4j-api:2.19.0")
    compileOnly("org.apache.logging.log4j:log4j-core:2.19.0")
}

tasks {
    compileJava {
        options.encoding = "UTF-8"
    }

    processResources {
        filesMatching("plugin.yml") {
            filter(ReplaceTokens::class, mapOf("tokens" to mapOf("version" to project.version)))
        }
    }
}
