# Protocol Buffers for Clojure
[![Build Status](https://travis-ci.org/arpagaus/clj-protobuf.svg?branch=master)](https://travis-ci.org/arpagaus/clj-protobuf)

FIXME: write description

## Usage

FIXME: write

## See Also
- Protocul Buffers
  - Optional Fields And Default Values: https://developers.google.com/protocol-buffers/docs/proto#optional
- Instaparse
  - GitHub repository: https://github.com/Engelberg/instaparse
  - Introduction by Mark Engelberg: https://www.youtube.com/watch?v=b2AUW6psVcE

## Background
We are a small group of people, more or less regularly meeting each other at the [Hackergarten](http://hackergarten.net) in [Lucerne](http://www.meetup.com/hackergarten-luzern).

We choose to implement a clojure library for the [Google Protocol Buffer](https://developers.google.com/protocol-buffers). There is already an existing clojure [library](https://github.com/ninjudd/clojure-protobuf). Despite this, we are working on our own version for the following reasons:

- So far no project member ever did something in Clojure. For us, it is an easy enough project to learn Clojure. Therefore don't expect anything from our solution.
- The existing implementation seems no to be Clojure-like enough. Calling a tool first, for the conversion of the *.proto files, written in C, doesn't feel clojure-eseque enough. This feeling may be inadequate, but are based on the following thoughts:
  - Using Protocol Buffer in the REPL should be as easy as possible.
  - Creating a dynamic webpage which allows to play around with Protocol Buffers, whould be easy either.

### protoc Command Line examples

Convert text reperesentation to binary representation:
```
protoc.exe simple.proto --encode=Person < simple.txt
```
Convert text reperesentation to binary representation:
```
protoc.exe simple.proto --decode=Person < simple.bin
```

## License

Copyright (C) 2015 Remo Arpagaus

Distributed under the Eclipse Public License, the same as Clojure.
