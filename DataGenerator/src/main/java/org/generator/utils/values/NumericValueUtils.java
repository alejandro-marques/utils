package org.generator.utils.values;

import org.generator.constants.PropertiesConstants;
import org.generator.exception.LimitReachedException;
import org.generator.model.configuration.FieldValueDefinition;
import org.generator.model.configuration.FieldValueDefinition.Mode;
import org.generator.model.configuration.FieldValueInfo;
import org.generator.model.data.FieldValue;
import org.generator.utils.RandomUtils;

import java.util.Map;

public class NumericValueUtils {

    public static FieldValue getIntegerValue(FieldValueInfo fieldValueInfo, FieldValue previousValue)
            throws Exception {
        return new FieldValue(getNumericValue(fieldValueInfo, previousValue).intValue());
    }

    public static FieldValue getDoubleValue(FieldValueInfo fieldValueInfo, FieldValue previousValue)
            throws Exception {
        return new FieldValue(getNumericValue(fieldValueInfo, previousValue));
    }

    public static Double getNumericValue(FieldValueInfo fieldValueInfo, FieldValue previousValue)
            throws Exception {
        Double value;
        // If there was a previous value
        if (null != previousValue){
            // Checks if previous value was a valid number
            try {value = Double.parseDouble(previousValue.getValue().toString());}
            catch (Exception exception){
                throw new Exception ("Error while generating numeric variation. Previous value \"" +
                        previousValue.toString() + "\" is not a valid number.");
            }

            // New value is calculated adding the variation to the original value
            value = value + getValue(fieldValueInfo.getVariation());
            // Then resulting value is limited if necessary
            value = limitValue(value, fieldValueInfo.getOptions());
        }
        // If no previous value was received then initial value is calculated instead of variation
        else {value = getValue(fieldValueInfo.getInitial());}

        return value;
    }

    public static Double getValue (Map<String, String> parameters) throws Exception {
        String modeName = parameters.get(PropertiesConstants.MODE);
        Mode valueMode = FieldValueDefinition.getEnum(Mode.class, modeName);
        if (null == valueMode) {
            throw new Exception("Not supported numeric mode \"" + modeName + "\".");
        }

        switch (valueMode){
            case FIXED:
                return getFixedValue(parameters);

            case UNIFORM:
                return getUniformValue(parameters);

            case GAUSSIAN:
                return getGaussianValue(parameters);

            default:
                throw new Exception("Numeric mode \"" + modeName + "\" not implemented yet.");
        }
    }

    private static Double getFixedValue (Map<String, String> parameters) throws Exception {
        return Double.parseDouble(parameters.get(PropertiesConstants.VALUE));
    }

    private static Double getUniformValue (Map<String, String> parameters) throws Exception {
        return RandomUtils.getRandomDouble(
                Integer.parseInt(parameters.get(PropertiesConstants.MIN)),
                Integer.parseInt(parameters.get(PropertiesConstants.MAX)));
    }

    private static Double getGaussianValue (Map<String, String> parameters) throws Exception {
        return RandomUtils.getRandomGaussianDouble(
                Double.parseDouble(parameters.get(PropertiesConstants.MEAN)),
                Double.parseDouble(parameters.get(PropertiesConstants.VARIANCE)),
                Integer.parseInt(parameters.get(PropertiesConstants.MIN)),
                Integer.parseInt(parameters.get(PropertiesConstants.MAX)));
    }

    private static Double limitValue (Double value, Map<String, String> parameters) throws Exception {
        boolean limit = Boolean.parseBoolean(parameters.get(PropertiesConstants.LIMIT));
        String maxValueString = parameters.get(PropertiesConstants.MAX);
        if (maxValueString != null){
            Integer maxValue = Integer.parseInt(maxValueString);
            if (value > maxValue){
                if (limit){throw new LimitReachedException(maxValue, "Maximum value already reached");}
                value = maxValue.doubleValue();
            }
        }
        String minValueString = parameters.get(PropertiesConstants.MIN);
        if (minValueString != null){
            Integer minValue = Integer.parseInt(minValueString);
            if (value < minValue){
                if (limit){throw new LimitReachedException(minValue, "Minimum value already reached");}
                value = minValue.doubleValue();
            }
        }
        return value;
    }
}
