#!/bin/bash

# Make the script to abort if any command fails
set -e

# Print the commands as it is executed. Useful for debugging
set -x

mkdir -p minifier_cache

# Delete previously downloaded files if present
# find minifier_cache -name 'compiler-*.tar.gz' -type f -print0 | xargs -0 -I {} rm {}
find minifier_cache -name 'closure-compiler.jar' -type f -print0 | xargs -0 -I {} rm {}
find minifier_cache -name 'closure-compiler-*' -type d -print0 | xargs -0 -I {} rm -rf {}

# Download closure compiler
CLOSURE_COMPILER_VERSION=20190415

if [[ ! -f minifier_cache/compiler-$CLOSURE_COMPILER_VERSION.tar.gz ]]
then
    wget "https://dl.google.com/closure-compiler/compiler-$CLOSURE_COMPILER_VERSION.tar.gz" -P minifier_cache
fi

mkdir -p minifier_cache/closure-compiler-$CLOSURE_COMPILER_VERSION

tar -xvzf minifier_cache/compiler-$CLOSURE_COMPILER_VERSION.tar.gz -C minifier_cache/closure-compiler-$CLOSURE_COMPILER_VERSION

cp minifier_cache/closure-compiler-$CLOSURE_COMPILER_VERSION/closure-compiler-v$CLOSURE_COMPILER_VERSION.jar minifier_cache/closure-compiler.jar

# minify all javascript files

find ktor/src -name '*.min.js' -type f -print0 | xargs -0 -I {} rm {}

find ktor/src -name '*.js' ! -name '*.min.js' -type f | sed -nE 's/.js$//p' | xargs -n1 sh -c 'java -jar minifier_cache/closure-compiler.jar --js_output_file=$1.min.js $1.js' sh