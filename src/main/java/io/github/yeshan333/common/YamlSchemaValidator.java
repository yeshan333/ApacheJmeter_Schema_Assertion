package io.github.yeshan333.common;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.spi.json.JacksonJsonNodeJsonProvider;
import com.jayway.jsonpath.spi.mapper.JacksonMappingProvider;

import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;

import java.io.IOException;
import java.nio.file.Paths;

import static com.jayway.jsonpath.JsonPath.using;

public class YamlSchemaValidator extends JsonSchemaValidator {
    private final ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

    final Configuration JACKSON_JSON_NODE_CONFIGURATION = Configuration
            .builder()
            .mappingProvider(new JacksonMappingProvider())
            .jsonProvider(new JacksonJsonNodeJsonProvider())
            .build();

    public JsonNode getJsonNodeFromYamlStringContent(String content) throws IOException {
        return mapper.readTree(content);
    }

    public JsonNode getJsonNodeFromYamlFile(String jsonFilePath) throws IOException {
        return mapper.readTree(Paths.get(jsonFilePath).toFile());
    }

    public JsonSchema getJsonSchemaFromYamlStringContent(String schemaContent) throws IOException {
        JsonSchemaFactory factory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V4);
        JsonNode yamlSchema = getJsonNodeFromYamlStringContent(schemaContent);
        JsonNode responseJsonNode = yamlSchema.get("response");
        return factory.getSchema(responseJsonNode);
    }

    public JsonSchema getJsonSchemaFromYamlFile(String yamlFilePath) throws IOException {
        JsonNode yamlSchemaNode = mapper.readTree(Paths.get(yamlFilePath).toFile());
        JsonSchemaFactory factory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V4);
        JsonNode responseJsonNode = yamlSchemaNode.get("response");
        return factory.getSchema(responseJsonNode);
    }

    public JsonSchema getJsonSchemaFromYamlFile(String yamlFilePath, String jsonPathExpr) throws IOException {
        JsonNode yamlSchemaNode = mapper.readTree(Paths.get(yamlFilePath).toFile());
        JsonSchemaFactory factory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V4);
        JsonNode responseJsonNode = using(JACKSON_JSON_NODE_CONFIGURATION).parse(yamlSchemaNode).read(jsonPathExpr);
        return factory.getSchema(responseJsonNode);
    }
}
