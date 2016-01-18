package org.generator.utils.properties;

import org.generator.constants.PropertiesConstants;
import org.generator.model.data.Field;
import org.generator.model.data.Property;
import org.generator.model.data.Property.Type;
import org.generator.model.data.Property.Subtype;

import java.util.Map;

public class FieldUtils {

    public static Object getFieldValue (Field field, Object previousValue) throws Exception {
        Type type = Property.getEnum(Type.class, field.getType());
        Subtype subtype = Property.getEnum(Subtype.class, field.getSubtype());

        // If no variation is defined each time an event is generated the value is calculated as a new one
        if (!hasVariation(field)){previousValue = null;}

        // If variation is set to "none" the value is only generated for the first event and then it remains constant for the next ones
        else if (null != previousValue && isNoVariation(field.getVariation())){
            return previousValue;
        }

        switch (type){
            case NUMERIC:
                switch (subtype){
                    case INTEGER:
                        return NumericFieldUtils.getIntegerFieldValue(field, previousValue);

                    case DOUBLE:
                        return NumericFieldUtils.getDoubleFieldValue(field, previousValue);

                    default:
                        throw new Exception(
                                "Subtype \"" + subtype.toString() + "\" not implemented yet.");
                }

            case TEXT:
                switch (subtype){
                    case STRING:
                        return TextStringFieldUtils.getStringFieldValue(field, previousValue);

                    case DICTIONARY:
                        return TextDictionaryFieldUtils.getDictionaryFieldValue(field, previousValue);

                    case RELATION:
                        return TextDictionaryFieldUtils.getRelationFieldValue(field, previousValue);

                    default:
                        throw new Exception(
                                "Subtype \"" + subtype.toString() + "\" not implemented yet.");
                }

            case DATE:
                return DateFieldUtils.getDatePropertyValue(field, previousValue, true);

            default:
                throw new Exception("Type \"" + type.toString() + "\" not implemented yet.");
        }
    }

    private static boolean hasVariation (Field field) throws Exception {
        if (null == field) {throw new Exception("Received null property to be checked");}
        return null != field.getVariation() && !field.getVariation().isEmpty();
    }

    private static boolean isNoVariation (Map<String, String> variation) throws Exception {
        if (null == variation) {throw new Exception("Received null variation to be checked");}
        if (variation.isEmpty()) {throw new Exception("Received empty variation to be checked");}
        return PropertiesConstants.NONE.equals(variation.get(PropertiesConstants.TYPE));
    }
}
