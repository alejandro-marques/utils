package org.generator.processor;

import org.generator.configuration.ProcessConfigurator;
import org.generator.exception.LimitReachedException;
import org.generator.model.configuration.GeneratorConfiguration;
import org.generator.model.configuration.ProcessConfiguration;
import org.generator.model.data.Field;
import org.generator.model.data.Transformation;
import org.generator.utils.RandomUtils;
import org.generator.utils.properties.FieldUtils;
import org.generator.utils.transformations.TransformationUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Process {

    private GeneratorConfiguration generatorConfiguration;
    private List<Field> model;
    private List<Transformation> transformations;

    private int maxDocuments;
    private int minRelatedDocuments;
    private int maxRelatedDocuments;

    private int currentDocument;
    private int currentRelatedDocument;
    private int relatedDocuments;

    private Map<String, Object> lastDocument = null;
    private Map<String, Object> lastId = null;


    public Process (String configurationFile, boolean resourceConfiguration)
            throws Exception {
        ProcessConfigurator processConfigurator = new ProcessConfigurator(configurationFile,
                resourceConfiguration);

        ProcessConfiguration configuration = processConfigurator.getProcessConfiguration();
        generatorConfiguration = configuration.getGenerator();
        model = configuration.getModel();
        transformations = configuration.getTransformations();

        initialize();
    }

    private void initialize (){
        maxDocuments = generatorConfiguration.getMaxDocuments();
        minRelatedDocuments = generatorConfiguration.getMinRelatedDocuments();
        maxRelatedDocuments = generatorConfiguration.getMaxRelatedDocuments();

        currentDocument = 0;
        currentRelatedDocument = 0;
        relatedDocuments = 0;
    }

    public Map<String, Object> nextDocument () throws Exception {
        boolean isVariation = false;
        Map<String, Object> document;

        // If there is a max documents limit and it has been reached null is returned
        if (-1 != maxDocuments && currentDocument >= maxDocuments){return null;}

        // If it is the first document that could be related with other ones the number of related documents is obtained
        if (0 == currentRelatedDocument){
            // Calculates how many related documents are going to be generated
            relatedDocuments = RandomUtils.getRandomInteger(minRelatedDocuments, maxRelatedDocuments);
            // Generates the base id for the first document (Which is going to be shared by every related document)
            try{lastId = generateId(generatorConfiguration.getId(), lastId);}
            catch (LimitReachedException exception){return null;}
        }
        // If it is a related document it is marked as having variation values
        else {isVariation = true;}

        // Document values are generated
        document = generateDocument(isVariation);
        // Id values are added
        document.putAll(lastId);
        // An id field formed by the id field values and the current related document counter is generated
        document.put("id", getIdValue());
        document.put("relatedDocuments", relatedDocuments);

        // Generated document is stored in order to use it for related documents which values are variations of this one
        lastDocument = document;
        currentDocument++;
        currentRelatedDocument++;

        // If max related documents have been reached a new non-related document marker is reset
        if (currentRelatedDocument >= relatedDocuments){currentRelatedDocument = 0;}

        transformDocument(document);
        return document;
    }


    private Map<String, Object> generateDocument (boolean isVariation) throws Exception {
        return generateFields(model, isVariation);
    }

    private Map<String, Object> generateId (List<Field> idFields, Map<String, Object> lastId)
            throws Exception {
        // If no lastId is found that means this is the first time an id is generated so initial values are used
        if (null == lastId){return generateFields(idFields, false);}

        // Otherwise id is a variation of previous one. If it is a multi-field id it is structured as different levels
        // For each iteration of id variation one level is changed and, if the limit is reached, the current level is reset
        // and the next level is modified. If every level reaches it limit no more ids can be generated and process finishes
        int currentLevel = 0;
        Field currentField;
        Object lastValue;

        // A copy of the last id is generated in order to be modified
        Map<String, Object> id = new HashMap<>();
        id.putAll(lastId);

        // While not every level has reached its limit values are modified
        while (currentLevel < idFields.size()){
            try{
                // Retrieves the id field to be modified
                currentField = idFields.get(currentLevel);
                lastValue = lastId.get(currentField.getName());
                // Tries to modify the last value, if it is possible the current level is reset and the id is returned
                id.put(currentField.getName(), FieldUtils.getFieldValue(currentField, lastValue));
                currentLevel = 0;
                return id;
            }
            // If it wasn't possible to modify current level that means it has reached its limit, so next level (if any) is
            // marked as the one to be modified in the next iteration and any previous levels are reset
            catch (LimitReachedException exception){
                // Previous levels are reset to their initial values
                for (int i = currentLevel; i >= 0 ; i--) {
                    currentField = idFields.get(i);
                    id.put(currentField.getName(), FieldUtils.getFieldValue(currentField, null));
                }
                // Next level is marked as current level (If this was the last level there are no more levels to be modified
                // therefore as soon as this loop is executed again it will exit and throw an exception
                currentLevel++;
            }
        }
        throw new LimitReachedException("Last id already generated");
    }


    private void transformDocument (Map<String, Object> document) throws Exception {
        for (Transformation transformation : transformations){
            TransformationUtils.transform(document, transformation);
        }
    }

    private Map<String, Object> generateFields (List<Field> fields, boolean isVariation)
            throws Exception {
        Map<String, Object> document = new HashMap<>();
        for (Field field : fields){
            String fieldName = field.getName();
            Object lastValue = isVariation && null != lastDocument? lastDocument.get(fieldName) : null;
            document.put(fieldName, FieldUtils.getFieldValue(field, lastValue));
        }
        return document;
    }

    private String getIdValue() throws Exception {
        StringBuilder builder = new StringBuilder();
        for(Entry<String, Object> entry : lastId.entrySet()){
            builder.append(entry.getValue()).append("-");
        }
        builder.append(currentRelatedDocument + 1);

        return builder.toString();
    }
}
