# Kotlin

This repository is to play with kotlin programming language and related frameworks or libraries

## Travis CI Build status
[![Build Status](https://travis-ci.org/harishkannarao/kotlin.svg?branch=master)](https://travis-ci.org/harishkannarao/kotlin)

## Required Softwares, Tools and Version
* Java JDK Version: 8 Oracle or OpenJDK (`java -version`)
* Git Client: Any latest version (`git --version`)
* Integrated Development Environment: Any version of IntelliJ Idea or Eclipse

## To build

    ./gradlew clean build
    
To print the http request and response during integration test execution

    ./gradlew clean build --info
    
## To run ktor application

#### Using gradle

    ./gradlew --parallel :ktor:run :wiremock-runner:run
    
#### Using Java

    ./gradlew clean assemble
    
    java -jar wiremock-runner/build/libs/wiremock-runner-exec.jar
    
    java -jar ktor/build/libs/ktor-exec.jar
    
Change logback configuration

    java -Dlogback.configurationFile=logback-cloud.xml -jar ktor/build/libs/ktor-exec.jar