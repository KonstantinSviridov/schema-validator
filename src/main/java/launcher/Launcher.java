package launcher;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import com.mulesoft.schema.validator.SchemaValidator;
import com.mulesoft.schema.validator.ValidationResult;

public class Launcher {


    private static final int EXAMPLES_MODE = 0;
    private static final int SCHEMAS_MODE = 1;


    public static void main(String[] args) {
        
        HashMap<String,String> argsMap = parseArgs(args);
        
        String schemasFolderPath = argsMap.get("schemasFolder");
        String examplesFolderPath = argsMap.get("examplesFolder");
        String logsFolderPath = argsMap.get("outputFolder");
        
        execute(schemasFolderPath, examplesFolderPath,logsFolderPath);
    }

    private static void execute(String schemasFolderPath, String examplesFolderPath, String logsFolderPath)    
    {
        
        File schemasFolder = new File(schemasFolderPath);
        File examplesFolder = new File(examplesFolderPath);
        File outputFolder = new File(logsFolderPath);
        
        cleanFolder(outputFolder);
        
        DataContainer container = gatherData(schemasFolder, examplesFolder);
        
        List<Document> jsonSchemas = container.getSchemasList();
        for(Document doc : jsonSchemas){

            File schemaFile = doc.getFile();
            
            Document exampleDocument = container.getExampleForSchema(doc);
            if(exampleDocument==null){
                continue;
            }
            File exampleFile = exampleDocument.getFile();
            ValidationResult result = null;

            result = validate(schemaFile, exampleFile, outputFolder);

            if(!result.success()){
                Utils.writeLog(result,outputFolder);
            }
        }
    }

    

    private static void cleanFolder(File folder) {
        
        if(!folder.isDirectory()){
            return;
        }
        
        File[] listFiles = folder.listFiles();
        for(File f : listFiles){
            f.delete();
        }        
    }

    private static ValidationResult validate(File schemaFile, File exampleFile, File logsFolder) {
        
        String schemaString = null;
        try {
            schemaString = Utils.getContent(schemaFile);
        } catch (IOException e1) {
            return new ValidationResult("Unable to read file", ValidationResult.EXCEPTION, schemaFile, e1);
        }
        
        String exampleString = null;
        try {
            exampleString = Utils.getContent(exampleFile);
        } catch (IOException e1) {
            return new ValidationResult("Unable to read file", ValidationResult.EXCEPTION, exampleFile, e1);
        }
        
        if(schemaString.equals(exampleString)){
            return new ValidationResult("", ValidationResult.SUCCESS, schemaFile, null);
        }

        
        ValidationResult result = new SchemaValidator().validate(schemaFile,exampleFile);
        return result;        
    }
    
    private static DataContainer gatherData(File schemasFolder, File examplesFolder)
    {
        DataContainer container = new DataContainer();
        gatherData(schemasFolder,container,SCHEMAS_MODE);        
        gatherData(examplesFolder,container,EXAMPLES_MODE);
        return container;
    }

    private static void gatherData(File folder, DataContainer container, int mode)
    {
        if(folder.isDirectory()){
            File[] listFiles = folder.listFiles();
            for(File f : listFiles){
                
                String name = getName(f);              
                
                if(mode == EXAMPLES_MODE){
                    container.registerExamplpe(name, f);
                    if(name.startsWith("_")){
                        container.registerExamplpe(name.substring(1), f);
                    }
                }
                else if(mode == SCHEMAS_MODE){
                    container.registerSchema(name, f);
                }
            }
        }
    }

    private static String getName(File f) {
        String name = f.getName();
        int ind = name.lastIndexOf('.');
        if(ind>0){
            name = name.substring(0, ind);
        }
        return name;
    }
    

    private static HashMap<String, String> parseArgs(String[] args) {
        HashMap<String,String> map = new HashMap<String, String>();
        for(int i = 0 ; i < args.length ; i+=2){
            String key = args[i];
            if(key.startsWith("-")){
                key=key.substring(1);
            }
            if(i<args.length-1){
                String value = args[i+1];
                map.put(key, value);
            }
        }
        return map;
    }

}
