package org.generator.launcher;

import org.generator.launcher.LauncherConstants.Format;
import org.generator.launcher.LauncherConstants.OutputType;
import org.generator.launcher.output.ConsoleOutput;
import org.generator.launcher.output.HdfsOutput;
import org.generator.launcher.output.MongoDbOutput;
import org.generator.launcher.output.Output;
import org.generator.processor.Processor;
import org.generator.utils.FormatUtils;

import java.util.Map;

public class Launcher {

    private static final int MESSAGE_FREQUENCY = 10000;
    private static final String SEPARATOR = "------------------------------------------------";

    private static int documentCount = 0;
    private static long start;

    private static boolean innerResources = false;

    private static Processor processor;
    private static Output output;
    private static boolean processMessages;

    public static void main(String[] args) {
        try {
            start = System.currentTimeMillis();

            Map<String, String> parameters = LauncherUtils.validateParametersFormat(args);
            innerResources = parameters.containsKey("inner");
            parameters = LauncherUtils.validateParameters(parameters, OutputType.ANY);

            Format format = Format.getByName(parameters.get(LauncherConstants.FORMAT));

            setModel(parameters);
            setOutput(parameters);

            System.out.println("\n" + SEPARATOR + "\n");

            try {
                Map<String, Object> document;
                while (null != (document = processor.nextDocument())) {
                    if (processMessages && documentCount % MESSAGE_FREQUENCY == 0){printProcess();}
                    output.write(document, format);
                    documentCount++;
                }
                output.flush();
                output.close();
            }
            catch (Exception exception){
                System.out.println("Error while generating documents (Reason: "
                        + exception.getMessage() + ")");
                exception.printStackTrace();
            }

            System.out.println("\n" + SEPARATOR + "\n");

            printProcess();
            System.out.println("Process finished.");

        }
        catch (Exception exception){
            System.out.println("ERROR: " + exception.getMessage());
            LauncherUtils.printUsage();
        }
    }


    private static void setModel (Map<String, String> parameters) throws Exception {
        String processModel = parameters.get(LauncherConstants.PROCESS_PARAMETER);
        System.out.println("Retrieving process configuration from \"" + processModel + "\"");
        processor = new Processor(processModel, innerResources);
    }


    private static void setOutput (Map<String, String> parameters) throws Exception {
        String outputName = parameters.get(LauncherConstants.OUTPUT);
        OutputType outputType = OutputType.getByName(outputName);
        if (null == outputType){
            throw new Exception("Not supported output \"" + outputName + "\"");
        }
        System.out.println("Retrieving output configuration for \"" + outputName + "\"");
        switch (outputType){

            case CONSOLE:
                output = new ConsoleOutput();
                processMessages = false;
                break;

            case MONGODB:
                output = new MongoDbOutput(parameters);
                processMessages = true;
                break;

            case HDFS:
                output = new HdfsOutput(parameters);
                processMessages = true;
                break;

            default:
                throw new Exception("No output has been set.");
        }
    }


    private static void printProcess(){
        long time = System.currentTimeMillis() - start;
        StringBuilder builder = new StringBuilder();
        builder.append(documentCount).append(" documents generated");
        builder.append(" (Elapsed time: ").append(FormatUtils.getTimeFromMillis(time)).append(").");
        System.out.println(builder.toString());
    }
}
