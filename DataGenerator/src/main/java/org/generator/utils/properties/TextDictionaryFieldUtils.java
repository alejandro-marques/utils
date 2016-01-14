package org.generator.utils.properties;

import org.generator.configuration.CommonData;
import org.generator.constants.PropertiesConstants;
import org.generator.model.data.Dictionary;
import org.generator.model.data.Field;
import org.generator.model.data.Property;
import org.generator.model.data.Property.Value;

import java.util.Map;

public class TextDictionaryFieldUtils {

    public static String getDictionaryFieldValue(Field field, Object previousValue) throws Exception {
        String previousStringValue = null;
        boolean limit = Boolean.parseBoolean(field.getParameters().get(PropertiesConstants.LIMIT));
        String dictionaryName = field.getParameters().get(PropertiesConstants.FILE);
        Dictionary dictionary = CommonData.getDictionary(dictionaryName);

        if (null == dictionary){throw new Exception ("Dictionary " + dictionaryName + " not found.");}

        if (null != previousValue){
            if (previousValue instanceof String){previousStringValue = (String) previousValue;}
            else {throw new Exception (previousValue.toString() + " is not a valid String");}
        }

        return getValue(null == previousStringValue? field.getValue() : field.getVariation(),
                previousStringValue, dictionary, limit);
    }

    public static String getValue (Map<String, String> parameters, String previousValue,
            Dictionary dictionary, boolean limit) throws Exception {
        String typeString = parameters.get(PropertiesConstants.TYPE);
        Value type = Property.getEnum(Property.Value.class, typeString);

        switch (type){
            case FIXED:
                return getFixedValue(parameters, dictionary);

            case RANDOM:
                return getRandomValue(dictionary);

            case SEQUENTIAL:
                return getSequentialValue(parameters, previousValue, dictionary, limit);

            default:
                throw new Exception("String type \"" + typeString + "\" not implemented yet.");
        }
    }

    private static String getFixedValue (Map<String, String> parameters, Dictionary dictionary)
            throws Exception {
        int position = Integer.parseInt(parameters.get(PropertiesConstants.POSITION));
        return dictionary.getWord(position);
    }

    private static String getRandomValue (Dictionary dictionary)
            throws Exception {
        return dictionary.getRandomWord();
    }

    private static String getSequentialValue(Map<String, String> parameters, String previousValue,
            Dictionary dictionary, boolean limit) throws Exception {
        return dictionary.getNextWord(previousValue, limit);
    }
}
