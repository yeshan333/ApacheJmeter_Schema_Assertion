package com.github.yeshan333.jmeter.assertions;

import org.apache.jmeter.assertions.AssertionResult;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;

public class JsonSchemaAssertionTests {
    @Test
    @DisplayName("schema file non-existent")
    void testAssertionFromNonExistentJsonFile() {
        SchemaAssertion jsonSchemaAssertion = new SchemaAssertion();
        AssertionResult assertionResult = new AssertionResult("json schema assertion");

        String response = "{\n" +
                "      \"token\": \"1\",\n" +
                "      \"code\": 0,\n" +
                "      \"pinfo\": {}\n" +
                "    }";

        String projectBasePath = Paths.get("").toAbsolutePath().toString();
        String jsonSchemaFilePath = projectBasePath + "/src/test/resources/schem/data_schema.json";

        String jsonPathExpr = "$.response";
        jsonSchemaAssertion.setSchemaValidationResult(assertionResult, response, jsonSchemaFilePath, true,
                jsonPathExpr);
        Assertions.assertSame(true, assertionResult.isError());
    }

    @Test
    @DisplayName("jmeter json schema from file validate (correct)")
    void testAssertionFromJsonFileResultCorrect() {
        SchemaAssertion jsonSchemaAssertion = new SchemaAssertion();
        AssertionResult assertionResult = new AssertionResult("json schema assertion");

        String response = "{\n" +
                "      \"token\": \"1\",\n" +
                "      \"code\": 0,\n" +
                "      \"pinfo\": {}\n" +
                "    }";

        String projectBasePath = Paths.get("").toAbsolutePath().toString();
        String jsonSchemaFilePath = projectBasePath + "/src/test/resources/schema/data_schema.json";

        String jsonPathExpr = "$.response";
        jsonSchemaAssertion.setSchemaValidationResult(assertionResult, response, jsonSchemaFilePath, true,
                jsonPathExpr);

        Assertions.assertSame(false, assertionResult.isFailure());
    }

    @Test
    @DisplayName("jmeter json schema from file validate (error)")
    void testAssertionFromJsonFileResultError() {
        SchemaAssertion jsonSchemaAssertion = new SchemaAssertion();
        AssertionResult assertionResult = new AssertionResult("json schema assertion");

        String response = "{\n" +
                "      \"token\": 1,\n" +
                "      \"code\": 0,\n" +
                "      \"pinfo\": {}\n" +
                "    }";

        String projectBasePath = Paths.get("").toAbsolutePath().toString();
        String jsonSchemaFilePath = projectBasePath + "/src/test/resources/schema/data_schema.json";

        String jsonPathExpr = "$.response";
        jsonSchemaAssertion.setSchemaValidationResult(assertionResult, response, jsonSchemaFilePath, true,
                jsonPathExpr);

        Assertions.assertSame(true, assertionResult.isFailure());
    }

    @Test
    @DisplayName("jmeter json schema from json content validate (correct)")
    void testAssertionFromJsonContentResultCorrect() {
        SchemaAssertion jsonSchemaAssertion = new SchemaAssertion();
        AssertionResult assertionResult = new AssertionResult("json schema assertion");

        String jsonSchemaContentString = "{\n" +
                "    \"api\": \"/v2/acquire\",\n" +
                "    \"title\": \"login\",\n" +
                "    \"description\": \"generate token\",\n" +
                "    \"request\": {\n" +
                "        \"type\": \"object\",\n" +
                "        \"properties\": {\n" +
                "            \"game\": {\"type\": \"string\"},\n" +
                "            \"server\": {\"type\": \"string\"}\n" +
                "        },\n" +
                "        \"required\": [\"game\"]\n" +
                "    },\n" +
                "    \"response\": {\n" +
                "        \"type\": \"object\",\n" +
                "        \"properties\": {\n" +
                "            \"token\": {\"type\": \"string\"},\n" +
                "            \"uid\": {\"type\": \"string\"},\n" +
                "            \"pinfo\": {\"type\": \"object\"},\n" +
                "            \"message\": {\"type\": \"number\"}\n" +
                "        },\n" +
                "        \"required\": [\"token\"]\n" +
                "    },\n" +
                "    \"response_code\": [401, 402, 405, 406, 409, 410, 462, 504, 10410, 10411, 10412, 10414, 10500, " +
                "10501, 10502]\n" +
                "}";

        String response = "{\n" +
                "      \"token\": \"1\",\n" +
                "      \"code\": 0,\n" +
                "      \"pinfo\": {}\n" +
                "    }";

        String jsonPathExpr = "$.response";
        jsonSchemaAssertion.setSchemaValidationResult(assertionResult, response, jsonSchemaContentString, false,
                jsonPathExpr);

        Assertions.assertSame(false, assertionResult.isFailure());
    }

    @Test
    @DisplayName("jmeter json schema from json content validate (correct)")
    void testAssertionFromJsonContentResultError() {
        SchemaAssertion jsonSchemaAssertion = new SchemaAssertion();
        AssertionResult assertionResult = new AssertionResult("json schema assertion");

        String jsonSchemaContentString = "{\n" +
                "    \"api\": \"/v2/acquire\",\n" +
                "    \"title\": \"login\",\n" +
                "    \"description\": \"generate token\",\n" +
                "    \"request\": {\n" +
                "        \"type\": \"object\",\n" +
                "        \"properties\": {\n" +
                "            \"game\": {\"type\": \"string\"},\n" +
                "            \"server\": {\"type\": \"string\"}\n" +
                "        },\n" +
                "        \"required\": [\"game\"]\n" +
                "    },\n" +
                "    \"response\": {\n" +
                "        \"type\": \"object\",\n" +
                "        \"properties\": {\n" +
                "            \"token\": {\"type\": \"string\"},\n" +
                "            \"uid\": {\"type\": \"string\"},\n" +
                "            \"pinfo\": {\"type\": \"object\"},\n" +
                "            \"message\": {\"type\": \"number\"}\n" +
                "        },\n" +
                "        \"required\": [\"token\"]\n" +
                "    },\n" +
                "    \"response_code\": [401, 402, 405, 406, 409, 410, 462, 504, 10410, 10411, 10412, 10414, 10500, " +
                "10501, 10502]\n" +
                "}";

        String response = "{\n" +
                "      \"token\": 1,\n" +
                "      \"code\": 0,\n" +
                "      \"pinfo\": {}\n" +
                "    }";

        String jsonPathExpr = "$.response";
        jsonSchemaAssertion.setSchemaValidationResult(assertionResult, response, jsonSchemaContentString, false,
                jsonPathExpr);

        Assertions.assertSame(true, assertionResult.isFailure());
    }
}
