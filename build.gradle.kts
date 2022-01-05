import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.10"
    id("io.papermc.paperweight.userdev") version "1.2.0"
    id("com.github.johnrengelman.shadow") version "7.1.0"
}

val javaVersion = "16"
val mcVersion = "1.17.1"

group = "de.hglabor"
version = "${mcVersion}_v1"

java.targetCompatibility = JavaVersion.valueOf("VERSION_${javaVersion.replace(".", "_")}")
java.sourceCompatibility = JavaVersion.VERSION_16

repositories {
    mavenCentral()
    mavenLocal()
    maven("https://papermc.io/repo/repository/maven-public/")
    maven("https://maven.enginehub.org/repo/")
}

dependencies {
    implementation(kotlin("stdlib"))
    paperDevBundle("$mcVersion-R0.1-SNAPSHOT")
    compileOnly("io.papermc.paper:paper-api:$mcVersion-R0.1-SNAPSHOT")
    implementation("de.hglabor:kit-api:1.17.1_LOCAL") //version can be outdated and should not be changed because the outdated jar gets replaced by the new compiled jar
    compileOnly("de.hglabor:localization:0.0.7") //i think its required by the kit api or something
    compileOnly("de.hglabor:hglabor-utils:${mcVersion}_v1")
    compileOnly("com.sk89q.worldedit:worldedit-bukkit:7.2.8")
}

tasks {
    build {
        dependsOn(reobfJar)
    }
    withType<JavaCompile> {
        options.encoding = "UTF-8"
        val version = if (javaVersion.contains(".")) {
            javaVersion.split(".")[1].toInt()
        } else {
            javaVersion.toInt()
        }
        options.release.set(version)
    }
    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = javaVersion
    }
}