# AGENTS.md — ApacheJmeter Schema Assertion

Agent instructions for coding in this repository.

## Project Overview

Apache JMeter plugin (assertion component) that validates sampler responses against JSON/YAML Schema.
Java 8 + Maven. Package namespace: `io.github.yeshan333.*`

## Build / Test Commands

```bash
# Full test suite
mvn clean test

# Single test class
mvn test -Dtest=SchemaAssertionTests

# Single test method
mvn test -Dtest=SchemaAssertionTests#testSetJsonPathExpr

# Build plugin JAR (output → examples/)
mvn clean package -DskipTests

# Run JMeter integration test (after install)
./apache-jmeter-5.6.3/bin/jmeter -t examples/Schema_Assertion_5.5.jmx -n -l result.jtl
```

## Directory Structure

```
src/main/java/io/github/yeshan333/
├── common/
│   ├── JsonSchemaValidator.java      # JSON parsing & schema loading
│   └── YamlSchemaValidator.java      # YAML parsing (extends JsonSchemaValidator)
└── jmeter/assertions/
    ├── SchemaAssertion.java           # Core assertion logic (implements Assertion)
    └── gui/SchemaAssertionGui.java    # JMeter GUI component
src/test/java/                         # Mirror of src/main structure
src/test/resources/schema/             # Test schema fixtures (.json, .yml)
examples/                              # JMX test plans + installer script
```

## Code Style

### Formatting
- 4-space indentation (no tabs)
- Max line length: 120 characters
- Braces: K&R style (opening brace on same line)
- File encoding: UTF-8

### Imports
- Group order: `com.*` → `org.*` → `java.*` → `javax.*` → `io.github.*`
- No wildcard imports except `com.networknt.schema.*` in test files
- One blank line between import groups

### Naming Conventions
- Classes: `PascalCase` (e.g., `SchemaAssertion`, `JsonSchemaValidator`)
- Methods/fields: `camelCase` (e.g., `getJsonSchemaFilePath`)
- Constants: `UPPER_SNAKE_CASE` (e.g., `SCHEMA_FILEPATH_KEY`)
- Test methods: `testDescriptiveName` with `@DisplayName("human readable")`

### Types & Generics
- Use explicit types; avoid raw types
- `JsonNode`, `JsonSchema`, `Set<ValidationMessage>` from networknt/Jackson
- Serializable classes must declare `serialVersionUID`

### Error Handling
- Use `try-catch` with specific exception types (FileNotFoundException, IOException, PathNotFoundException)
- Log warnings via `slf4j` (`LoggerFactory.getLogger`) before setting assertion failures
- Use `AssertionResult.setFailure(true)` for schema mismatches
- Use `AssertionResult.setError(true)` for I/O / config errors
- Never swallow exceptions — always log

### Testing (JUnit 5)
- Use `@Test` + `@DisplayName` on every test
- Use `org.junit.jupiter.api.Assertions` (not `Assert`)
- Test fixtures in `src/test/resources/`
- Project base path: `Paths.get("").toAbsolutePath()`

## Architecture Guidelines

- **File size**: Java files ≤ 250 lines. Split if exceeded.
- **Folder size**: ≤ 8 files per directory. Use subdirectories.
- SchemaAssertion is the entry point — delegates to `JsonSchemaValidator` / `YamlSchemaValidator`
- Schema detection: content starting with `{` → JSON, otherwise → YAML
- JsonPath expression (`$.response`) extracts the schema node from a wrapper document
- All schema validation uses Draft V4 (`SpecVersion.VersionFlag.V4`)

## Key Dependencies

| Library | Version | Purpose |
|---------|---------|---------|
| Apache JMeter | 5.5 | Assertion API & GUI |
| networknt json-schema-validator | 1.0.64 | JSON Schema validation |
| Jackson (core/databind/yaml) | 2.12.x | JSON/YAML parsing |
| JsonPath (jayway) | 2.9.0 | JSONPath expressions |
| Apache Tika | 1.28.5 | MIME type detection |

## CI Workflows

| Workflow | Trigger | Purpose |
|----------|---------|---------|
| `run-tests.yaml` | push/PR | Maven unit tests |
| `jacoco-unit-test.yaml` | push/main | Coverage + badge |
| `integration-tests.yaml` | push/PR | JMeter end-to-end test |
