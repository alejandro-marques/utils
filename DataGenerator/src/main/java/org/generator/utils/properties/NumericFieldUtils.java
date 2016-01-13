package org.generator.utils.properties;

import org.generator.constants.PropertiesConstants;
import org.generator.exception.LimitReachedException;
import org.generator.model.data.Field;
import org.generator.model.data.Property;
import org.generator.model.data.Property.Value;
import org.generator.utils.RandomUtils;

import java.util.Map;

public class NumericFieldUtils {

    public static Integer getIntegerFieldValue(Field field, Object previousValue) throws Exception {
        return getNumericFieldValue(field, previousValue).intValue();
    }

    public static Double getDoubleFieldValue(Field field, Object previousValue) throws Exception {
        return getNumericFieldValue(field, previousValue);
    }

    public static Double getNumericFieldValue(Field field, Object previousValue) throws Exception {
        Double value;
        if (null != previousValue){
            if (previousValue instanceof Double){value = (Double) previousValue;}
            else if (previousValue instanceof Integer){value = ((Integer) previousValue).doubleValue();}
            else {throw new Exception (previousValue.toString() + " is not a valid number");}

            value = value + getValue(field.getVariation());
            value = limitValue(value, field.getParameters());
        }
        else {value = getValue(field.getValue());}

        return value;
    }

    public static Double getValue (Map<String, String> parameters) throws Exception {
        String typeString = parameters.get(PropertiesConstants.TYPE);
        Value valueType = Property.getEnum(Value.class, typeString);

        switch (valueType){
            case FIXED:
                return getFixedValue(parameters);

            case UNIFORM:
                return getUniformValue(parameters);

            case GAUSSIAN:
                return getGaussianValue(parameters);

            default:
                throw new Exception("Integer type \"" + typeString + "\" not implemented yet.");
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
