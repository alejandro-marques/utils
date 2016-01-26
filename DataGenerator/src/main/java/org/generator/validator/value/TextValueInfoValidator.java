package org.generator.validator.value;

import com.google.gson.reflect.TypeToken;
import org.generator.configuration.CommonData;
import org.generator.constants.PropertiesConstants;
import org.generator.model.configuration.FieldValueDefinition.Mode;
import org.generator.model.configuration.FieldValueDefinition.Subtype;
import org.generator.model.configuration.FieldValueInfo;
import org.generator.model.data.Dictionary;
import org.generator.model.data.Word;
import org.generator.model.data.WordRelation;
import org.generator.utils.FileUtils;
import org.generator.validator.Validator;
import org.generator.validator.Validator.ValueType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TextValueInfoValidator extends FieldValueInfoValidator {

    public static void validate (Subtype subtype, FieldValueInfo fieldValueInfo,
            List<String> previousFields) throws Exception {
        // General value parameters are checked
        validateOptions(fieldValueInfo.getOptions());

        // Initial parameters are mandatory, if not present an exception is thrown
        Map<String, String> initialParameters = fieldValueInfo.getInitial();
        if (null == initialParameters || initialParameters.isEmpty()) {
            throw new Exception("Missing mandatory initial value parameters");
        }
        validateValueParameters(subtype, initialParameters, false, previousFields);

        // Variation parameters are optional, therefore they are only checked if present
        Map<String, String> variationParameters = fieldValueInfo.getVariation();
        if (null != variationParameters && !variationParameters.isEmpty()) {
            validateValueParameters(subtype, variationParameters, true, previousFields);
        }
    }

    private static void validateOptions(Map<String, String> options)
            throws Exception {
        if (options != null && !options.isEmpty()){
            Validator.findAndCheckParameter(options, PropertiesConstants.LIMIT, ValueType.BOOLEAN, false);
        }
    }

    private static void validateValueParameters(Subtype subtype, Map<String, String> parameters,
            boolean isVariation, List<String> previousFields)
            throws Exception {
        try {
            String modeName = (String) Validator.findAndCheckParameter(parameters,
                    PropertiesConstants.MODE, ValueType.STRING, true);
            Mode mode = checkMode(modeName, subtype, isVariation);

            switch (subtype) {
                case STRING:
                    validateStringValueParameters(mode, parameters);
                    break;
                case DICTIONARY:
                    validateDictionaryValueParameters(mode, parameters, previousFields, isVariation);
                    break;
            }
        }
        catch (Exception exception){
            throw new Exception ((isVariation? "variation" : "initial") + " -> "
                    + exception.getMessage());
        }
    }

    private static void validateStringValueParameters(Mode mode, Map<String, String> parameters)
            throws Exception {
        switch (mode) {
            case FIXED:
                validateFixedString(parameters);
                break;
        }
    }

    private static void validateDictionaryValueParameters(Mode mode, Map<String, String> parameters,
            List<String> previousFields, boolean isVariation) throws Exception {
        switch (mode) {
            case FIXED:
                validateFixedDictionary(parameters, previousFields, isVariation);
                break;
            case RANDOM:
                validateRandomDictionary(parameters, previousFields, isVariation);
                break;
            case SEQUENTIAL:
                validateSequentialDictionary(parameters, previousFields, isVariation);
                break;
        }
    }

    private static void validateFixedString(Map<String, String> parameters) throws Exception {
        Validator.findAndCheckParameter(parameters, PropertiesConstants.VALUE, ValueType.STRING, true);
    }

    private static void validateFixedDictionary(Map<String, String> parameters,
            List<String> previousFields, boolean isVariation) throws Exception {
        Validator.findAndCheckParameter(parameters, PropertiesConstants.POSITION, ValueType.STRING, true);
        validateRelationOrDictionary(parameters, previousFields, isVariation);
    }

    private static void validateRandomDictionary(Map<String, String> parameters,
            List<String> previousFields, boolean isVariation) throws Exception {
        validateRelationOrDictionary(parameters, previousFields, isVariation);
    }

    private static void validateSequentialDictionary(Map<String, String> parameters,
            List<String> previousFields, boolean isVariation) throws Exception {
        validateRelationOrDictionary(parameters, previousFields, isVariation);
    }

    private static void validateRelationOrDictionary (Map<String, String> parameters,
            List<String> previousFields, boolean isVariation) throws Exception {
        String dictionary = (String) Validator.findAndCheckParameter(parameters,
                PropertiesConstants.DICTIONARY, ValueType.STRING, false);
        String relation = (String) Validator.findAndCheckParameter(parameters,
                PropertiesConstants.RELATION, ValueType.STRING, false);

        if (null == dictionary && null == relation){
            throw new Exception("Either a dictionary or a relation file must be defined");
        }

        if (dictionary != null){
            if (null == CommonData.getDictionary(dictionary)) {
                CommonData.addDictionary(dictionary, getDictionary(dictionary));
            }
        }

        if (relation != null){
            if (null == CommonData.getRelation(relation)) {
                CommonData.addRelation(relation, getRelation(relation));
            }
            String origin = (String) Validator.findAndCheckParameter(parameters,
                    PropertiesConstants.ORIGIN, ValueType.STRING, false);
            if((null != origin) && ((null == previousFields) || !previousFields.contains(origin))){
                throw new Exception("In order to relate this field with \"" + origin + "\" it must " +
                        "be processed after it. Field order must be checked.");
            }
            if (null == origin && !isVariation){
                throw new Exception("Relation value is only allowed for initial values if a " +
                        "previously processed field is set as origin");
            }
        }
    }

    private static Dictionary getDictionary(String dictionaryName) throws Exception {
        // If file is not a resource the configuration path is added
        String dictionaryFile = (CommonData.isResourceConfiguration() ?
                "" : CommonData.getConfigurationPath()) + dictionaryName;
        // The list of words for that dictionary is retrieved
        List<Word> words = FileUtils.getObjectFromJsonFile(dictionaryFile,
                new TypeToken<List<Word>>(){}.getType(), CommonData.isResourceConfiguration());

        // And added to a dictionary object
        Dictionary dictionary = new Dictionary(dictionaryName);
        for (Word word : words) {dictionary.add(word);}
        return dictionary;
    }

    private static Map<String, Dictionary> getRelation(String relationName) throws Exception {
        // If file is not a resource the configuration path is added
        String relationFile = (CommonData.isResourceConfiguration() ?
                "" : CommonData.getConfigurationPath()) + relationName;
        // The list of words for that relation is retrieved
        List<WordRelation> wordRelations = FileUtils.getObjectFromJsonFile(relationFile,
                new TypeToken<List<WordRelation>>(){}.getType(), CommonData.isResourceConfiguration());

        // And added to a relation object
        Map<String, Dictionary> relation = new HashMap<>();
        for (WordRelation word : wordRelations) {
            if (null != word.getNext()) {
                Dictionary dictionary = new Dictionary(relationName);
                for (Word nextWord : word.getNext()) {dictionary.add(nextWord);}
                relation.put(word.getValue(), dictionary);
            }
        }
        return relation;
    }
}