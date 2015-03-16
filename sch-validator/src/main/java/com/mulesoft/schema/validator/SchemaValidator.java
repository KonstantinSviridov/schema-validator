package com.mulesoft.schema.validator;

import java.io.File;




import java.io.IOException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jackson.JsonLoader;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;

public class SchemaValidator {


    public ValidationResult validate(File schemaFile, File exampleFile){
        
        String fName = schemaFile.getName().toLowerCase();
        if(fName.endsWith("xsd")){
            return validateXml(schemaFile, exampleFile);
        }
        else if(fName.endsWith("json")){        
            return validateJson(schemaFile, exampleFile);
        }
        return new ValidationResult("Unknown media type", ValidationResult.INVALID, schemaFile, null);
    }

    private ValidationResult validateJson(File schemaFile, File exampleFile) {
        
        
      
            JsonNode schemaNode;
            try {
                schemaNode = JsonLoader.fromFile(schemaFile);
            } catch (IOException e) {
                return new ValidationResult("Unable to parse file", ValidationResult.EXCEPTION, schemaFile, e);
            }
            JsonNode exampleNode;
            try {
                exampleNode = JsonLoader.fromFile(exampleFile);
            } catch (IOException e) {
                return new ValidationResult("Unable to parse file", ValidationResult.EXCEPTION, exampleFile, e);
            }
      
            final JsonSchemaFactory factory = JsonSchemaFactory.byDefault();
      
            JsonSchema schema;
            try {
                schema = factory.getJsonSchema(schemaNode);
            } catch (ProcessingException e) {
                return new ValidationResult("Unable to parse JSON Schema", ValidationResult.EXCEPTION, schemaFile, e);
            }
      
            ProcessingReport report;
            try {
                report = schema.validate(exampleNode);
            } catch (ProcessingException e) {
                return new ValidationResult("Schema validation failed", ValidationResult.EXCEPTION, schemaFile, e);
            }
            String message = report.toString().trim();
            int status = message.endsWith("success") ? ValidationResult.SUCCESS : ValidationResult.INVALID;
            return new ValidationResult(message, status, schemaFile, null);
    }
    
    private ValidationResult validateXml(File schemaFile, File exampleFile) {

        // parse an XML document into a DOM tree
        DocumentBuilder parser = null;
        try {
            parser = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            return new ValidationResult("Exception", ValidationResult.EXCEPTION, schemaFile, e);
        }
        Document document;
        try {
            document = parser.parse(exampleFile);
        } catch (SAXException e) {
            return new ValidationResult("Unable to parse file", ValidationResult.EXCEPTION, exampleFile, e);
        } catch (IOException e) {
            return new ValidationResult("Exception", ValidationResult.EXCEPTION, schemaFile, e);
        }

        // create a SchemaFactory capable of understanding WXS schemas
        SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

        // load a WXS schema, represented by a Schema instance
        Schema schema;
        try {
            schema = factory.newSchema(schemaFile);
        } catch (SAXException e) {
            return new ValidationResult("Unable to parse XSD schema", ValidationResult.EXCEPTION, schemaFile, e);
        }

        // create a Validator instance, which can be used to validate an
        // instance document
        Validator validator = schema.newValidator();

        // validate the DOM tree
        try {
            validator.validate(new DOMSource(document));
        } catch (SAXException e) {
            return new ValidationResult("Schema does not match the example", ValidationResult.INVALID, schemaFile, e);
        } catch (IOException e) {
            return new ValidationResult("Exception", ValidationResult.EXCEPTION, schemaFile, e);
        }
        return new ValidationResult("Success", ValidationResult.SUCCESS, schemaFile, null);
    }

}
