package org.generator.processor;

import org.generator.configuration.ProcessConfigurator;
import org.generator.constants.FieldConstants;
import org.generator.constants.PropertiesConstants;
import org.generator.exception.LimitReachedException;
import org.generator.model.configuration.FieldInfo;
import org.generator.model.configuration.GeneratorInfo;
import org.generator.model.configuration.ProcessInfo;
import org.generator.model.data.FieldValue;
import org.generator.model.data.Transformation;
import org.generator.utils.CommonUtils;
import org.generator.utils.FormatUtils;
import org.generator.utils.RandomUtils;
import org.generator.utils.transformations.TransformationUtils;
import org.generator.utils.values.ValueUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Processor {

    private GeneratorInfo generatorInfo;
    private List<FieldInfo> model;
    private List<Transformation> transformations;

    private int maxDocuments;
    private int minRelatedDocuments;
    private int maxRelatedDocuments;

    private int currentDocument;
    private int currentRelatedDocument;
    private int relatedDocuments;
    private Map <Integer, Integer> idLevelVariation;

    private boolean hasAuxiliaryFields;
    private boolean weightedId;
    private boolean hasRelations;

    private Map<String, FieldValue> lastDocument = null;
    private Map<String, FieldValue> lastId = null;
    private Map<String, FieldValue> lastAuxiliaryFields = null;

    public Processor(String configurationFile, boolean resourceConfiguration)
            throws Exception {
        ProcessConfigurator processConfigurator = new ProcessConfigurator(configurationFile,
                resourceConfiguration);

        ProcessInfo configuration = processConfigurator.getProcessInfo();
        generatorInfo = configuration.getGenerator();
        model = configuration.getModel();
        transformations = configuration.getTransformations();

        initialize();
    }

    private void initialize (){
        maxDocuments = generatorInfo.getMaxDocuments();
        minRelatedDocuments = generatorInfo.getMinRelatedDocuments();
        maxRelatedDocuments = generatorInfo.getMaxRelatedDocuments();

        currentDocument = 0;
        currentRelatedDocument = 0;
        relatedDocuments = 0;
        idLevelVariation = new HashMap<>();

        hasAuxiliaryFields = generatorInfo.isAuxiliaryFields();
        weightedId = hasAuxiliaryFields && hasWeight();
        hasRelations = hasAuxiliaryFields &&
                minRelatedDocuments != maxRelatedDocuments || minRelatedDocuments > 1;
    }


    /**
     *  Generates the next random document based on the process configuration and previous one (if any)
     * @return  Next document as a key-value list
     * @throws Exception
     */
    public Map<String, Object> nextDocument () throws Exception {
        Map<String, FieldValue> document = new HashMap<>();

        // If there is a max documents limit and it has been reached null is returned
        if (-1 != maxDocuments && currentDocument >= maxDocuments){return null;}

        // If it is the first document that could be related with other ones the number of related documents is obtained
        if (0 == currentRelatedDocument){
            // Calculates how many related documents are going to be generated
            relatedDocuments = RandomUtils.getRandomInteger(minRelatedDocuments, maxRelatedDocuments);
            // Generates the base id for the first group of related documents (It is shared by every related document)
            try{lastId = generateId(generatorInfo.getIdFields(), lastId, document);}
            catch (LimitReachedException exception){return null;}
            catch (Exception exception){
                exception.printStackTrace();
                throw new Exception("ID Fields -> " + exception.getMessage());
            }
            // Generates auxiliary fields if requested
            try{lastAuxiliaryFields = generateAuxiliaryFields();}
            catch (Exception exception){
                throw new Exception("Auxiliary Fields -> " + exception.getMessage());
            }
        }

        // Fields which have the same value for every related document are added
        document.putAll(lastId);
        document.putAll(lastAuxiliaryFields);

        // An specific id for each related document is generated if necesary
        if (hasAuxiliaryFields){
            document.put(FieldConstants.COUNT_FIELD, new FieldValue(currentDocument));
            if (hasRelations) {
                String id = document.get(FieldConstants.ID_FIELD).getValue().toString() + "-"
                        + (currentRelatedDocument + 1);
                document.put(FieldConstants.ID_FIELD, new FieldValue(id));
            }
        }

        // Document values are generated
        try {document.putAll(generateDocument(currentRelatedDocument, document));}
        catch (Exception exception){
            throw new Exception("Model Fields -> " + exception.getMessage());
        }

        // Generated document is stored in order to use it for related documents which values are variations of this one
        lastDocument = document;
        currentDocument++;
        currentRelatedDocument++;

        // If max related documents have been reached a new non-related document marker is reset
        if (currentRelatedDocument >= relatedDocuments){currentRelatedDocument = 0;}

        try {return getValues(transformDocument(document));}
        catch (Exception exception){
            exception.printStackTrace();
            throw new Exception("Transformation -> " + exception.getMessage());
        }
    }

    private boolean hasWeight (){
        if (generatorInfo.getIdFields() != null){
            for (FieldInfo fieldInfo : generatorInfo.getIdFields()){
                if (null != fieldInfo.getOptions() && !fieldInfo.getOptions().isEmpty()){
                    if (Boolean.parseBoolean(fieldInfo.getOptions().get(PropertiesConstants.WEIGHT))) {
                        return true;
                    }
                }
            }
        }
        return false;
    }


    private Map<String, FieldValue> generateDocument (int variationCount,
            Map<String, FieldValue> document) throws Exception {
        return generateFields(model, variationCount, document);
    }

    private Map<String, FieldValue> generateId (List<FieldInfo> idFieldsInfo,
            Map<String, FieldValue> lastId, Map<String, FieldValue> document) throws Exception {
        // If no lastId is found that means this is the first time an id is generated so initial values are used
        if (null == lastId){
            // Variation counter is initialized for each id field level
            for (int level = 0; level < idFieldsInfo.size(); level++){
                idLevelVariation.put(level, 0);
            }
            // and id is generated by retrieving initial values for every id field level
            return generateFields(idFieldsInfo, 0, document);
        }

        /* Otherwise id is a variation of previous one. If it is a multi-field id it is structured as different levels. In some
                 * cases some id fields depend on the value of previous ones, so order maybe important. In those cases where
                 * order is important, the different fields are generating from higher priority fields (lesser order) to lower ones
                 * (higher order) but, when increasing id fields it must be done from lower to higher. Therefore, for each
                 * iteration of id variation one level is changed, starting from the level with lesser priority, if the variation limit
                 * is reached for that field, the next priority level is modified, and every level with lesser priority is reset. If every
                 * level reaches its limit no more ids can be generated and process finishes
                */
        int currentLevel = idFieldsInfo.size() - 1;
        int currentLevelVariation;
        FieldInfo currentFieldInfo;
        FieldValue lastValue;

        // A copy of the last id is generated in order to be modified
        Map<String, FieldValue> id = new HashMap<>();

        // While not every level has reached its limit values are modified
        while (currentLevel >= 0){
            try{
                // A reference id is created including every value for fields with higher priority than the current level
                for (int level = 0; level < currentLevel; level++){
                    String fieldName = idFieldsInfo.get(level).getName();
                    id.put(fieldName, lastId.get(fieldName));
                }

                // Retrieves the id field to be modified
                currentFieldInfo = idFieldsInfo.get(currentLevel);
                lastValue = lastId.get(currentFieldInfo.getName());
                // Retrieves the current level variation counter
                currentLevelVariation = idLevelVariation.get(currentLevel);
                // Tries to modify the last value, if it is possible the current level value is added
                id.put(currentFieldInfo.getName(), ValueUtils.getValue(
                        currentFieldInfo.getValue(), ++currentLevelVariation, lastValue, id));

                // And every level with lesser priority than the current one is reset to its initial value
                // Previous levels are reset to their initial values
                for (int level = currentLevel + 1; level < idFieldsInfo.size() ; level++) {
                    FieldInfo field = idFieldsInfo.get(level);
                    id.put(field.getName(), ValueUtils.getValue(field.getValue(), 0, null, id));
                    // And its variation counter is reset
                    idLevelVariation.put(level, 0);
                }

                // If current level variation was retrieved the level variation count is incresed
                idLevelVariation.put(currentLevel, currentLevelVariation);
                // And current level is set to the level with lesser priority
                currentLevel = idFieldsInfo.size() - 1;
                return id;
            }
            // If it wasn't possible to modify current level that means it has reached its limit, so next level (if any) is
            // marked as the one to be modified in the next iteration and any previous levels are reset
            catch (LimitReachedException exception){
                // Next level is marked as current level (If this was the last level there are no more levels to be modified
                // therefore as soon as this loop is executed again it will exit and throw an exception
                currentLevel--;
            }
        }
        throw new LimitReachedException("Last id already generated");
    }

    private Map<String, FieldValue> generateAuxiliaryFields () throws Exception {
        Map<String, FieldValue> auxiliaryFields = new HashMap<>();
        if (hasAuxiliaryFields){
            if (weightedId){
                auxiliaryFields.put(FieldConstants.WEIGHT_FIELD, new FieldValue(generateWeight(lastId)));
            }
            if (hasRelations){
                auxiliaryFields.put(FieldConstants.RELATED_DOCUMENTS_FIELD,
                        new FieldValue(relatedDocuments));
            }
            String id = CommonUtils.getStringFromMapValues(getValues(lastId), "-");
            auxiliaryFields.put(FieldConstants.ID_FIELD, new FieldValue(id));
        }
        return auxiliaryFields;
    }

    private Double generateWeight (Map<String, FieldValue> id) throws Exception {
        Double weight = 1.0;
        try {
            for (FieldInfo fieldInfo : generatorInfo.getIdFields()) {
                Map<String, String> parameters = fieldInfo.getOptions();
                if (null != parameters && Boolean.parseBoolean(parameters.get(PropertiesConstants.WEIGHT))) {
                    FieldValue value = id.get(fieldInfo.getName());
                    weight = weight * Double.parseDouble(
                            value.getProperties().get(PropertiesConstants.WEIGHT).toString());
                }
            }
        }
        catch (Exception exception){
            throw new Exception("Unable to get weight for id");
        }

        return FormatUtils.formatDouble(weight, 2);
    }


    private Map<String, FieldValue> transformDocument (Map<String, FieldValue> document) throws Exception {
        if (null == transformations || transformations.isEmpty()){return document;}

        Map<String, FieldValue> transformedDocument = duplicateDocument(document);
        for (Transformation transformation : transformations){
            TransformationUtils.transform(transformedDocument, transformation);
        }
        return transformedDocument;
    }

    private Map<String, FieldValue> generateFields (List<FieldInfo> fieldsInfo, int variationCount,
            Map<String, FieldValue> document) throws Exception {
        if (null != fieldsInfo) {
            for (FieldInfo fieldInfo : fieldsInfo) {
                String fieldName = fieldInfo.getName();
                try {
                    FieldValue lastValue = variationCount > 0 && null != lastDocument ?
                            lastDocument.get(fieldName) : null;
                    document.put(fieldName,  ValueUtils.getValue(
                            fieldInfo.getValue(), variationCount, lastValue, document));
                }
                catch (Exception exception){
                    throw new Exception("Field [" + fieldName + "] -> " + exception.getMessage());
                }
            }
        }
        return document;
    }

    private Map<String, Object> getValues (Map<String, FieldValue> extendedValues){
        HashMap<String, Object> values = new HashMap<>();
        for (Map.Entry<String, FieldValue> extendedValue : extendedValues.entrySet()){
            values.put(extendedValue.getKey(), extendedValue.getValue().getValue());
        }
        return values;
    }

    private Map<String, FieldValue> duplicateDocument (Map<String, FieldValue> document){
        if (null == document) {return null;}
        Map<String, FieldValue> newDocument = new HashMap<>();
        if (!document.isEmpty()){
            for (Entry<String, FieldValue> entry : document.entrySet()){
                newDocument.put(entry.getKey(), new FieldValue(entry.getValue().getValue()));
            }
        }
        return newDocument;
    }
}
