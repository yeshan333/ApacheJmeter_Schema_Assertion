package com.github.yeshan333.common;

import com.fasterxml.jackson.databind.ObjectMapper;
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

public class JsonSchemaJsonPathTests extends JsonSchemaValidator {
    final Configuration JACKSON_JSON_NODE_CONFIGURATION = Configuration
            .builder()
            .mappingProvider(new JacksonMappingProvider())
            .jsonProvider(new JacksonJsonNodeJsonProvider())
            .build();
    @Test
    @DisplayName("JsonSchema Content JsonPath Validation")
    void testJsonSchemaContentValidationWithJsonPath() {
        try {
            String json = "{\n" +
                    "  \"api\": \"auth_code\",\n" +
                    "  \"title\": \"login token\",\n" +
                    "  \"description\": \"generate login token\",\n" +
                    "  \"example\": [\n" +
                    "    [{\n" +
                    "      \"ptoken\": \"ya29.CjGXA32cPvYBSxIE0jj52v_iH0FnqiK8kBBlUuAN03HxTsVyBecZsbLx7oCRg9v_p-Ae\"," +
                    "\n" +
                    "      \"challengeData\": \"abcdef\"\n" +
                    "    }, {\n" +
                    "      \"token\": \"AQGna15lCRcUfHEW2X18qw+B/Q5U61grAapjig3efZADWHsHSs3t0KEud5cFtTZZnRw=\",\n" +
                    "      \"code\": 0,\n" +
                    "      \"pinfo\": {}\n" +
                    "    }]\n" +
                    "  ],\n" +
                    "  \"request\": {\n" +
                    "    \"type\": \"object\",\n" +
                    "    \"properties\": {\n" +
                    "      \"ptoken\": {\n" +
                    "        \"type\": \"string\"\n" +
                    "      },\n" +
                    "      \"challengeData\": {\n" +
                    "        \"type\": \"string\"\n" +
                    "      }\n" +
                    "    },\n" +
                    "    \"required\": [\"ptoken\", \"challengeData\"]\n" +
                    "  },\n" +
                    "  \"response\": {\n" +
                    "    \"type\": \"object\",\n" +
                    "    \"properties\": {\n" +
                    "      \"token\": {\"type\": \"string\"},\n" +
                    "      \"uid\": {\"type\": \"string\"},\n" +
                    "      \"pinfo\": {\"type\": \"object\"}\n" +
                    "    },\n" +
                    "    \"required\": [\"token\"]\n" +
                    "  },\n" +
                    "  \"response_code\": [200, 401, 402, 405, 406, 409, 410, 462, 504, 10410, 10411, 10412, 10414, " +
                    "10500, 10501, 10502]\n" +
                    "}";
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
    @DisplayName("JsonSchema File JsonPath Validation")
    void testJsonSchemaFileValidationWithJsonPath() {
        try {
            String projectBasePath = Paths.get("").toAbsolutePath().toString();
            String testJsonSchemaPath = projectBasePath + "/src/test/resources/schema/data_schema.json";

            ObjectMapper mapper = new ObjectMapper();
            JsonNode json = mapper.readTree(Paths.get(testJsonSchemaPath).toFile());

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
}
