# Ztl Case Assignment
This repo contains a proposed solution for the case assignment.

### Build
The project can be built with: 
```bash
gradlew build
```
___
### Test
Test coverage should be fair, though I believe it can be improved by adding integration tests. Running tests can be achieved with:
```bash
gradlew clean test
```
___
### Run
The application runs by default on port `http://localhost:8080`. To run the application simply use the following command:
```bash
gradlew run
```
___
### Endpoints
The application consists of two endpoints:

#### Countries (`/countries`)
Request parameters:
- `sortBy`: sorts content according to value (optional). Valid values:
  - `name` _(default)_
  - `currency`
- `sortOrder`: assigns the order direction (optional). Valid values: 
  - `asc` _(default)_
  - `desc`
- `max`: limits results (optional, _defaults to `250`_). 

The response contains a full list of countries. Each with the following attributes:
```
- name (String)
- currency (String)
- maxWithdrawal (BigInt) 
- requiredDocumentation (List of Document)
  - Document:
    - id (Int)
    - code (String)
    - description (String) 
    - required (Bool)
```

_Note: In order to keep data integrity, countries that don't have the above attributes are filtered out on requests._

#### Currencies (`/currencies`)
Request parameters:
- `sortOrder`: assigns the order direction (optional). Valid values:
  - `asc` _(default)_
  - `desc`
- `max`: limits results (optional, _defaults to `250`_).

The response contains a full list of countries. Each with the following attributes:
```
- name (String)
- countries (List of String)
```
___
### TODO 
- Use environment variables.
- Cache values to reduce response times (currently anywhere between 100-1200ms).
