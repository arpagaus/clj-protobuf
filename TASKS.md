# Tasks

- [ ] Implement ```protobuf-dump```
  - [ ] Support Enums
  - [ ] Support nested messages
  - [ ] Support ```required```
  - [ ] Support ```oneof```
  - [ ] Support import of other packages
- [ ] Implement ```protobuf-load```
- [ ] Remove dependency to com.google.protobuf
- [ ] Support for ClojureScript

# Findings
## Type Scopes: Enums & messages
Scope analogous to Java inner classes. Only upstream is visible. Inner messages / enums cannot be used.
