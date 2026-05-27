# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project

Apache JMeter assertion plugin that validates HTTP sampler responses against JSON/YAML Schema definitions using JsonPath expressions. Packaged as a fat JAR deployed to `<JMETER_HOME>/lib/ext/`.

- **Java 11**, Maven build
- **Namespace**: `io.github.yeshan333.*`
- **JMeter version**: 5.6.3

## Build & Test Commands

```bash
mvn clean test                                              # full test suite (27 tests)
mvn test -Dtest=SchemaAssertionTests                        # single test class
mvn test -Dtest=SchemaAssertionTests#testSetJsonPathExpr    # single test method
mvn clean package -DskipTests                               # build fat JAR -> examples/
```

## Architecture

```
SchemaAssertionGui (Swing, extends AbstractAssertionGui)
    |  createTestElement() / modifyTestElement()
    v
SchemaAssertion (extends AbstractTestElement, implements Assertion)
    |  getResult(SampleResult) — JMeter entry point
    |  getSchemaMimeType() — Tika for files, startsWith("{") heuristic for inline
    v
JsonSchemaValidator / YamlSchemaValidator (extends JsonSchemaValidator)
    |  Jackson ObjectMapper for parsing, jayway JsonPath for schema node extraction
    v
networknt json-schema-validator (Draft V4) -> Set<ValidationMessage>
```

**JsonPath is mandatory** -- the plugin always requires a JsonPath expression (typically `$.response`) to extract the schema node from a wrapper document.

## Dependency Scope Strategy

The fat JAR (`jar-with-dependencies`) must not bundle libraries JMeter already provides, or classpath conflicts occur at runtime. The scoping rules:

| Scope | What | Why |
|-------|------|-----|
| **provided** | Jackson core/databind/annotations, Tika, Log4j2/SLF4J | JMeter supplies these at runtime |
| **compile** (bundled) | `json-schema-validator`, `json-path`, `jackson-dataformat-yaml` | JMeter does NOT include these |
| **test** | JUnit Jupiter | Test-only |

**Critical**: `jackson-dataformat-yaml` must stay at a version compatible with JMeter's bundled `jackson-core` (currently 2.16.1 for JMeter 5.6.3). Upgrading it independently causes `NoSuchMethodError` at runtime. The other Jackson modules can be declared at a newer version since they're `provided` and JMeter's own version loads at runtime.

## CI Workflows

| Workflow | File | Trigger | What |
|----------|------|---------|------|
| Maven Tests | `run-tests.yaml` | push/PR to main | `mvn test` |
| Coverage | `jacoco-unit-test.yaml` | push to main | Tests + JaCoCo badge + Codecov |
| Integration | `integration-tests.yaml` | push/PR to main | Builds JAR, downloads JMeter 5.6.3, runs JMX test plan, verifies pass/fail results |
| Release JAR | `release-jar.yaml` | GitHub release published | Builds and uploads fat JAR to release assets |

The coverage workflow auto-commits a JaCoCo badge SVG, which can cause push conflicts if you push immediately after it runs.

## Code Style

- 4-space indentation, max 120 chars per line, K&R braces
- Import order: `com.*` -> `org.*` -> `java.*` -> `javax.*` -> `io.github.*`
- Tests use `@Test` + `@DisplayName`, JUnit 5 assertions
- `AssertionResult.setFailure(true)` for schema mismatches; `.setError(true)` for I/O errors
