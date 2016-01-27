package org.generator.utils.transformations;

import org.generator.model.configuration.FieldValueDefinition;
import org.generator.model.configuration.FieldValueDefinition.Subtype;
import org.generator.model.configuration.FieldValueInfo;
import org.generator.model.data.FieldValue;
import org.generator.model.data.Transformation;
import org.generator.model.data.Transformation.Operation;
import org.generator.utils.values.ValueUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NumericTransformation {

    public static void operate (Map<String, FieldValue> document, Transformation transformation)
            throws Exception {
        boolean isInteger = true;
        List<Double> values = new ArrayList<>();

        for (String fieldName : transformation.getSource()){
            Object fieldValue = document.get(fieldName).getValue();
            if (isInteger) {
                try {getIntegerValue(fieldValue);}
                catch (Exception exception) {isInteger = false;}
            }
            try {values.add(getDoubleValue(document.get(fieldName).getValue()));}
            catch (Exception exception){
                throw new Exception("Field \"" + fieldName + "\" does not contain a valid numeric value \"" +
                        document.get(fieldName).getValue() + "\" in order to be transformed.");
            }
        }
        FieldValueInfo value = transformation.getValue();
        if (null != value){
            values.add(getDoubleValue(ValueUtils.getValue(value, 0, null, document).getValue().toString()));
            if (Subtype.INTEGER != FieldValueDefinition.getEnum(Subtype.class, value.getSubtype())){
                isInteger = false;
            }
        }

        Object result = operate(values, Operation.fromString(transformation.getOperation()));
        document.put(transformation.getField(),
                new FieldValue(isInteger? roundDouble(result) : result));
    }

    public static void round (Map<String, FieldValue> document, Transformation transformation)
            throws Exception {
        for (String fieldName : transformation.getSource()){
            document.put(fieldName, new FieldValue(roundDouble(document.get(fieldName).getValue())));
        }
    }

    private static Double operate (List<Double> values, Operation operation) throws Exception {
        Double value = values.remove(0);
        for (Double operand : values){
            switch (operation){
                case SUM:
                    if (null == value){value = 0.0;}
                    if (null == operand){operand = 0.0;}
                    value = value + operand;
                    break;
                case SUBTRACT:
                    if (null == value){value = 0.0;}
                    if (null == operand){operand = 0.0;}
                    value = value - operand;
                    break;
                case MULTIPLY:
                    if (null == value){value = 1.0;}
                    if (null == operand){operand = 1.0;}
                    value = value * operand;
                    break;
                case DIVIDE:
                    if (null == value){value = 1.0;}
                    if (null == operand){operand = 1.0;}
                    value = value / operand;
                    break;
            }
        }
        return value;
    }

    private static Integer getIntegerValue(Object source) throws Exception {
        if (null == source){return null;}
        return Integer.parseInt(source.toString());
    }

    private static Double getDoubleValue(Object source) throws Exception {
        if (null == source){return null;}
        return Double.parseDouble(source.toString());
    }

    private static Integer roundDouble(Object source) throws Exception {
        if (null == source){return null;}
        return ((int) (getDoubleValue(source) + 0.5));
    }
}
