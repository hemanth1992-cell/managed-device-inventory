#!/usr/bin/env sh
set -eu
DIR=$(CDPATH= cd -- "$(dirname -- "$0")" && pwd)
exec "$DIR/gradle/wrapper/gradlew" "$@"
