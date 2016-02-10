package org.generator.launcher.output;

import org.generator.connector.DatabaseConnector;
import org.generator.connector.MongoDBConnector;
import org.generator.launcher.LauncherConstants;
import org.generator.launcher.LauncherConstants.OutputType;
import org.generator.launcher.LauncherUtils;

import java.util.Map;

public class MongoDbOutput implements Output{

    private DatabaseConnector connector;

    public MongoDbOutput (Map<String, String> parameters) throws Exception {

        LauncherUtils.validateParameters(parameters, OutputType.MONGODB);

        connector = new MongoDBConnector(
            parameters.get(LauncherConstants.HOST_PARAMETER),
            Integer.parseInt(parameters.get(LauncherConstants.PORT_PARAMETER)),
            parameters.get(LauncherConstants.DATABASE_PARAMETER),
            parameters.get(LauncherConstants.COLLECTION_PARAMETER));
    }

    @Override
    public void write(Map<String, Object> document) throws Exception {
        connector.bufferedIndexMap(document);
    }

    @Override
    public void flush() throws Exception {
        connector.flushBuffer();
    }
}
