package launcher;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataContainer {
    
    public static final String JSON_EXTENSION = ".json";
    
    public static final String XML_EXTENSION = ".xml";
    
    public static final String XSD_EXTENSION = ".xsd";
    
    
    
    private HashMap<String,Document> jsonSchemas = new HashMap<String, Document>();
    private HashMap<String,Document> xmlSchemas = new HashMap<String, Document>();
    
    private HashMap<String,Document> jsonExamples = new HashMap<String, Document>();
    private HashMap<String,Document> xmlExamples = new HashMap<String, Document>();
    
    
    public List<Document> getSchemasList(){
        ArrayList<Document> list = new ArrayList<Document>();        
        list.addAll(jsonSchemas.values());
        list.addAll(xmlSchemas.values());
        return list;
    }
    
    public Document getExampleForSchema(Document schemaDocument){
        
        String namer = schemaDocument.getName();
        if(schemaDocument.isJson()){
            return jsonExamples.get(namer);
        }
        else if(schemaDocument.isXml()){
            return xmlExamples.get(namer);
        }
        else{
            return null;
        }
    }

    
    public void registerExamplpe(String name, File file){
        
        String fileName = file.getName().toLowerCase();
        if(fileName.endsWith(JSON_EXTENSION)){
            jsonExamples.put(name, new Document(name, file));
        }
        else if(fileName.endsWith(XML_EXTENSION)||fileName.endsWith(XSD_EXTENSION)){
            xmlExamples.put(name, new Document(name, file));
        }
    }
    
    public void registerSchema(String name, File file){
        
        String fileName = file.getName().toLowerCase();
        if(fileName.endsWith(JSON_EXTENSION)){
            jsonSchemas.put(name, new Document(name, file));
        }
        else if(fileName.endsWith(XML_EXTENSION)||fileName.endsWith(XSD_EXTENSION)){
            xmlSchemas.put(name, new Document(name, file));
        }
    }

}
