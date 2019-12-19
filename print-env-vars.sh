#!/bin/sh

# Make the script to abort if any command fails
set -e

# Print the commands as it is executed. Useful for debugging
set -x

echo "custom variable: $CUSTOM_VARIABLE"
echo "custom encrypted variable: $ENCRYPTED_VARIABLE"
echo "default variable: $GITHUB_SHA"
