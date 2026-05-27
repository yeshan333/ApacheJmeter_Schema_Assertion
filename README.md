# ApacheJmeter Schema Assertion

[![Maven Tests CI](https://github.com/yeshan333/ApacheJmeter_Schema_Assertion/actions/workflows/run-tests.yaml/badge.svg)](https://github.com/yeshan333/ApacheJmeter_Schema_Assertion/actions/workflows/run-tests.yaml) [![codecov](https://codecov.io/gh/yeshan333/ApacheJmeter_Schema_Assertion/branch/main/graph/badge.svg?token=WN0DZ6QQ9H)](https://codecov.io/gh/yeshan333/ApacheJmeter_Schema_Assertion)

<!-- ![Code Coverage](.github/badges/jacoco.svg) -->

a ApacheJmeter assertion plugin to validate JSON/YAML Schema (Based on Sampler response).

## Features

- Get JSON/YAML Schema from textarea or filepath
- Use [JsonPath](https://github.com/json-path/JsonPath) expression to extract the schema object

## Installation

Download the latest plugin JAR from [GitHub Releases](https://github.com/yeshan333/ApacheJmeter_Schema_Assertion/releases) and copy it to `<JMETER_HOME>/lib/ext/`.

## Quick Usage

> [Java JDK 11+](https://adoptium.net/) is required.

```shell
git clone git@github.com:yeshan333/ApacheJmeter_Schema_Assertion.git

cd ApacheJmeter_Schema_Assertion/examples

chmod +x jmeter_installer.sh

# install ApacheJmeter and the plugin
./jmeter_installer.sh

./apache-jmeter-5.6.3/bin/jmeter -t Schema_Assertion_5.5.jmx
```

![Assertion Settings](https://cdn.jsdelivr.net/gh/yeshan333/jsDelivrCDN@main/20220115114549.jpg)

![Assertion Results](https://cdn.jsdelivr.net/gh/yeshan333/jsDelivrCDN@main/20220115114624.jpg)

## Running Tests

> Maven & JDK 11+ is required.

To run tests, run the following command:

```bash
mvn clean test
```

## License

Distributed under the MIT License. See [LICENSE](./LICENSE) for more information.

## Acknowledgements

Thanks for these awesome resources that were used during the development of the **ApacheJmeter Schema Assertion**:

- [Open API](https://spec.openapis.org/oas/latest.html#info-object-example)
- [JSON Schema](https://json-schema.org/specification.html)
- [YAML Schema](https://asdf-standard.readthedocs.io/en/1.5.0/schemas/yaml_schema.html#Schema%20Definitions)
- [https://github.com/networknt/json-schema-validator](https://github.com/networknt/json-schema-validator)
- [https://github.com/json-path/JsonPath](https://github.com/json-path/JsonPath)
