#!/bin/bash

./gradlew javadoc || exit 1
cp -r app/build/docs/javadoc/* /javadoc-out/ || exit 1
