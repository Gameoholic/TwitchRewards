plugins {
    java
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "com.github.gameoholic"
version = "1.5"

repositories {
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://oss.sonatype.org/content/groups/public/")
}

java.toolchain.languageVersion.set(JavaLanguageVersion.of(17))

// not to be confused with shadow, which the plugin itself provides
val shade: Configuration by configurations.creating
configurations.implementation {
    extendsFrom(shade)
}

tasks {
    shadowJar {
        configurations = listOf(shade)
        archiveClassifier.set("")
        isEnableRelocation = true
        relocationPrefix = "com.github.gameoholic.twitchrewards.shade"
        minimize()
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
    shade("com.github.twitch4j:twitch4j:1.15.0")
    shade("org.bstats:bstats-bukkit:3.0.2")
}
