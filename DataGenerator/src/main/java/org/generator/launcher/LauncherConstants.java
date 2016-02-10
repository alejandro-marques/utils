package org.generator.launcher;

public interface LauncherConstants {

    // Outputs
    String CONSOLE = "console";
    String DATABASE = "database";
    String MONGODB = "mongodb";

    // Parameters
    String OUTPUT = "output";
    String HOST_PARAMETER = "host";
    String PORT_PARAMETER = "port";
    String DATABASE_PARAMETER = "db";
    String COLLECTION_PARAMETER = "collection";
    String PROCESS_PARAMETER = "process";

    // Parameter types
    String STRING_PARAMETER_TYPE = "string";
    String INTEGER_PARAMETER_TYPE = "integer";
    String FILE_PARAMETER_TYPE = "file";


    // Enums
    enum Mode {
        ANY("any"),
        CONSOLE (LauncherConstants.CONSOLE),
        DATABASE (LauncherConstants.DATABASE);

        public String name;

        Mode (String name){
            this.name = name;
        }
    }

    enum Parameter {

        MODEL (LauncherConstants.PROCESS_PARAMETER, LauncherConstants.FILE_PARAMETER_TYPE,
                true, null, OutputType.ANY),
        OUTPUT (LauncherConstants.OUTPUT, LauncherConstants.STRING_PARAMETER_TYPE,
                true, null, OutputType.ANY),

        MONGO_HOST (LauncherConstants.HOST_PARAMETER,
                LauncherConstants.STRING_PARAMETER_TYPE, false, "localhost", OutputType.MONGODB),
        MONGO_PORT (LauncherConstants.PORT_PARAMETER,
                LauncherConstants.INTEGER_PARAMETER_TYPE, false, "27017", OutputType.MONGODB),
        MONGO_DATABASE (LauncherConstants.DATABASE_PARAMETER,
                LauncherConstants.STRING_PARAMETER_TYPE, true, null, OutputType.MONGODB),
        MONGO_COLLECTION (LauncherConstants.COLLECTION_PARAMETER,
                LauncherConstants.STRING_PARAMETER_TYPE, true, null, OutputType.MONGODB);

        public String name;
        public String type;
        public boolean mandatory;
        public String defaultValue;
        public OutputType outputType;

        Parameter (String name, String type, boolean mandatory, String defaultValue,
                   OutputType outputType){
            this.name = name;
            this.type = type;
            this.mandatory = mandatory;
            this.defaultValue = defaultValue;
            this.outputType = outputType;
        }
    }

    enum OutputType {
        ANY ("any"),
        CONSOLE (LauncherConstants.CONSOLE),
        MONGODB (LauncherConstants.MONGODB);

        public String name;

        OutputType(String name){
            this.name = name;
        }

        public static OutputType getByName (String name){
            for (OutputType outputType : OutputType.values()){
                if (outputType.name.equals(name)){return outputType;}
            }
            return null;
        }
    }
}
