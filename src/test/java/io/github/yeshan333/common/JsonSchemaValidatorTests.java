package io.github.yeshan333.common;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.networknt.schema.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import java.io.IOException;
import java.util.Set;
import java.nio.file.Paths;

class JsonSchemaValidatorTests extends JsonSchemaValidator {
    @Test
    @DisplayName("load json schema from json schema file (correct result)")
    void testLoadJsonSchemaFromJsonFileCorrectResult() {
        try {
            String projectBasePath = Paths.get("").toAbsolutePath().toString();
            String testJsonSchemaPath = projectBasePath + "/src/test/resources/schema/data_schema.json";

            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonSchemaNode = mapper.readTree(Paths.get(testJsonSchemaPath).toFile());
            // TODO: Remove it
            // System.out.println(jsonSchemaNode);
            JsonSchemaFactory factory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V4);

            // TODO: Inject Config
            SchemaValidatorsConfig config = new SchemaValidatorsConfig();
            config.setTypeLoose(true);

            JsonSchema jsonSchema = factory.getSchema(jsonSchemaNode.get("response"), config);
            jsonSchema.initializeValidators();

            JsonNode node = getJsonNodeFromStringContent("{\n" +
                    "    \"ticket\": \"2323\",\n" +
                    "    \"token\": \"4sadasdada\"\n" +
                    "}");
            Set<ValidationMessage> errors = jsonSchema.validate(node);
            // TODO: Remove it
            // System.out.println(errors);
            Assertions.assertEquals(0, errors.size());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Test
    @DisplayName("load json schema from json schema file (error result)")
    void testLoadJsonSchemaFromJsonFileErrorResult() {
        try {
            String projectBasePath = Paths.get("").toAbsolutePath().toString();
            String testJsonSchemaPath = projectBasePath + "/src/test/resources/schema/data_schema.json";

            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonSchemaNode = mapper.readTree(Paths.get(testJsonSchemaPath).toFile());

            // TODO: Remove it
            // System.out.println(jsonSchemaNode);
            JsonSchemaFactory factory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V4);

            // Inject Config
            SchemaValidatorsConfig config = new SchemaValidatorsConfig();
            config.setTypeLoose(true);

            JsonSchema jsonSchema = factory.getSchema(jsonSchemaNode.get("response"), config);
            jsonSchema.initializeValidators();

            JsonNode node = getJsonNodeFromStringContent("{\n" +
                    "    \"ticket\": \"2323\",\n" +
                    "    \"token\": 2\n" +
                    "}");
            Set<ValidationMessage> errors = jsonSchema.validate(node);
            // TODO: Remove it
            // System.out.println(errors);
            Assertions.assertEquals(1, errors.size());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Test
    @DisplayName("json schema from string content (correct result)")
    void testJsonSchemaFromJsonStringContent() {
        try {
            JsonSchema schema = getJsonSchemaFromJsonStringContent("{\n" +
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
                    "}");
            JsonNode node = getJsonNodeFromStringContent("{\n" +
                    "    \"token\": \"1\",\n" +
                    "    \"ticket\": \"GG\"\n" +
                    "}");
            Set<ValidationMessage> errors = schema.validate(node);
            // TODO: Remove it
            // System.out.println(errors);
            Assertions.assertEquals(0, errors.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("json schema from string content (error result)")
    void testJsonSchemaFromJsonStringContentErrorResult() {
        try {
            JsonSchema schema = getJsonSchemaFromJsonStringContent("{\n" +
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
                    "}");
            JsonNode node = getJsonNodeFromStringContent("{\"id\": \"2\"}");
            Set<ValidationMessage> errors = schema.validate(node);
            // TODO: Remove it
            // System.out.println(errors);
            Assertions.assertEquals(1, errors.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("automatic json schema version")
    void testAutomaticSchemaVersion() {
        try {
            // With automatic version detection
            JsonNode schemaNode = getJsonNodeFromStringContent(
                    "{\"$schema\": \"http://json-schema.org/draft-06/schema#\", \"properties\": { \"id\": {\"type\": " +
                            "\"number\"}}}");
            JsonSchema schema = getJsonSchemaFromJsonNodeAutomaticVersion(schemaNode);

            // by default all schemas are loaded lazily. load them eagerly via
            schema.initializeValidators();

            JsonNode node = getJsonNodeFromStringContent("{\"id\": 2}");
            Set<ValidationMessage> errors = schema.validate(node);
            // TODO: Remove it
            // System.out.println("=======schema check result========");
            // System.out.println("schema: " + schemaNode);
            // System.out.println("wait to check: " + node);
            // System.out.println(errors);
            // System.out.println("==================================");
            Assertions.assertEquals(0, errors.size());
        } catch (IOException ie) {
            ie.printStackTrace();
        }
    }
}

