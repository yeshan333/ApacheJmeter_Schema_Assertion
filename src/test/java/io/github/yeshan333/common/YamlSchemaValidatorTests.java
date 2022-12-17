package io.github.yeshan333.common;

import com.fasterxml.jackson.databind.JsonNode;

import com.networknt.schema.JsonSchema;
import com.networknt.schema.ValidationMessage;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Set;

public class YamlSchemaValidatorTests extends YamlSchemaValidator {
    @Test
    @DisplayName("load schema from yaml schema file (correct result)")
    void testLoadJsonSchemaFromYamlFileCorrectResult() {
        try {
            String projectBasePath = Paths.get("").toAbsolutePath().toString();
            String testYamlSchemaPath = projectBasePath + "/src/test/resources/schema/data_schema.yml";

            JsonSchema yamlSchema = getJsonSchemaFromYamlFile(testYamlSchemaPath);
            yamlSchema.initializeValidators();
            JsonNode node = getJsonNodeFromStringContent("{\"code\": 2}");

            Set<ValidationMessage> errors = yamlSchema.validate(node);
            Assertions.assertEquals(0, errors.size());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Test
    @DisplayName("load schema from yaml schema file (error result)")
    void testLoadJsonSchemaFromYamlFileErrorResult() {
        try {
            String projectBasePath = Paths.get("").toAbsolutePath().toString();
            String testYamlSchemaPath = projectBasePath + "/src/test/resources/schema/data_schema.yml";

            JsonSchema yamlSchema = getJsonSchemaFromYamlFile(testYamlSchemaPath);
            yamlSchema.initializeValidators();
            JsonNode node = getJsonNodeFromStringContent("{\"request\": \"2\"}");

            Set<ValidationMessage> errors = yamlSchema.validate(node);
            Assertions.assertEquals(1, errors.size());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Test
    @DisplayName("laod json schema from yaml content (correct)")
    void testLoadJsonSchemaFromYamlStringContentCorrectResult() {
        try {
            JsonSchema schema = getJsonSchemaFromYamlStringContent("api: verify_oauth_code\n" +
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
                    "    - code");
            JsonNode node = getJsonNodeFromStringContent("{\"code\": 2}");
            Set<ValidationMessage> errors = schema.validate(node);
            Assertions.assertEquals(0, errors.size());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Test
    @DisplayName("laod json schema from yaml content (error)")
    void testLoadJsonSchemaFromYamlStringContentErrorResult() {
        try {
            JsonSchema schema = getJsonSchemaFromYamlStringContent("api: verify_oauth_code\n" +
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
                    "    - code");
            JsonNode node = getJsonNodeFromStringContent("{\"code\": \"2\"}");
            Set<ValidationMessage> errors = schema.validate(node);
            Assertions.assertEquals(1, errors.size());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
