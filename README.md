# Kotlin

This repository is to play with kotlin programming language and related frameworks or libraries

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