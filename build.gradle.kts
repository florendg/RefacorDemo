/*
 * This file was generated by the Gradle 'init' task.
 *
 * This generated file contains a sample Java Library project to get you started.
 * For more details take a look at the Java Libraries chapter in the Gradle
 * User Manual available at https://docs.gradle.org/6.5/userguide/java_library_plugin.html
 */

plugins {
    // Apply the java-library plugin to add support for Java Library
    `java-library`
}

repositories {
    // Use jcenter for resolving dependencies.
    // You can declare any Maven/Ivy/file repository here.
    mavenCentral()
}

dependencies {

    // This dependency is used internally, and not exposed to consumers on their own compile classpath.
    implementation("com.google.code.gson:gson:2.8.6")

    // Use JUnit Jupiter API for testing.
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.6.2")
}
java {
    version = JavaVersion.VERSION_14
}

tasks {
    withType<JavaCompile> {
        options.compilerArgs.add("--enable-preview")
    }



    test {
        jvmArgs("--enable-preview")
        useJUnitPlatform()
    }
}


