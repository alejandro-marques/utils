package org.generator.utils.values;

import org.generator.constants.PropertiesConstants;
import org.generator.exception.LimitReachedException;
import org.generator.model.configuration.FieldValueInfo;
import org.generator.model.configuration.FieldValueDefinition;
import org.generator.model.configuration.FieldValueDefinition.Subtype;
import org.generator.model.configuration.FieldValueDefinition.Type;
import org.generator.model.data.FieldValue;

import java.util.Map;

public class ValueUtils {

    public static FieldValue getValue (FieldValueInfo fieldValueInfo, int variationCount,
            FieldValue previousValue, Map<String, FieldValue> relatedValues) throws Exception {
        Type type = FieldValueDefinition.getEnum(Type.class, fieldValueInfo.getType());
        Subtype subtype = FieldValueDefinition.getEnum(Subtype.class, fieldValueInfo.getSubtype());

        // If no variation is defined each time an event is generated the value is calculated as a new one
        if (!hasVariation(fieldValueInfo)){variationCount = 0;}
        if (variationCount > 0) {
            // Checks if number of allowed variations has been reached
            checkVariationLimit(fieldValueInfo.getOptions(), variationCount, previousValue);

            // If variation is set to "none" the value is only generated for the first event and then it remains constant
            // for the next ones
            if (isNoVariation(fieldValueInfo.getVariation())) {return previousValue;}
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
                                variationCount > 0, previousValue, relatedValues);

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
        return null != variation && !variation.isEmpty() &&
                PropertiesConstants.NONE.equals(variation.get(PropertiesConstants.MODE));
    }

    private static void checkVariationLimit (Map<String, String> options, int variationCount,
            FieldValue previousValue) throws Exception {
        if (null != options) {
            boolean limit = Boolean.parseBoolean(options.get(PropertiesConstants.LIMIT));
            if (limit) {
                String maxString = options.get(PropertiesConstants.MAX_VARIATIONS);
                if (null != maxString && variationCount > Integer.parseInt(maxString)){
                    throw new LimitReachedException(previousValue.getValue(),
                            "Max allowed variations reached.");
                }
            }
        }
    }
}
