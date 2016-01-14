package org.generator.model.data;

import org.generator.constants.TransformationConstants;

import java.util.List;
import java.util.Map;

public class Transformation {

    public enum Type{

        ADD(TransformationConstants.ADD),
        REMOVE(TransformationConstants.REMOVE),
        COPY (TransformationConstants.COPY),
        RENAME (TransformationConstants.RENAME),

        MULTIPLY(TransformationConstants.MULTIPLY),
        DIVIDE(TransformationConstants.DIVIDE),
        SUM (TransformationConstants.SUM),
        SUBTRACT (TransformationConstants.SUBTRACT),
        ROUND (TransformationConstants.ROUND),

        COMBINE (TransformationConstants.COMBINE),
        SPLIT (TransformationConstants.SPLIT);

        public String name;

        Type (String name){
            this.name = name;
        }

        public static Type fromString(String text) {
            if (text != null) {
                for (Type type : Type.values()) {
                    if (text.equals(type.name)) {
                        return type;
                    }
                }
            }
            return null;
        }
    }

    private String field;
    private List<String> source;
    private String operation;
    private List<Condition> conditions;
    private Map<String, String> parameters;
    private Field value;


    public String getField() {return field;}
    public void setField(String name) {this.field = name;}

    public List<String> getSource() {return source;}
    public void setSource(List<String> source) {this.source = source;}

    public String getOperation() {return operation;}
    public void setOperation(String operation) {this.operation = operation;}

    public List<Condition> getConditions() {return conditions;}
    public void setConditions(List<Condition> conditions) {this.conditions = conditions;}

    public Map<String, String> getParameters() {return parameters;}
    public void setParameters(Map<String, String> parameters) {this.parameters = parameters;}

    public Field getValue() {return value;}
    public void setValue(Field value) {this.value = value;}
}
