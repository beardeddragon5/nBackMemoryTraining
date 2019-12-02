#!/bin/bash

for dir in `find . -type d | grep -v ./out | grep -vE '^\.$'`
do
  mkdir -p out/$dir
done
