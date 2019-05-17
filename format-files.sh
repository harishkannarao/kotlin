#!/bin/sh

# Make the script to abort if any command fails
set -e

# Print the commands as it is executed. Useful for debugging
set -x

# format all javascript files
find ktor/src -name '*.js' ! -name '*.min.js' -type f | xargs -n1 sh -c 'js-beautify --quiet --preserve-newlines --type js --replace --file $1' sh

# format all css files
find ktor/src -name '*.css' ! -name '*.min.css' -type f | xargs -n1 sh -c 'js-beautify --quiet --preserve-newlines --type css --replace --file $1' sh

# format all html files
find ktor/src -name '*.html' -type f | xargs -n1 sh -c 'js-beautify --quiet --preserve-newlines --type html --replace --file $1' sh

# format all ftl files
find ktor/src -name '*.ftl' -type f | xargs -n1 sh -c 'js-beautify --quiet --preserve-newlines --type html --replace --file $1' sh

