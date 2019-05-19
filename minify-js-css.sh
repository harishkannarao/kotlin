#!/bin/sh

# Make the script to abort if any command fails
set -e

# Print the commands as it is executed. Useful for debugging
set -x

mkdir -p minifier_cache

# Delete previously downloaded files if present
find minifier_cache -name 'closure-compiler.jar' -type f -print0 | xargs -0 -I {} rm {}
find minifier_cache -name 'closure-compiler-*' -type d -print0 | xargs -0 -I {} rm -rf {}
find minifier_cache -name 'closure-stylesheets.jar' -type f -print0 | xargs -0 -I {} rm {}

# Download closure compiler
CLOSURE_COMPILER_VERSION=20190415
CLOSURE_STYLESHEETS_VERSION=1.5.0

if [[ ! -f minifier_cache/compiler-$CLOSURE_COMPILER_VERSION.tar.gz ]]
then
    wget "https://dl.google.com/closure-compiler/compiler-$CLOSURE_COMPILER_VERSION.tar.gz" -P minifier_cache
fi

if [[ ! -f minifier_cache/closure-stylesheets-$CLOSURE_STYLESHEETS_VERSION.jar ]]
then
    wget "https://github.com/google/closure-stylesheets/releases/download/v$CLOSURE_STYLESHEETS_VERSION/closure-stylesheets.jar" -O minifier_cache/closure-stylesheets-$CLOSURE_STYLESHEETS_VERSION.jar
fi

mkdir -p minifier_cache/closure-compiler-$CLOSURE_COMPILER_VERSION

tar -xvzf minifier_cache/compiler-$CLOSURE_COMPILER_VERSION.tar.gz -C minifier_cache/closure-compiler-$CLOSURE_COMPILER_VERSION

cp minifier_cache/closure-compiler-$CLOSURE_COMPILER_VERSION/closure-compiler-v$CLOSURE_COMPILER_VERSION.jar minifier_cache/closure-compiler.jar

cp minifier_cache/closure-stylesheets-$CLOSURE_STYLESHEETS_VERSION.jar minifier_cache/closure-stylesheets.jar

# minify all javascript files

find ktor/src -name '*.min.js' -type f -print0 | xargs -0 -I {} rm {}

find ktor/src -name '*.js' ! -name '*.min.js' -type f | sed -nE 's/.js$//p' | xargs -n1 sh -c 'java -jar minifier_cache/closure-compiler.jar --js_output_file=$1.min.js $1.js' sh

# minify all css files

find ktor/src -name '*.min.css' -type f -print0 | xargs -0 -I {} rm {}

find ktor/src -name '*.css' ! -name '*.min.css' -type f | sed -nE 's/.css$//p' | xargs -n1 sh -c 'java -jar minifier_cache/closure-stylesheets.jar --output-file $1.min.css $1.css' sh