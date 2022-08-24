package io.github.yeshan333.jmeter.assertions;

import org.apache.jmeter.assertions.AssertionResult;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;

public class YamlSchemaAssertionTests {
    @Test
    @DisplayName("jmeter yaml schema from file validate (correct)")
    void testAssertionFromYamlFileResultCorrect() {
        SchemaAssertion yamlSchemaAssertion = new SchemaAssertion();
        AssertionResult assertionResult = new AssertionResult("json schema assertion");

        String response = "{\n" +
                "      \"token\": \"1\",\n" +
                "      \"code\": 0,\n" +
                "      \"pinfo\": {}\n" +
                "    }";

        String projectBasePath = Paths.get("").toAbsolutePath().toString();
        String yamlSchemaFilePath = projectBasePath + "/src/test/resources/schema/data_schema.yml";

        String jsonPathExpr = "$.response";
        yamlSchemaAssertion.setSchemaValidationResult(assertionResult, response, yamlSchemaFilePath, true,
                jsonPathExpr);

        Assertions.assertSame(false, assertionResult.isFailure());
    }

    @Test
    @DisplayName("jmeter yaml schema from file validate (error)")
    void testAssertionFromYamlFileErrorCorrect() {
        SchemaAssertion yamlSchemaAssertion = new SchemaAssertion();
        AssertionResult assertionResult = new AssertionResult("json schema assertion");

        String response = "{\n" +
                "      \"token\": \"1\",\n" +
                "      \"code\": \"0\",\n" +
                "      \"pinfo\": {}\n" +
                "    }";

        String projectBasePath = Paths.get("").toAbsolutePath().toString();
        String yamlSchemaFilePath = projectBasePath + "/src/test/resources/schema/data_schema.yml";

        String jsonPathExpr = "$.response";
        yamlSchemaAssertion.setSchemaValidationResult(assertionResult, response, yamlSchemaFilePath, true,
                jsonPathExpr);

        Assertions.assertSame(true, assertionResult.isFailure());
    }

    @Test
    @DisplayName("jmeter json schema from yaml content validate (correct)")
    void testAssertionFromYamlContentResultCorrect() {
        SchemaAssertion yamlSchemaAssertion = new SchemaAssertion();
        AssertionResult assertionResult = new AssertionResult("json schema assertion");

        String jsonSchemaContentString = "api: verify_oauth_code\n" +
                "title: check OAuth verify_code\n" +
                "request:\n" +
                "  type: object\n" +
                "  properties:\n" +
                "    player_id:\n" +
                "      type: string\n" +
                "      minLength: 1\n" +
                "    client_id:\n" +
                "      type: string\n" +
                "      minLength: 1\n" +
                "    verify_code:\n" +
                "      type: string\n" +
                "      minLength: 1\n" +
                "    redirect_uri:\n" +
                "      type: string\n" +
                "      minLength: 1\n" +
                "  required:\n" +
                "    - player_id\n" +
                "    - client_id\n" +
                "    - verify_code\n" +
                "    - redirect_uri\n" +
                "\n" +
                "response:\n" +
                "  type: object\n" +
                "  properties:\n" +
                "    code:\n" +
                "      type: integer\n" +
                "    message:\n" +
                "      type: string\n" +
                "  required:\n" +
                "    - code";

        String response = "{\n" +
                "      \"token\": \"1\",\n" +
                "      \"code\": 0,\n" +
                "      \"pinfo\": {}\n" +
                "    }";

        String jsonPathExpr = "$.response";
        yamlSchemaAssertion.setSchemaValidationResult(assertionResult, response, jsonSchemaContentString, false,
                jsonPathExpr);

        Assertions.assertSame(false, assertionResult.isFailure());
    }

    @Test
    @DisplayName("jmeter json schema from yaml content validate (error)")
    void testAssertionFromYamlContentResultError() {
        SchemaAssertion yamlSchemaAssertion = new SchemaAssertion();
        AssertionResult assertionResult = new AssertionResult("json schema assertion");

        String jsonSchemaContentString = "api: verify_oauth_code\n" +
                "title: check OAuth verify_code\n" +
                "request:\n" +
                "  type: object\n" +
                "  properties:\n" +
                "    player_id:\n" +
                "      type: string\n" +
                "      minLength: 1\n" +
                "    client_id:\n" +
                "      type: string\n" +
                "      minLength: 1\n" +
                "    verify_code:\n" +
                "      type: string\n" +
                "      minLength: 1\n" +
                "    redirect_uri:\n" +
                "      type: string\n" +
                "      minLength: 1\n" +
                "  required:\n" +
                "    - player_id\n" +
                "    - client_id\n" +
                "    - verify_code\n" +
                "    - redirect_uri\n" +
                "\n" +
                "response:\n" +
                "  type: object\n" +
                "  properties:\n" +
                "    code:\n" +
                "      type: integer\n" +
                "    message:\n" +
                "      type: string\n" +
                "  required:\n" +
                "    - code";

        String response = "{\n" +
                "      \"token\": \"1\",\n" +
                "      \"code\": \"0\",\n" +
                "      \"pinfo\": {}\n" +
                "    }";

        String jsonPathExpr = "$.response";
        yamlSchemaAssertion.setSchemaValidationResult(assertionResult, response, jsonSchemaContentString, false,
                jsonPathExpr);

        Assertions.assertSame(true, assertionResult.isFailure());
    }
}
