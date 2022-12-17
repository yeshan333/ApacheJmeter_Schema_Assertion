package io.github.yeshan333.jmeter.assertions;

import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.assertions.AssertionResult;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;

public class SchemaAssertionTests {
    @Test
    @DisplayName("getJsonSchemaFilePath & setJsonSchemaContent & setJsonPathExpr")
    void testGetJsonSchemaFilePath() {
        SchemaAssertion schemaAssertion = new SchemaAssertion();

        String projectBasePath = Paths.get("").toAbsolutePath().toString();
        String yamlSchemaFilePath = projectBasePath + "/src/test/resources/schema/data_schema.yml";
        schemaAssertion.setJsonSchemaFilePath(yamlSchemaFilePath);
        Assertions.assertEquals(yamlSchemaFilePath, schemaAssertion.getJsonSchemaFilePath());

        schemaAssertion.setJsonSchemaContent("{\"response:{}\"}");
        Assertions.assertEquals("{\"response:{}\"}", schemaAssertion.getJsonSchemaContent());

        schemaAssertion.setJsonPathExpr("$.response");
        Assertions.assertEquals("$.response", schemaAssertion.getJsonPathExpr());
    }

    @Test
    @DisplayName("Assertion Result")
    void testSetJsonPathExpr() {
        SchemaAssertion schemaAssertion = new SchemaAssertion();
        SampleResult responseSampleResult = new SampleResult();

        AssertionResult actualSampleResultResponseDataNull = schemaAssertion.getResult(responseSampleResult);
        Assertions.assertEquals("Response was null", actualSampleResultResponseDataNull.getFailureMessage());

        responseSampleResult.setResponseData("{\"status\": \"ok\"}", "UTF-8");

        AssertionResult actualSampleResultJsonPathExprLost = schemaAssertion.getResult(responseSampleResult);
        Assertions.assertEquals(" JsonPath Expression is required!",
                actualSampleResultJsonPathExprLost.getFailureMessage());

        schemaAssertion.setJsonPathExpr("$.response");
        AssertionResult actualSampleResultSchemaContentAndExprLost = schemaAssertion.getResult(responseSampleResult);
        Assertions.assertEquals(" Schema File Path or Schema Content is required (One of them should be empty) !",
                actualSampleResultSchemaContentAndExprLost.getFailureMessage());

        schemaAssertion.setJsonSchemaFilePath("");
        schemaAssertion.setJsonSchemaContent("    \"response\": {\n" +
                "        \"type\": \"object\",\n" +
                "        \"properties\": {\n" +
                "            \"token\": {\"type\": \"string\"},\n" +
                "            \"uid\": {\"type\": \"string\"},\n" +
                "            \"pinfo\": {\"type\": \"object\"},\n" +
                "            \"message\": {\"type\": \"number\"}\n" +
                "        },\n" +
                "        \"required\": [\"token\"]\n" +
                "    }");
        schemaAssertion.setJsonPathExpr("$.response");
        AssertionResult actualSampleResultTypeUnMatched = schemaAssertion.getResult(responseSampleResult);
        Assertions.assertEquals(true, actualSampleResultTypeUnMatched.getFailureMessage().startsWith("[$.token"));

        schemaAssertion.setJsonSchemaContent("");
        String projectBasePath = Paths.get("").toAbsolutePath().toString();
        String yamlSchemaFilePath = projectBasePath + "/src/test/resources/schema/data_schema.yml";
        schemaAssertion.setJsonSchemaFilePath(yamlSchemaFilePath);

        AssertionResult actualSampleResultSchemaFromFile = schemaAssertion.getResult(responseSampleResult);
        Assertions.assertEquals(true, actualSampleResultSchemaFromFile.getFailureMessage().startsWith("[$.code"));

        schemaAssertion.setJsonPathExpr("$.unexist_path");
        AssertionResult actualSampleResultJsonPathUnexist = schemaAssertion.getResult(responseSampleResult);
        Assertions.assertEquals("(load schema)JsonPath Path File NotFound -> No results for path: $['unexist_path']",
                actualSampleResultJsonPathUnexist.getFailureMessage());

        schemaAssertion.setJsonPathExpr("$Illegal");
        AssertionResult actualSampleResultJsonPathIllegal = schemaAssertion.getResult(responseSampleResult);
        Assertions.assertEquals("(load schema)Illegal character at position 1 expected '.' or '['",
                actualSampleResultJsonPathIllegal.getFailureMessage());
    }
}
