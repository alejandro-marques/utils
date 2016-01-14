package org.generator.launcher;

import org.generator.connector.DatabaseConnector;
import org.generator.connector.MongoDBConnector;
import org.generator.processor.Process;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

public class MongoDBIndexer {

    enum Parameter {
        HOST (LauncherConstants.HOST_PARAMETER, LauncherConstants.STRING_PARAMETER_TYPE, false, "localhost"),
        PORT (LauncherConstants.PORT_PARAMETER, LauncherConstants.INTEGER_PARAMETER_TYPE, false, "27017"),
        DATABASE (LauncherConstants.DATABASE_PARAMETER, LauncherConstants.STRING_PARAMETER_TYPE, true, null),
        COLLECTION (LauncherConstants.COLLECTION_PARAMETER, LauncherConstants.STRING_PARAMETER_TYPE, true, null),
        MODEL (LauncherConstants.PROCESS_PARAMETER, LauncherConstants.FILE_PARAMETER_TYPE, true, null);

        String name;
        String type;
        boolean mandatory;
        String defaultValue;

        Parameter (String name, String type, boolean mandatory, String defaultValue){
            this.name = name;
            this.type = type;
            this.mandatory = mandatory;
            this.defaultValue = defaultValue;
        }
    }

    private static final int MESSAGE_FREQUENCY = 10000;
    private static SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss.SSS");

    private static Process process;
    private static int documentCount = 0;
    private static long start;

    public static void main(String[] args) {
        try {
            start = System.currentTimeMillis();
            timeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            Map<String, String> parameters = validateParameters(validateParametersFormat(args));

            String processModel = parameters.get(LauncherConstants.PROCESS_PARAMETER);

            DatabaseConnector connector = new MongoDBConnector(
                    parameters.get(LauncherConstants.HOST_PARAMETER),
                    Integer.parseInt(parameters.get(LauncherConstants.PORT_PARAMETER)),
                    parameters.get(LauncherConstants.DATABASE_PARAMETER),
                    parameters.get(LauncherConstants.COLLECTION_PARAMETER));

            if (null != processModel){
                System.out.println("Retrieving process configuration from " + processModel);
                process = new Process(processModel, false);

            }

            try {
                Map<String, Object> document;
                while (null != (document = process.nextDocument())) {
                    if (documentCount % MESSAGE_FREQUENCY == 0){printProcess();}
                    connector.bufferedIndexMap(document);
                    documentCount++;
                }
            }
            catch (Exception exception){
                System.out.println("Error while generating documents (Reason: " + exception.getMessage() + ")");
                exception.printStackTrace();
            }

            printProcess();
            connector.flushBuffer();
            System.out.println("Process finished.");
        }
        catch (Exception exception){
            System.out.println("ERROR: " + exception.getMessage());
            printUsage();
        }
    }

    private static Map<String, String> validateParametersFormat(String[] args) throws Exception {
        Map<String, String> arguments = new HashMap<>();
        for (String argument : args){
            String[] argumentArray = argument.split("=");
            if ('-' != argument.charAt(0) || argumentArray.length != 2) {
                throw new Exception("Wrong argument format for " + argument + ". Expected format is \"-key=value\".");
            }
            arguments.put(argumentArray[0].substring(1), argumentArray[1]);
        }
        return arguments;
    }

    private static Map<String, String> validateParameters (Map<String, String> parameters) throws Exception {
        String value;
        String name;
        for (Parameter parameterValidator : Parameter.values()){
            name = parameterValidator.name;
            value = parameters.get(name);
            if (value == null) {
                if (parameterValidator.mandatory) {
                    throw new Exception("Missing mandatory parameter \"" + name + "\"");
                }
                else {parameters.put(name, parameterValidator.defaultValue);}
            }
            else {
                if (LauncherConstants.INTEGER_PARAMETER_TYPE.equals(parameterValidator.type)){
                    try {Integer.parseInt(value);}
                    catch (Exception exception){
                        throw new Exception(value + " is not a valid integer for parameter \"" + name + "\"");
                    }
                }
                else if (LauncherConstants.FILE_PARAMETER_TYPE.equals(parameterValidator.type)){
                    try {
                        File file = new File(value);
                        if (!file.exists()){
                            throw new Exception(file.getAbsolutePath() + " file does not exist for parameter \"" + name + "\"");
                        }
                    }
                    catch (Exception exception){
                        throw new Exception(value + " is not a valid file for parameter \"" + name + "\" " +
                                "(Reason: " + exception.getMessage() + ")");
                    }
                }
            }
        }
        return parameters;
    }

    public static void printUsage (){
        System.out.println("Usage:");
        System.out.print("java -jar MongoDBIndexer.jar ");
        for (Parameter parameter : Parameter.values()){
            System.out.print(parameter.mandatory? "" : "[");
            System.out.print("-" + parameter.name + "={");
            System.out.print(parameter.type + (parameter.mandatory? "" : ":" + parameter.defaultValue));
            System.out.print("}");
            System.out.print((parameter.mandatory? "" : "]") + " ");
        }
    }

    public static void printProcess(){
        long time = System.currentTimeMillis() - start;
        StringBuilder builder = new StringBuilder();
        builder.append(documentCount).append(" documents generated");
        builder.append(" (").append(getTimeFromMillis(time)).append(").");
        System.out.println(builder.toString());
    }

    public static String getTimeFromMillis(long time){
        Date date = new Date(time);
        return timeFormat.format(date);
    }
}
