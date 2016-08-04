#!/bin/bash

for PROTO in $(ls *.proto)
do
  echo "Building for $PROTO"
  BASE=${PROTO%.*}
  protoc --encode=Person $BASE.proto < $BASE.txt > $BASE.bin
done
