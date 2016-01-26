package org.generator.utils.values;

import org.generator.configuration.CommonData;
import org.generator.constants.PropertiesConstants;
import org.generator.model.configuration.FieldValueInfo;
import org.generator.model.data.Dictionary;
import org.generator.model.configuration.FieldValueDefinition;
import org.generator.model.configuration.FieldValueDefinition.Mode;
import org.generator.model.data.FieldValue;
import org.generator.model.data.Word;

import java.util.Map;

public class TextDictionaryValueUtils {

    public static FieldValue getDictionaryFieldValue(FieldValueInfo fieldValueInfo, FieldValue previousValue,
            Map<String, FieldValue> relatedValues) throws Exception {
        boolean limit = Boolean.parseBoolean(fieldValueInfo.getOptions().get(PropertiesConstants.LIMIT));
        return getValueFromDictionaryFieldValue(fieldValueInfo, previousValue, relatedValues, limit);
    }

    /*public static String getRelationFieldValue(ValueInfo valueInfo, Object previousValue) throws Exception {
        String relationName = valueInfo.getOptions().get(PropertiesConstants.RELATION);

        if (null == relationName && null == previousValue){
            return getDictionaryFieldValue(valueInfo, previousValue);
        }
        if (null == relationName){relationName = previousValue.toString();}
        boolean limit = Boolean.parseBoolean(valueInfo.getOptions().get(PropertiesConstants.LIMIT));
        String dictionaryName = valueInfo.getOptions().get(PropertiesConstants.FILE);
        Map<String, Dictionary> relation = CommonData.getRelation(dictionaryName);
        if (null == relation){throw new Exception ("Relation " + dictionaryName + " not found.");}
        Dictionary dictionary = relation.get(relationName);
        if (null == dictionary){throw new Exception ("Relation for " + relationName + " not found in " + dictionaryName + ".");}

        return getValueFromDictionaryFieldValue(valueInfo, previousValue, limit);
    }*/

    public static FieldValue getValueFromDictionaryFieldValue(FieldValueInfo fieldValueInfo,
            FieldValue previousValue, Map<String, FieldValue> relatedValues, boolean limit) throws Exception {
        String previousStringValue = null;
        if (null != previousValue){
            if (previousValue.getValue() instanceof String){previousStringValue = (String) previousValue.getValue();}
            else {throw new Exception (previousValue.toString() + " is not a valid String");}
        }

        Map<String, String> parameters = null == previousStringValue?
                fieldValueInfo.getInitial() : fieldValueInfo.getVariation();
        return getValue(parameters, previousStringValue, relatedValues, limit);
    }

    public static FieldValue getValue (Map<String, String> parameters, String previousValue,
            Map<String, FieldValue> relatedValues, boolean limit) throws Exception {
        String modeName = parameters.get(PropertiesConstants.MODE);
        Mode mode = FieldValueDefinition.getEnum(Mode.class, modeName);

        Dictionary dictionary = getDictionary(parameters, previousValue, relatedValues);
        Word word;

        if (null == mode) {
            throw new Exception("Not supported dictionary mode \"" + modeName + "\".");
        }

        switch (mode){
            case FIXED:
                word = getFixedValue(parameters, dictionary);
                break;

            case RANDOM:
                word = getRandomValue(dictionary);
                break;

            case SEQUENTIAL:
                word = getSequentialValue(previousValue, dictionary, limit);
                break;

            default:
                throw new Exception("String type \"" + modeName + "\" not implemented yet.");
        }
        FieldValue value = new FieldValue(null != word? word.getValue() : null);
        value.addProperty(PropertiesConstants.WEIGHT, getRelativeWeightWord(word, dictionary));

        return value;
    }

    private static Double getRelativeWeightWord (Word word, Dictionary dictionary){
        if (null == word) {return 0.0;}
        return word.getWeight() / dictionary.getMaxWeight();
    }

    public static Dictionary getDictionary (Map<String, String> parameters, String previousValue,
            Map<String, FieldValue> relatedValues) throws Exception {

        String parameter = parameters.get(PropertiesConstants.DICTIONARY);
        if (null != parameter){return getDictionary(parameter);}
        parameter = parameters.get(PropertiesConstants.RELATION);
        if (null != parameter){
            return getDictionaryFromRelation(parameter, parameters.get(PropertiesConstants.ORIGIN),
                    previousValue, relatedValues);
        }
        throw new Exception ("Wrong dictionary configuration. Missing relation or dictionary.");
    }

    private static Dictionary getDictionary(String dictionaryName) throws Exception {
        Dictionary dictionary = CommonData.getDictionary(dictionaryName);
        if (null == dictionary){
            throw new Exception ("Dictionary \"" + dictionaryName + "\" not found.");
        }
        return dictionary;
    }

    private static Dictionary getDictionaryFromRelation(String relationName, String origin,
            String previousValue, Map<String, FieldValue> relatedValues) throws Exception {

        Map<String, Dictionary> relation = CommonData.getRelation(relationName);
        if (null == relation){throw new Exception ("Relation \"" + relationName + "\" not found.");}

        String key = null;
        if (null == origin){key = previousValue;}
        else {
            FieldValue originValue = relatedValues.get(origin);
            if (null != originValue) {key = originValue.getValue().toString();}
        }
        if (null == key){
            throw new Exception("Value used as key for relation \"" + relationName + "\" is null.");
        }

        Dictionary dictionary = relation.get(key);
        if (null == dictionary){
            throw new Exception ("Value used as key \"" + key + "\" not found " +
                    "for relation \"" + relationName + "\".");
        }
        return dictionary;
    }

    private static Word getFixedValue (Map<String, String> parameters, Dictionary dictionary)
            throws Exception {
        int position = Integer.parseInt(parameters.get(PropertiesConstants.POSITION));
        return dictionary.getWord(position);
    }

    private static Word getRandomValue (Dictionary dictionary)
            throws Exception {
        return dictionary.getRandomWord();
    }

    private static Word getSequentialValue(String previousValue,
            Dictionary dictionary, boolean limit) throws Exception {
        return dictionary.getNextWord(previousValue, limit);
    }
}
