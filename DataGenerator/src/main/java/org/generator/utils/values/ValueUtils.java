package org.generator.utils.values;

import org.generator.constants.PropertiesConstants;
import org.generator.model.configuration.FieldValueInfo;
import org.generator.model.configuration.FieldValueDefinition;
import org.generator.model.configuration.FieldValueDefinition.Subtype;
import org.generator.model.configuration.FieldValueDefinition.Type;
import org.generator.model.data.FieldValue;

import java.util.Map;

public class ValueUtils {

    public static FieldValue getValue (FieldValueInfo fieldValueInfo, FieldValue previousValue,
            Map<String, FieldValue> relatedValues) throws Exception {
        Type type = FieldValueDefinition.getEnum(Type.class, fieldValueInfo.getType());
        Subtype subtype = FieldValueDefinition.getEnum(Subtype.class, fieldValueInfo.getSubtype());

        // If no variation is defined each time an event is generated the value is calculated as a new one
        if (!hasVariation(fieldValueInfo)){previousValue = null;}

        // If variation is set to "none" the value is only generated for the first event and then it remains constant for the
        // next ones
        else if (null != previousValue && isNoVariation(fieldValueInfo.getVariation())){
            return previousValue;
        }

        switch (type){
            case NUMERIC:
                switch (subtype){
                    case INTEGER:
                        return NumericValueUtils.getIntegerValue(fieldValueInfo, previousValue);

                    case DOUBLE:
                        return NumericValueUtils.getDoubleValue(fieldValueInfo, previousValue);

                    default:
                        throw new Exception(
                                "Subtype \"" + subtype.toString() + "\" not implemented yet.");
                }

            case TEXT:
                switch (subtype){
                    case STRING:
                        return TextStringValueUtils.getStringFieldValue(fieldValueInfo, previousValue);

                    case DICTIONARY:
                        return TextDictionaryValueUtils.getDictionaryFieldValue(fieldValueInfo,
                                previousValue, relatedValues);

                    default:
                        throw new Exception(
                                "Subtype \"" + subtype.toString() + "\" not implemented yet.");
                }

            case DATE:
                return DateValueUtils.getDatePropertyValue(fieldValueInfo, previousValue, true);

            default:
                throw new Exception("Type \"" + type.toString() + "\" not implemented yet.");
        }
    }

    private static boolean hasVariation (FieldValueInfo fieldValueInfo) throws Exception {
        if (null == fieldValueInfo) {throw new Exception("Received null property to be checked");}
        return null != fieldValueInfo.getVariation() && !fieldValueInfo.getVariation().isEmpty();
    }

    private static boolean isNoVariation (Map<String, String> variation) throws Exception {
        if (null == variation) {throw new Exception("Received null variation to be checked");}
        if (variation.isEmpty()) {throw new Exception("Received empty variation to be checked");}
        return PropertiesConstants.NONE.equals(variation.get(PropertiesConstants.MODE));
    }
}
