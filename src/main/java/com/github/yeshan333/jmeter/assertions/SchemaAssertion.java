package io.github.yeshan333.jmeter.assertions;

import com.networknt.schema.JsonSchema;
import com.networknt.schema.ValidationMessage;

import org.apache.tika.Tika;
import com.fasterxml.jackson.databind.JsonNode;
import com.jayway.jsonpath.PathNotFoundException;

import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.testelement.AbstractTestElement;
import org.apache.jmeter.assertions.Assertion;
import org.apache.jmeter.assertions.AssertionResult;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.yeshan333.common.YamlSchemaValidator;
import io.github.yeshan333.common.JsonSchemaValidator;

public class SchemaAssertion extends AbstractTestElement implements Serializable, Assertion {
    private static final long serialVersionUID = 234L;
    public static final String SCHEMA_FILE_OR_SCHEMA_CONTENT_IS_REQUIRED = " Schema File Path or Schema Content is " +
            "required (One of them should be empty) !";
    public static final String JSONPATH_EXPRESSION_IS_REQUIRED = " JsonPath Expression is required!";
    public static final String SCHEMA_FILEPATH_KEY = "schema_assertion_filepath";
    public static final String SCHEMA_CONTENT_KEY = "schema_content";
    public static final String JSONPATH_KEY = "jsonpath_expression";
    // TODO: protocol type detection
    private String errObj = "";

    private static final Logger log = LoggerFactory.getLogger(SchemaAssertion.class);

    public void setJsonSchemaFilePath(String jsonSchemaFilePath) throws IllegalArgumentException {
        setProperty(SCHEMA_FILEPATH_KEY, jsonSchemaFilePath);
    }

    public String getJsonSchemaFilePath() {
        return getPropertyAsString(SCHEMA_FILEPATH_KEY);
    }

    public void setJsonschemaContent(String jsonschemaContent) {
        setProperty(SCHEMA_CONTENT_KEY, jsonschemaContent);
    }

    public String getJsonschemaContent() {
        return getPropertyAsString(SCHEMA_CONTENT_KEY);
    }

    public void setJsonPathExpr(String jsonPathExpr) {
        setProperty(JSONPATH_KEY, jsonPathExpr);
    }

    public String getJsonPathExpr() {
        return getPropertyAsString(JSONPATH_KEY);
    }

    /**
     * Get MimeType From text
     */
    public String getSchemaMimeType(String schemaSource, boolean isFromSchemaFile) {
        String mimeType;
        if(isFromSchemaFile) {
            Tika tika = new Tika();
            mimeType = tika.detect(schemaSource);
            return mimeType;
        }

        if(schemaSource.startsWith("{")) {
            return "application/json";
        } else {
            return "text/x-yaml";
        }
    }

    /**
     * Get assertion result.
     */
    @Override
    public AssertionResult getResult(SampleResult response) {
        AssertionResult result = new AssertionResult(getName());

        String resultData = response.getResponseDataAsString();
        if (resultData.length() == 0) {
            return result.setResultForNull();
        }

        String jsonSchemaFilePath = getJsonSchemaFilePath();
        String jsonSchemaContent = getJsonschemaContent();
        String jsonPathExpr = getJsonPathExpr();
        log.debug("response Json String: {}, json schema file path: {}", resultData, jsonSchemaFilePath);

        // TODO: bad code smell
        if ( jsonSchemaContent.length() != 0 && jsonSchemaFilePath.length() == 0) {
            setSchemaValidationResult(result, resultData, jsonSchemaContent, false, jsonPathExpr);
        } else if (jsonSchemaFilePath.length() != 0 && jsonSchemaContent.length() == 0){
            setSchemaValidationResult(result, resultData, jsonSchemaFilePath, true, jsonPathExpr);
        } else {
            result.setResultForFailure(SCHEMA_FILE_OR_SCHEMA_CONTENT_IS_REQUIRED);
        }

        if( jsonPathExpr.length() == 0 ) {
            result.setResultForFailure(JSONPATH_EXPRESSION_IS_REQUIRED);
        }

        return result;
    }

    /**
     * Set validation result.
     * TODO: refactor
     */
    public void setSchemaValidationResult(AssertionResult result, String jsonRespStr, String jsonSchemaSource,
                                          boolean isSchemaFromFile, String jsonPathExpr) {
        try {
            JsonSchemaValidator jsonSchemaValidator = new JsonSchemaValidator();
            YamlSchemaValidator yamlSchemaValidator = new YamlSchemaValidator();
            errObj = "(load schema)";
            JsonSchema schema;
            // TODO: improve this code, hard code
            if(isSchemaFromFile) {
                Path schemaPath = Paths.get(jsonSchemaSource);
                if (!Files.exists(schemaPath)) {
                    result.setFailureMessage("Schema File is non-existent.");
                    result.setFailure(true);
                    throw new FileNotFoundException();
                }

                if(getSchemaMimeType(jsonSchemaSource, true).equals("application/json")) {
                    schema = jsonSchemaValidator.getJsonSchemaFromJsonFile(jsonSchemaSource, jsonPathExpr);
                } else {
                    schema = yamlSchemaValidator.getJsonSchemaFromYamlFile(jsonSchemaSource, jsonPathExpr);
                }

            } else {
                JsonNode schemaNode;
                if(getSchemaMimeType(jsonSchemaSource, false).equals("application/json")) {
                    schemaNode = jsonSchemaValidator.getJsonNodeFromStringContent(jsonSchemaSource);
                } else {
                    schemaNode = yamlSchemaValidator.getJsonNodeFromYamlStringContent(jsonSchemaSource);
                }
                schema = jsonSchemaValidator.getJsonSchemaFromJsonNode(schemaNode, jsonPathExpr);
            }

            errObj = "(load response)";
            JsonNode response = jsonSchemaValidator.getJsonNodeFromStringContent(jsonRespStr);

            errObj = "(schema validate)";
            Set<ValidationMessage> validationResults = schema.validate(response);

            if (validationResults.size() != 0) {
                result.setFailureMessage(validationResults.toString());
                result.setFailure(true);
            }
        } catch(FileNotFoundException e) {
            log.warn("Schema File is non-existent: ", e);
            result.setError(true);
        } catch (IOException e) {
            log.warn("IO error: ", e);
            result.setFailureMessage(errObj + e.getMessage());
            result.setError(true);
        } catch (PathNotFoundException e) {
            log.warn("JsonPath File Path NotFound: ", e);
            result.setFailureMessage(errObj + "JsonPath Path File NotFound -> " + e.getMessage());
            result.setError(true);
        } catch (Exception e) {
            log.warn("Unknown Error", e);
            result.setFailureMessage(errObj + e.getMessage());
            result.setError(true);
        }
    }
}
