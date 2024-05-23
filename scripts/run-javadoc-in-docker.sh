#!/bin/bash
mkdir javadoc
docker run --rm -v `pwd`/javadoc:/javadoc-out  citest scripts/javadoc.sh
