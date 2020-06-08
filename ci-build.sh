#!/bin/sh

# Make the script to abort if any command fails
set -e

# Print the commands as it is executed. Useful for debugging
set -x

./gradlew pullDockerDependencies
./gradlew clean
./gradlew startDockerDependencies
./gradlew minifyFiles
# Wait for some time before the minification process completes
sleep 60
find ./ktor/src -name "*.min.js"
./gradlew assemble
./gradlew build -DchromeDriverBinary=/usr/lib/chromium-browser/chromedriver -DchromeBinary=/usr/bin/chromium-browser -DchromeHeadless=true -Dapp.enable.call.trace=false
./gradlew build -Dapp.development.mode=false -Dapp.enable.call.trace=false -DchromeDriverBinary=/usr/lib/chromium-browser/chromedriver -DchromeBinary=/usr/bin/chromium-browser -DchromeHeadless=true
./gradlew :ktor:generateLicenseReport