#!/usr/bin/env bash
set -euo pipefail
cd "$(dirname "$0")"
mkdir -p build/classes build/test-classes
find src/main/java -name '*.java' | sort > build/main-sources.txt
javac --release 17 -encoding UTF-8 -d build/classes @build/main-sources.txt
find src/test/java -name '*.java' | sort > build/test-sources.txt
javac --release 17 -encoding UTF-8 -cp build/classes -d build/test-classes @build/test-sources.txt
java -Djava.awt.headless=true -cp build/classes:build/test-classes reservas.Pruebas
jar --create --file build/sistema-reservas.jar --main-class reservas.Main -C build/classes .
echo 'JAR generado: build/sistema-reservas.jar'
