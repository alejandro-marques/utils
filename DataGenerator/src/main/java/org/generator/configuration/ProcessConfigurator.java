package org.generator.configuration;

import com.google.gson.reflect.TypeToken;
import org.generator.constants.PropertiesConstants;
import org.generator.model.configuration.ProcessConfiguration;
import org.generator.model.data.Dictionary;
import org.generator.model.data.Field;
import org.generator.model.data.Property;
import org.generator.model.data.Property.Subtype;
import org.generator.model.data.Property.Type;
import org.generator.model.data.Word;
import org.generator.utils.FileUtils;

import java.io.File;
import java.util.*;

public class ProcessConfigurator {
    private String configurationPath;
    private boolean resourceConfiguration;

    private ProcessConfiguration processConfiguration;

    public ProcessConfigurator(String configurationFile, boolean resourceConfiguration)
            throws Exception {
        this.resourceConfiguration = resourceConfiguration;
        if (!resourceConfiguration) {
            File file = new File(configurationFile);
            if (!file.exists()) {
                throw new Exception("Configuration file " + file.getAbsolutePath() + " not found");
            }
            else if (!file.isFile()) {
                throw new Exception("Configuration file " + file.getAbsolutePath() + " is not a valid file");
            }
            else {configurationPath = file.getParent() + "/";}
        }

        processConfiguration = FileUtils.getObjectFromJsonFile(configurationFile, new TypeToken<ProcessConfiguration>(){}.getType(),
                resourceConfiguration);
        validate();
        initialize();
    }

    private void validate() throws Exception {}


    private void initialize() throws Exception {
        initializeDictionaries();
    }


    private void initializeDictionaries () throws Exception {
        // Every dictionary file is retrieved from configuration file (from both model and id)
        Set<String> dictionaries = getDictionaryNames(processConfiguration.getModel());
        dictionaries.addAll(getDictionaryNames(getProcessConfiguration().getGenerator().getId()));

        for (String dictionaryName : dictionaries){
            if (null != dictionaryName) {
                // If file is not a resource the configuration path is added
                String dictionaryFile = (resourceConfiguration ? "" : configurationPath) +
                        dictionaryName;
                // The list of words for that dictionary is retrieved
                List<Word> words = FileUtils.getObjectFromJsonFile(dictionaryFile,
                        new TypeToken<List<Word>>() {
                        }.getType(), resourceConfiguration);

                // And added to a dictionary object
                Dictionary dictionary = new Dictionary(dictionaryName);
                for (Word word : words) {dictionary.add(word.getWeight(), word.getValue());}
                // Each dictionary is added to a CommonData object so it could be used from any class
                CommonData.addDictionary(dictionaryName, dictionary);
            }
        }
    }

    private Set<String> getDictionaryNames (List<Field> fields){
        Set<String> dictionaryNames = new HashSet<>();
        for (Field field : fields) {
            // If the field type is text and subtype dictionary, it must have a "file" property with the dictionary file name
            if (Type.TEXT == Property.getEnum(Type.class, field.getType()) &&
                    Subtype.DICTIONARY == Property.getEnum(Subtype.class, field.getSubtype())) {
                Map<String, String> parameters = field.getParameters();
                dictionaryNames.add(parameters.get(PropertiesConstants.FILE));
            }
        }
        return dictionaryNames;
    }

    public ProcessConfiguration getProcessConfiguration() {return processConfiguration;}
}
