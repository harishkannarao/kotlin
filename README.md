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
    
## To run ktor application

#### Using gradle

    ./gradlew :ktor:run
    
#### Using Java

    ./gradlew clean assemble
    
    java -jar ktor/build/libs/ktor-exec.jar