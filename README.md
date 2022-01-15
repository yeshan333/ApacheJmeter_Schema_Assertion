# ApacheJmeter Schema Assertion

a ApacheJmeter assertion plugin to validate JSON/YAML Schema.

## Features

- Get JSON/YAML Schema from textarea or filepath
- Use [JsonPath](https://github.com/json-path/JsonPath) expression to extract the schema object
    
## Quick Usage

> [Java JDK](https://adoptopenjdk.net/) is required.

```shell
git clone git@github.com:yeshan333/ApacheJmeter_Schema_Assertion.git

cd ApacheJmeter_Schema_Assertion/examples

chmod +x jmeter_installer.sh

# install ApacheJmeter
./jmeter_installer.sh

./apache-jmeter/bin/jmeter -t Schema-Assertion.jmx
```

## Running Tests

> Maven & JDK 1.8 is required.

To run tests, run the following command:

```bash
mvn clean test
```

## License

Distributed under the MIT License. See LICENSE for more information.

## Acknowledgements

Thanks for these awesome resources that were used during the development of the **ApacheJmeter Schema Assertion**:

- [Open API](https://spec.openapis.org/oas/latest.html#info-object-example)
- [JSON Schema](https://json-schema.org/specification.html)
- [YAML Schema](https://asdf-standard.readthedocs.io/en/1.5.0/schemas/yaml_schema.html#Schema%20Definitions)
- [https://github.com/networknt/json-schema-validator](https://github.com/networknt/json-schema-validator)
- [https://github.com/json-path/JsonPath](https://github.com/json-path/JsonPath)

