package org.generator.launcher;

import org.generator.launcher.LauncherConstants.OutputType;
import org.generator.launcher.LauncherConstants.Parameter;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class LauncherUtils {

    public static Map<String, String> validateParametersFormat(String[] args) throws Exception {
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

    public static Map<String, String> validateParameters (Map<String, String> parameters,
            OutputType outputType) throws Exception {
        String value;
        String name;
        for (Parameter parameterValidator : Parameter.values()){
            if (outputType != parameterValidator.outputType){continue;}

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
                        throw new Exception(value + " is not a valid integer for parameter \""
                                + name + "\"");
                    }
                }
                else if (LauncherConstants.FILE_PARAMETER_TYPE.equals(parameterValidator.type)){
                    if (!value.endsWith(".json")){
                        throw new Exception(value + " is not a valid file for parameter \""
                                + name + "\" " + "(Reason: Json file expected)");
                    }
                }
            }
        }
        return parameters;
    }

    public static void printUsage (){
        System.out.println("Usage:");
        System.out.print("\tjava -jar data-generator.jar ");
        Map<OutputType, String> parametersByOutput = new HashMap<>();
        String previousParameters;

        for (Parameter parameter : Parameter.values()){
            previousParameters = parametersByOutput.get(parameter.outputType);
            if(null == previousParameters){previousParameters = getParameterUsage(parameter);}
            else {previousParameters = previousParameters + getParameterUsage(parameter);}
            parametersByOutput.put(parameter.outputType, previousParameters);
        }
        if (null != parametersByOutput.get(OutputType.ANY)) {
            System.out.println(parametersByOutput.remove(OutputType.ANY));
        }
        if (!parametersByOutput.isEmpty()) {
            System.out.println("\n\tAditional parameters depending on output: ");
            for (Entry<OutputType, String> outputParameters : parametersByOutput.entrySet()) {
                System.out.println("\t\t " + outputParameters.getKey().name + ": " +
                        outputParameters.getValue());
            }
        }
    }

    private static String getParameterUsage (Parameter parameter){
        StringBuilder builder = new StringBuilder();
        builder.append(parameter.mandatory? "" : "[");
        builder.append("-").append(parameter.name);
        builder.append("={");
        builder.append(parameter.type);
        builder.append(parameter.mandatory? "" : ":" + parameter.defaultValue);
        builder.append("}");
        builder.append(parameter.mandatory? "" : "]");
        builder.append(" ");
        return builder.toString();
    }
}
