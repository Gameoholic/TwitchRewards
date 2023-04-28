plugins {
    java
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "com.github.gameoholic"
version = "1.6"

repositories {
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://oss.sonatype.org/content/groups/public/")
    mavenCentral()
}

java.toolchain.languageVersion.set(JavaLanguageVersion.of(17))

// not to be confused with shadow, which the plugin itself provides
val shade: Configuration by configurations.creating
configurations.implementation {
    extendsFrom(shade)
}

val excludedDependencies = listOf(
    "org.slf4j:slf4j-api",
    "org.apache.commons:commons-lang3",
    "commons-io:commons-io",
    "commons-lang:commons-lang",
    "commons-logging:commons-logging",
    "commons-configuration:commons-configuration",
    "org.jetbrains:annotations",
)

val relocations = listOf(
    "feign",
    "io",
    "kotlin",
    "okhttp3",
    "okio",
    "org.bstats",
    "org.HdrHistogram",
    "rx"
)

tasks {
    shadowJar {
        configurations = listOf(shade)
        archiveClassifier.set("")
        minimize()

        shade.dependencies.forEach { project.configurations.compileOnly.get().allDependencies }

        exclude("javax", "sampleapp.properties", "dependencies.txt")

        dependencies {
            excludedDependencies.forEach { exclude(dependency(it)) }
        }

        relocations.forEach { relocate(it, "com.github.gameoholic.twitchrewards.shade.$it") }

        relocate("com", "com.github.gameoholic.twitchrewards.shade.com") {
            exclude("com/github/gameoholic/twitchrewards/**")
        }
    }

    jar {
        archiveClassifier.set("skinny")
    }

    build {
        dependsOn(shadowJar)
    }
    processResources {
        expand("version" to version)
    }
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.19.4-R0.1-SNAPSHOT")
    shade("io.github.xanthic.cache:cache-provider-guava")
    shade("com.github.twitch4j:twitch4j:1.15.0")
    shade("org.bstats:bstats-bukkit:3.0.2")
}
