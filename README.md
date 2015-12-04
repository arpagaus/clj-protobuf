# Protocol Buffers for Clojure
[![Build Status](https://travis-ci.org/arpagaus/clj-protobuf.svg?branch=master)](https://travis-ci.org/arpagaus/clj-protobuf)

**Disclaimer**: This project is a work in progress. Please use the issue tracker to report any questions, enhancements or issues you encounter.

This library aims to provide a pure Clojure implementation for reading and writing Google's binary [Protocol Buffer](https://developers.google.com/protocol-buffers) format. Currently there exists already a library [clojure-protobuf](https://github.com/ninjudd/clojure-protobuf) for this which is based on generated Java classes. Please use that library for now if you need Protocol Buffer support in Clojure.

## Usage

FIXME: write

## See Also
- Protocul Buffers
  - Optional Fields And Default Values: https://developers.google.com/protocol-buffers/docs/proto#optional
- Instaparse
  - GitHub repository: https://github.com/Engelberg/instaparse
  - Introduction by Mark Engelberg: https://www.youtube.com/watch?v=b2AUW6psVcE

## Background

We are a small group of individuals, coming together once a month at the [Hackergarten in Lucerne, Switzerland](http://www.meetup.com/hackergarten-luzern).

We choose to implement a clojure library for the [Google Protocol Buffer](https://developers.google.com/protocol-buffers). There is already an existing clojure [library](https://github.com/ninjudd/clojure-protobuf). Despite this, we are working on our own version for the following reasons:

- So far no project member ever did something in Clojure. For us, it is an easy enough project to learn Clojure. Therefore don't expect anything from our solution.
- The existing implementation seems no to be Clojure-like enough. Calling a tool first, for the conversion of the *.proto files, written in C, doesn't feel clojure-eseque enough. This feeling may be inadequate, but are based on the following thoughts:
  - Using Protocol Buffer in the REPL should be as easy as possible.
  - Creating a dynamic webpage which allows to play around with Protocol Buffers, whould be easy either.

## Interface

We defined an interfaced which consits of several functions and how a schema looks.

### Functions

Read/write a protobuf byte stream, based on an intermeidate representation of the schema:

```
protobuf-load
protobuf-dump
```

For debugging purpose. Read/write a protobuf byte stream to a string. Use the same format as the protoc tool from Google.

```
protobuf-dump-string
protobuf-load-string
```

Read/write *.proto files and convert them into the intermediate schema:

```
protobuf-schema-load
protobuf-schema-dump
```

### Schema

TODO (see examples and tests)

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
