package launcher;

import java.io.File;

public class Document {
    
    public Document(String name, File file) {
        super();
        this.name = name;
        this.file = file;
    }

    private String name;
    
    private File file;

    public String getName() {
        return name;
    }

    public File getFile() {
        return file;
    }
    
    public boolean isJson(){
        return file.getName().toLowerCase().endsWith(DataContainer.JSON_EXTENSION);
    }
    
    public boolean isXml(){
        String fName = file.getName().toLowerCase();
        return fName.endsWith(DataContainer.XML_EXTENSION) || fName.endsWith(DataContainer.XSD_EXTENSION);
    }

}
