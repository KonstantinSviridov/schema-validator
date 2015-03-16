This project provides tool for validating JSON and XML examples against schemas.

#Installation

Checkout the project and run `mvn clean install`.

#Running validation

##Preparing data

Locate all schemas and examples to be checked in `schemasFolder` and `examplesFolder` respectively. Each example must have the same name as the corresponding schema, or it may be named as `_{schemaName}`.

##Launching validator

In the `target` subdirectory you can find a jar with complete dependencies. Run it the following way:
```
java -cp {jarName} launcher.Launcher -schemasFolder {absolute path to schemasFolder} -examplesFolder {absolute path to exampleFolder} -outputFolder {absolute path to outputFolder}
``` 

#Validation results
Successful validation does not produce any output. If validation has crashed or finished negatively, the result is stored in `{schemaName}.txt` file inside `outputFolder`.

##Incorrect validation output
In some cases validation process reports fake errors. The only known case is that for JSON `null` is not accepted as `Object`.


#Future work
Schema validation facility is supposed to be integrated into API Notebook.