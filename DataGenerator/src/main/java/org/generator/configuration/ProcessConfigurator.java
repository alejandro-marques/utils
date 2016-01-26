package org.generator.configuration;

import com.google.gson.reflect.TypeToken;
import org.generator.model.configuration.ProcessInfo;
import org.generator.utils.FileUtils;
import org.generator.validator.ProcessValidator;

import java.io.File;

public class ProcessConfigurator {
    private ProcessInfo processInfo;

    public ProcessConfigurator(String configurationFile, boolean resourceConfiguration)
            throws Exception {
        CommonData.setResourceConfiguration(resourceConfiguration);

        if (!resourceConfiguration) {
            File file = new File(configurationFile);
            if (!file.exists()) {
                throw new Exception("Configuration file " + file.getAbsolutePath() + " not found");
            }
            else if (!file.isFile()) {
                throw new Exception("Configuration file " + file.getAbsolutePath() + " is not a valid file");
            }
            else {CommonData.setConfigurationPath(file.getParent() + "/");}
        }

        try {
            processInfo = FileUtils.getObjectFromJsonFile(configurationFile,
                new TypeToken<ProcessInfo>(){}.getType(), resourceConfiguration);
        }
        catch (Exception exception){
            throw new Exception ("Unable to load configuration (Reason: " +
                    exception.getMessage() + ")");
        }

        ProcessValidator.validate(processInfo);
    }

    public ProcessInfo getProcessInfo() {return processInfo;}
}
