package com.mulesoft.schema.validator;

import java.io.File;

public class ValidationResult {
    
    public static final int SUCCESS = 0;
    
    public static final int INVALID = 1;
    
    public static final int EXCEPTION = 2;
    
    public ValidationResult(String message, int status, File file,
            Exception exception) {
        super();
        this.message = message;
        this.status = status;
        this.file = file;
        this.exception = exception;
    }

    private String message;
    
    private int status;
    
    private File file;
    
    private Exception exception;

    public String message() {
        return message;
    }

    public int status() {
        return status;
    }
    
    public boolean success(){
        return status == 0;
    }

    public Exception exception() {
        return exception;
    }

    public File file() {
        return file;
    }
    

}
