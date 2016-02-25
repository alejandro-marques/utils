package org.generator.utils.values;

import org.generator.configuration.CommonData;
import org.generator.constants.PropertiesConstants;
import org.generator.exception.LimitReachedException;
import org.generator.model.configuration.FieldValueInfo;
import org.generator.model.data.Dictionary;
import org.generator.model.configuration.FieldValueDefinition;
import org.generator.model.configuration.FieldValueDefinition.Mode;
import org.generator.model.configuration.FieldValueDefinition.DictionaryType;
import org.generator.model.data.FieldValue;
import org.generator.model.data.Word;
import org.generator.utils.RandomUtils;

import java.util.Map;

public class TextDictionaryValueUtils {

    public static FieldValue getDictionaryFieldValue(FieldValueInfo fieldValueInfo,
            boolean isVariation, FieldValue previousValue, Map<String, FieldValue> relatedValues)
            throws Exception {
        // Checks whether the number of values to be returned is limited or not
        boolean limit = Boolean.parseBoolean(fieldValueInfo.getOptions().get(PropertiesConstants.LIMIT));

        // Checks if there is a previous value to be taken into account
        String previousStringValue = null;
        if (isVariation && null != previousValue && null != previousValue.getValue()){
            if (previousValue.getValue() instanceof String){
                previousStringValue = (String) previousValue.getValue();
            }
            else {throw new Exception (previousValue.getValue() + " is not a valid String");}
        }

        // If it is a variation the variation value is used, otherwise it is used the initial value
        return getValue(isVariation? fieldValueInfo.getVariation() : fieldValueInfo.getInitial(),
                previousStringValue, relatedValues, limit);
    }


    public static FieldValue getValue (Map<String, String> parameters, String previousValue,
            Map<String, FieldValue> relatedValues, boolean limit)
            throws Exception {
        String modeName = parameters.get(PropertiesConstants.MODE);
        Mode mode = FieldValueDefinition.getEnum(Mode.class, modeName);
        boolean isRelation = null != parameters.get(PropertiesConstants.RELATION);

        Dictionary dictionary = getDictionary(parameters, previousValue, relatedValues);
        Word word;

        if (null == mode) {
            throw new Exception("Not supported dictionary mode \"" + modeName + "\".");
        }

        switch (mode){
            case FIXED:
                word = getFixedValue(parameters, dictionary, isRelation);
                break;

            case RANDOM:
                word = getRandomValue(dictionary, isRelation);
                break;

            case SEQUENTIAL:
                word = getSequentialValue(previousValue, dictionary, limit, isRelation);
                break;

            case PARAGRAPH:
                word = getParagraphValue(dictionary, parameters);
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
        if (null == dictionary) {return 1.0;}
        return word.getWeight() / dictionary.getMeanWeight();
    }

    public static Dictionary getDictionary (Map<String, String> parameters, String previousValue,
            Map<String, FieldValue> relatedValues) throws Exception {

        DictionaryType type = DictionaryType.DICTIONARY;;
        if (null != parameters.get(PropertiesConstants.RELATION)){
            type = DictionaryType.RELATION;
        }
        else if (null != parameters.get(PropertiesConstants.LANGUAGE)){
            type = DictionaryType.LANGUAGE;
        }

        switch (type){
            case RELATION:
                return getDictionaryFromRelation(parameters.get(PropertiesConstants.RELATION),
                        parameters.get(PropertiesConstants.ORIGIN), previousValue, relatedValues);

            case LANGUAGE:
                return CommonData.getLanguage(parameters.get(PropertiesConstants.LANGUAGE));

            default:
                return getDictionary(parameters.get(PropertiesConstants.DICTIONARY));
        }
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
            if (null != originValue && null != originValue.getValue()) {
                key = originValue.getValue().toString();
            }
        }

        if (null != key){
            Dictionary dictionary = relation.get(key);
            if (null != dictionary){return dictionary;}

        }
        throw new Exception("No relation found for " + key);
    }

    private static Word getFixedValue (Map<String, String> parameters, Dictionary dictionary,
            boolean isRelation) throws Exception {
        if (isRelation && null == dictionary) {return new Word(null, 1.0);}
        int position = Integer.parseInt(parameters.get(PropertiesConstants.POSITION));
        return dictionary.getWord(position);
    }

    private static Word getRandomValue (Dictionary dictionary, boolean isRelation)
            throws Exception {
        if (isRelation && null == dictionary) {return new Word(null, 1.0);}
        return dictionary.getRandomWord();
    }

    private static Word getSequentialValue(String previousValue,
            Dictionary dictionary, boolean limit, boolean isRelation) throws Exception {
        if (isRelation && null == dictionary) {
            if (null == previousValue) {return new Word(null, 1.0);}
            throw new LimitReachedException(null, "Last word for relation already reached.");
        }
        return dictionary.getNextWord(previousValue, limit);
    }

    private static Word getParagraphValue(Dictionary dictionary, Map<String, String> parameters) throws Exception {
        int minWords = Integer.parseInt(parameters.get(PropertiesConstants.MIN_WORDS));
        int maxWords = Integer.parseInt(parameters.get(PropertiesConstants.MAX_WORDS));
        int minParagraphs = Integer.parseInt(parameters.get(PropertiesConstants.MIN_PARAGRAPHS));
        int maxParagraphs = Integer.parseInt(parameters.get(PropertiesConstants.MAX_PARAGRAPHS));
        return new Word(getParagraphs(dictionary, minParagraphs, maxParagraphs, minWords, maxWords), 1.0);
    }



    private static String getParagraphs (Dictionary dictionary, int minParagraphs, int maxParagraphs,
            int minWords, int maxWords) throws Exception {
        int paragraphs = RandomUtils.getRandomInteger(minParagraphs, maxParagraphs);
        StringBuilder builder = new StringBuilder();
        boolean first = true;
        for (int i = 0; i < paragraphs; i++){
            if (!first) {builder.append("\n");}
            builder.append(getParagraph(dictionary, minWords, maxWords));
            first = false;
        }
        return builder.toString();
    }



    private static String getParagraph (Dictionary dictionary, int minWords, int maxWords) throws Exception {
        int words = RandomUtils.getRandomInteger(minWords, maxWords);
        StringBuilder builder = new StringBuilder();
        boolean first = true;
        boolean capitalize = true;
        String word = "";
        for (int i = 0; i < words; i++){
            if (!first) {builder.append(" ");}
            word = dictionary.getRandomWord().getValue();
            if (capitalize){word = word.substring(0,1).toUpperCase() + word.substring(1);}
            builder.append(word);
            first = false;
            capitalize = word.endsWith(".");
        }
        if (!builder.toString().endsWith(".")){builder.append(".");}
        return builder.toString();
    }
}
