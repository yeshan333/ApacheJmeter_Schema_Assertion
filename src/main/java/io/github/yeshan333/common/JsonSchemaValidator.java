package io.github.yeshan333.common;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.spi.mapper.JacksonMappingProvider;
import com.jayway.jsonpath.spi.json.JacksonJsonNodeJsonProvider;

import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.SpecVersionDetector;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.io.IOException;
import java.nio.file.Paths;

import static com.jayway.jsonpath.JsonPath.using;

public class JsonSchemaValidator {
    private final ObjectMapper mapper = new ObjectMapper();

    final Configuration JACKSON_JSON_NODE_CONFIGURATION = Configuration
            .builder()
            .mappingProvider(new JacksonMappingProvider())
            .jsonProvider(new JacksonJsonNodeJsonProvider())
            .build();

    public JsonNode getJsonNodeFromStringContent(String content) throws IOException {
        return mapper.readTree(content);
    }

    protected JsonNode getJsonNodeFromUrl(String url) throws IOException {
        return mapper.readTree(new URL(url));
    }

    public JsonNode getJsonNodeFromJsonFile(String jsonFilePath) throws IOException {
        return mapper.readTree(Paths.get(jsonFilePath).toFile());
    }

    public JsonSchema getJsonSchemaFromStringContent(String schemaContent) {
        JsonSchemaFactory factory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V4);
        return factory.getSchema(schemaContent);
    }

    public JsonSchema getJsonSchemaFromJsonStringContent(String schemaContent) throws IOException{
        JsonSchemaFactory factory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V4);
        JsonNode jsonSchema = getJsonNodeFromStringContent(schemaContent);
        JsonNode responseJsonNode = jsonSchema.get("response");
        return factory.getSchema(responseJsonNode);
    }

    public JsonSchema getJsonSchemaFromJsonStringContent(String schemaContent, String jsonPathExpr) throws IOException {
        JsonSchemaFactory factory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V4);
        JsonNode jsonSchema = getJsonNodeFromStringContent(schemaContent);
        JsonNode responseJsonNode = using(JACKSON_JSON_NODE_CONFIGURATION).parse(jsonSchema).read(jsonPathExpr);
        return factory.getSchema(responseJsonNode);
    }

    public JsonSchema getJsonSchemaFromJsonFile(String jsonFilePath, String jsonPathExpr) throws IOException {
        JsonNode jsonSchemaNode = mapper.readTree(Paths.get(jsonFilePath).toFile());
        JsonSchemaFactory factory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V4);
        JsonNode responseJsonNode = using(JACKSON_JSON_NODE_CONFIGURATION).parse(jsonSchemaNode).read(jsonPathExpr);
        return factory.getSchema(responseJsonNode);
    }

    protected JsonSchema getJsonSchemaFromUrl(String uri) throws URISyntaxException {
        JsonSchemaFactory factory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V4);
        return factory.getSchema(new URI(uri));
    }

    public JsonSchema getJsonSchemaFromJsonNode(JsonNode jsonNode) {
        JsonSchemaFactory factory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V4);
        return factory.getSchema(jsonNode);
    }

    public JsonSchema getJsonSchemaFromJsonNode(JsonNode jsonNode, String jsonPathExpr) {
        JsonSchemaFactory factory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V4);
        JsonNode responseJsonNode = using(JACKSON_JSON_NODE_CONFIGURATION).parse(jsonNode).read(jsonPathExpr);
        return factory.getSchema(responseJsonNode);
    }

    // Automatically detect schema version for given JsonNode
    protected JsonSchema getJsonSchemaFromJsonNodeAutomaticVersion(JsonNode jsonNode) {
        JsonSchemaFactory factory = JsonSchemaFactory.getInstance(SpecVersionDetector.detect(jsonNode));
        return factory.getSchema(jsonNode);
    }
}
