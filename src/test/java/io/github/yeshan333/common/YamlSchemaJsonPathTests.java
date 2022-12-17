package io.github.yeshan333.common;

import com.fasterxml.jackson.databind.JsonNode;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.spi.json.JacksonJsonNodeJsonProvider;
import com.jayway.jsonpath.spi.mapper.JacksonMappingProvider;

import com.networknt.schema.JsonSchema;
import com.networknt.schema.ValidationMessage;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;
import java.util.Set;

import static com.jayway.jsonpath.JsonPath.using;

public class YamlSchemaJsonPathTests extends YamlSchemaValidator {
    final Configuration JACKSON_JSON_NODE_CONFIGURATION = Configuration
            .builder()
            .mappingProvider(new JacksonMappingProvider())
            .jsonProvider(new JacksonJsonNodeJsonProvider())
            .build();

    @Test
    @DisplayName("YamlSchema Content JsonPath Validation")
    void testYamlSchemaFromContentValidationWithJsonPath() {
        try {
            String yaml = "api: verify_oauth_code\n" +
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

            JsonNode json = getJsonNodeFromYamlStringContent(yaml);
            String jsonPathExpression = "$.response";
            JsonNode responseJsonNode = using(JACKSON_JSON_NODE_CONFIGURATION).parse(json).read(jsonPathExpression);

            JsonSchema jsonSchema = getJsonSchemaFromJsonNode(responseJsonNode);

            JsonNode node = getJsonNodeFromStringContent("{\n" +
                    "    \"ticket\": \"2323\",\n" +
                    "    \"token\": 2323\n" +
                    "}");
            Set<ValidationMessage> errors = jsonSchema.validate(node);
            Assertions.assertEquals(1, errors.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("YamlSchema File JsonPath Validation")
    void testYamlSchemaFromFileValidationWithJsonPath() {
        try {
            String projectBasePath = Paths.get("").toAbsolutePath().toString();
            String testYamlSchemaPath = projectBasePath + "/src/test/resources/schema/data_schema.yml";

            JsonNode yaml = getJsonNodeFromYamlFile(testYamlSchemaPath);

            String jsonPathExpression = "$.response";
            JsonNode responseJsonNode = using(JACKSON_JSON_NODE_CONFIGURATION).parse(yaml).read(jsonPathExpression);

            JsonSchema jsonSchema = getJsonSchemaFromJsonNode(responseJsonNode);

            JsonNode node = getJsonNodeFromStringContent("{\n" +
                    "    \"ticket\": \"2323\",\n" +
                    "    \"token\": 2323\n" +
                    "}");
            Set<ValidationMessage> errors = jsonSchema.validate(node);
            Assertions.assertEquals(1, errors.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
