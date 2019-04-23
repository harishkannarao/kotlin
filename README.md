# Kotlin

This repository is to play with kotlin programming language and related frameworks or libraries

## Travis CI Build status
[![Build Status](https://travis-ci.org/harishkannarao/kotlin.svg?branch=master)](https://travis-ci.org/harishkannarao/kotlin)

## Required Softwares, Tools and Version
* Java JDK Version: 11 Adopt OpenJDK (`java -version`)
* Git Client: Any latest version (`git --version`)
* Integrated Development Environment: Any version of IntelliJ Idea or Eclipse

## To build

    ./gradlew clean build
    
To print the http request and response during integration test execution

    ./gradlew clean build --info
    

## To run ktor application

#### Using gradle

    ./gradlew --parallel --quiet :ktor:run :wiremock-runner:run
    
Change logback configuration

    ./gradlew --parallel --quiet :ktor:run :wiremock-runner:run -Dlogback.configurationFile=logback-cloud.xml
    
#### Using Java

    ./gradlew clean assemble
    
    java -jar wiremock-runner/build/libs/wiremock-runner-exec.jar
    
    java -jar ktor/build/libs/ktor-exec.jar

Change logback configuration

    java -Dlogback.configurationFile=logback-cloud.xml -jar ktor/build/libs/ktor-exec.jar    
    
## Test filtering

#### Specific test class(es)

    ./gradlew clean :ktor-integration-test:test --tests "com.harishkannarao.ktor.api.BasicAuthIntegrationTest" --tests "com.harishkannarao.ktor.api.CustomerIntegrationTest"

#### Specific test method(s)

    ./gradlew clean :ktor-integration-test:test --tests "com.harishkannarao.ktor.api.BasicAuthIntegrationTest.returns message when authenticated"

#### Specific package (recursively)

    ./gradlew clean :ktor-integration-test:test --tests "com.harishkannarao.ktor.api*"

#### Include and Exclude Groups

    ./gradlew clean :ktor-integration-test:test -DtestNgIncludeGroups=API_INTEGRATION_TEST,WEB_INTEGRATION_TEST -DtestNgExcludeGroups=AUTH_API_INTEGRATION_TEST
    
#### Non Auth Integration tests

    ./gradlew clean :ktor-integration-test:nonAuthIntegrationTest