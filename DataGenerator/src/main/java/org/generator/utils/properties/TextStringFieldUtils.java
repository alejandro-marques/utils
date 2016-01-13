package org.generator.utils.properties;

import org.generator.constants.PropertiesConstants;
import org.generator.model.data.Field;
import org.generator.model.data.Property;
import org.generator.model.data.Property.Value;

import java.util.Map;

public class TextStringFieldUtils {

    public static String getStringFieldValue(Field field, Object previousValue) throws Exception {
        String value;

        if (null != previousValue){
            if (previousValue instanceof String){value = (String) previousValue;}
            else {throw new Exception (previousValue.toString() + " is not a valid String");}

            value = getValue(field.getVariation(), value);
        }
        else{value = getValue(field.getValue(), null);}

        return value;
    }

    public static String getValue (Map<String, String> parameters, String previousValue) throws Exception {
        String typeString = parameters.get(PropertiesConstants.TYPE);
        Value type = Property.getEnum(Value.class, typeString);

        switch (type){
            case FIXED:
                return getFixedValue(parameters);

            default:
                throw new Exception("String type \"" + typeString + "\" not implemented yet.");
        }
    }

    private static String getFixedValue (Map<String, String> parameters) throws Exception {
        return parameters.get(PropertiesConstants.VALUE);
    }
}
