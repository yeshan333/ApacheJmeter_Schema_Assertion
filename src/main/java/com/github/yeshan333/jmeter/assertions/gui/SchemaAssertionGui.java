package com.github.yeshan333.jmeter.assertions.gui;

import org.apache.jmeter.gui.util.HorizontalPanel;
import org.apache.jmeter.gui.util.VerticalPanel;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.assertions.gui.AbstractAssertionGui;

import javax.swing.*;
import java.awt.BorderLayout;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.yeshan333.jmeter.assertions.SchemaAssertion;

public class SchemaAssertionGui extends AbstractAssertionGui {
    private static final Logger log = LoggerFactory.getLogger(SchemaAssertionGui.class);
    private static final long serialVersionUID = 241L;
    private JTextField schemaPath;
    private JTextArea schemaContent;
    private JTextField jsonPathExpr;

    public SchemaAssertionGui() {
        init();
    }

    /**
     * Return the label.
     */
    @Override
    public String getStaticLabel() {
        return "Schema Assertion";
    }

    @Override
    public String getLabelResource() {
        return this.getClass().getSimpleName();
    }

    /**
     * Create Test Element.
     */
    @Override
    public TestElement createTestElement() {
        log.debug("SchemaAssertionGui.createTestElement() called");
        SchemaAssertion el = new SchemaAssertion();
        modifyTestElement(el);
        return el;
    }

    /**
     * Modify a given TestElement to mirror the data in the gui components.
     *
     * @see org.apache.jmeter.gui.JMeterGUIComponent#modifyTestElement(TestElement).
     */
    @Override
    public void modifyTestElement(TestElement inElement) {
        log.debug("SchemaAssertionGui.modifyTestElement() called");
        configureTestElement(inElement);
        ((SchemaAssertion) inElement).setJsonSchemaFilePath(schemaPath.getText());
        ((SchemaAssertion) inElement).setJsonschemaContent(schemaContent.getText());
        ((SchemaAssertion) inElement).setJsonPathExpr(jsonPathExpr.getText());
    }

    /**
     * Implement JMeterGUIComponent.clearGui.
     */
    @Override
    public void clearGui() {
        super.clearGui();
        schemaPath.setText("");
        schemaContent.setText("");
        jsonPathExpr.setText("");
    }

    /**
     * Configure the GUI from the associated test element.
     *
     * @param el - the test element (should be SchemaAssertion).
     */
    @Override
    public void configure(TestElement el) {
        super.configure(el);
        SchemaAssertion assertion = (SchemaAssertion) el;
        schemaPath.setText(assertion.getJsonSchemaFilePath());
        schemaContent.setText(assertion.getJsonschemaContent());
        jsonPathExpr.setText(assertion.getJsonPathExpr());
    }

    /**
     * Initialize the GUI.
     */
    private void init() {
        setLayout(new BorderLayout(0, 10));
        setBorder(makeBorder());

        add(makeTitlePanel(), BorderLayout.NORTH);

        JPanel mainPanel = new JPanel(new BorderLayout());

        VerticalPanel assertionPanel = new VerticalPanel();
        assertionPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
                "Schema Validator"));

        // Get Schema File
        HorizontalPanel schemaPanel = new HorizontalPanel();

        schemaPanel.add(new JLabel("Schema File Path:"));

        schemaPath = new JTextField(26);
        schemaPanel.add(schemaPath);

        HorizontalPanel jsonPathPanel = new HorizontalPanel();
        jsonPathExpr = new JTextField();
        jsonPathPanel.add(new JLabel("JsonPath Expression:"));
        jsonPathPanel.add(jsonPathExpr);

        // Get schema yaml/json format text
        HorizontalPanel schemaTextContentPanel = new HorizontalPanel();
        schemaTextContentPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
                "plain text (json/yaml)"));

        schemaContent = new JTextArea("json/yaml schema", 30, 30);
        schemaContent.setLineWrap(true);
        JScrollPane jsp = new JScrollPane(schemaContent);
        schemaTextContentPanel.add(jsp);

        assertionPanel.add(schemaPanel);
        assertionPanel.add(jsonPathPanel);
        assertionPanel.add(schemaTextContentPanel);

        mainPanel.add(assertionPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
    }
}
