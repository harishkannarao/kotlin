# Kotlin

This repository is to play with kotlin programming language and related frameworks or libraries.

Please read my blog post about the features exercised in this repository

Blog: [Kotlin / Ktor / Jdbi Json / Api / Free Marker / VueJs](https://blogs.harishkannarao.com/2019/06/kotlin-ktor-jdbi-json-api-free-marker.html)

## Travis CI Build status
[![Build Status](https://travis-ci.org/harishkannarao/kotlin.svg?branch=master)](https://travis-ci.org/harishkannarao/kotlin)

## Required Softwares, Tools and Version
* Java JDK Version: 11 Adopt OpenJDK (`java -version`)
* Docker Version: Docker version 18.09.2, build 6247962 (`docker --version`)
* Docker Compose Version: docker-compose version 1.23.2, build 1110ad01 (`docker-compose --version`)
* Chrome (Windows & Mac OS) Browser / Chromium (Linux OS) Browser: 62
* chromedriver: 2.32 [chromedriver installation steps](https://blogs.harishkannarao.com/2018/01/installing-chromedriver-for-selenium.html)
* Git Client: Any latest version (`git --version`)
* Integrated Development Environment: Any version of IntelliJ Idea or Eclipse

## Docker dependencies
Docker dependencies needs to be started using docker-compose before the build

#### Pull the latest images of docker services

    ./gradlew pullDocker
    
#### Start docker services

    ./gradlew startDocker
    
#### Stop docker services

    ./gradlew stopDocker
  
  
## To build

    ./gradlew stopDocker pullDocker clean minifyFiles startDocker build
    
To run web integration tests in head less mode

    ./gradlew stopDocker pullDocker clean startDocker build -DchromeHeadless=true
    
To print the http request and response during integration test execution

    ./gradlew stopDocker pullDocker clean startDocker build --info
    

## To run ktor application

#### Using gradle

    ./gradlew --parallel --quiet :ktor:run :wiremock-runner:run -Dapp.development.mode=true
    
Change logback configuration

    ./gradlew --parallel --quiet :ktor:run :wiremock-runner:run -Dapp.development.mode=true -Dlogback.configurationFile=logback-cloud.xml
    
#### Using Java

    ./gradlew clean minifyFiles assemble
    
    java -jar wiremock-runner/build/libs/wiremock-runner-exec.jar
    
    java -jar ktor/build/libs/ktor-exec.jar

Change logback configuration

    java -Dlogback.configurationFile=logback-cloud.xml -jar ktor/build/libs/ktor-exec.jar   
    
## Formatting js, css, html and ftl files

    ./gradlew formatFiles
    
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
    
### Just a sample