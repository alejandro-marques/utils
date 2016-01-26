package org.generator.utils.values;

import org.generator.constants.PropertiesConstants;
import org.generator.model.configuration.FieldValueDefinition;
import org.generator.model.configuration.FieldValueDefinition.Mode;
import org.generator.model.configuration.FieldValueInfo;
import org.generator.model.data.FieldValue;

import java.util.Map;

public class TextStringValueUtils {

    public static FieldValue getStringFieldValue(FieldValueInfo fieldValueInfo, Object previousValue) throws Exception {
        String value;

        if (null != previousValue){
            if (previousValue instanceof String){value = (String) previousValue;}
            else {throw new Exception (previousValue.toString() + " is not a valid String");}

            value = getValue(fieldValueInfo.getVariation(), value);
        }
        else{value = getValue(fieldValueInfo.getInitial(), null);}

        return new FieldValue(value);
    }

    public static String getValue (Map<String, String> parameters, String previousValue) throws Exception {
        String modeName = parameters.get(PropertiesConstants.MODE);
        Mode mode = FieldValueDefinition.getEnum(Mode.class, modeName);

        switch (mode){
            case FIXED:
                return getFixedValue(parameters);

            default:
                throw new Exception("String type \"" + modeName + "\" not implemented yet.");
        }
    }

    private static String getFixedValue (Map<String, String> parameters) throws Exception {
        return parameters.get(PropertiesConstants.VALUE);
    }
}
