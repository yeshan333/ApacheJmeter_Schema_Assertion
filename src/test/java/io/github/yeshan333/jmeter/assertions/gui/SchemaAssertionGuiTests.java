package io.github.yeshan333.jmeter.assertions.gui;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class SchemaAssertionGuiTests {
    @Test
    @DisplayName("GUI label: Schema Assertion")
    void testGetStaticLabel() {
        SchemaAssertionGui gui = new SchemaAssertionGui();
        Assertions.assertEquals("Schema Assertion", gui.getStaticLabel());
    }

    @Test
    @DisplayName("Label resource class: SchemaAssertionGui")
    void testGetLabelResource() {
        SchemaAssertionGui gui = new SchemaAssertionGui();
        Assertions.assertEquals("SchemaAssertionGui", gui.getLabelResource());
    }

    @Test
    @DisplayName("createTestElement")
    void testCreateTestElement() {
        SchemaAssertionGui gui = new SchemaAssertionGui();
        Assertions.assertEquals("Schema Assertion", gui.createTestElement().getName());
    }
}
