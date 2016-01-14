package org.generator.utils.transformations;

import org.generator.model.data.Field;
import org.generator.model.data.Transformation;
import org.generator.utils.properties.FieldUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NumericTransformation {

    public static void sum (Map<String, Object> document, Transformation transformation)
            throws Exception {

        List<Double> values = getValues(document, transformation);
        Double value = values.remove(0);

        for (Double operand : values){value = value + operand;}

        document.put(transformation.getField(), value);
    }

    public static void subtract (Map<String, Object> document, Transformation transformation)
            throws Exception {
        List<Double> values = getValues(document, transformation);
        Double value = values.remove(0);
        for (Double operand : values){value = value - operand;}

        document.put(transformation.getField(), value);
    }

    public static void multiply (Map<String, Object> document, Transformation transformation)
            throws Exception {
        List<Double> values = getValues(document, transformation);
        Double value = values.remove(0);
        for (Double operand : values){value = value * operand;}

        document.put(transformation.getField(), value);
    }

    public static void divide (Map<String, Object> document, Transformation transformation)
            throws Exception {
        List<Double> values = getValues(document, transformation);
        Double value = values.remove(0);
        for (Double operand : values){value = value / operand;}

        document.put(transformation.getField(), value);
    }

    public static void round (Map<String, Object> document, Transformation transformation)
            throws Exception {
        for (String fieldName : transformation.getSource()){
            document.put(fieldName, getIntegerValue(document.get(fieldName)));
        }
    }

    private static List<Double> getValues (Map<String, Object> document, Transformation transformation)
            throws Exception {
        List<Double> values = new ArrayList<>();

        for (String fieldName : transformation.getSource()){
            try {values.add(getDoubleValue(document.get(fieldName)));}
            catch (Exception exception){
                throw new Exception("Field \"" + fieldName + "\" does not contain a valid numeric value \"" +
                        document.get(fieldName) + "\" in order to be transformed.");
            }
        }
        Field value = transformation.getValue();
        if (null != value){
            values.add(getDoubleValue(FieldUtils.getFieldValue(value, null).toString()));
        }

        return values;
    }

    private static Double getDoubleValue(Object source) throws Exception {
        if (null == source){return null;}
        return Double.parseDouble(source.toString());
    }

    private static Integer getIntegerValue(Object source) throws Exception {
        if (null == source){return null;}
        return ((int) (getDoubleValue(source) + 0.5));
    }
}
