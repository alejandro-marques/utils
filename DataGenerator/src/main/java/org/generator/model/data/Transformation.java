package org.generator.model.data;

import org.generator.constants.TransformationConstants;
import org.generator.model.configuration.FieldValueInfo;

import java.util.List;
import java.util.Map;

public class Transformation {

    public enum Operation {

        ADD(TransformationConstants.ADD),
        REMOVE(TransformationConstants.REMOVE),
        COPY (TransformationConstants.COPY),
        RENAME (TransformationConstants.RENAME),
        TRANSLATE (TransformationConstants.TRANSLATE),

        MULTIPLY(TransformationConstants.MULTIPLY),
        DIVIDE(TransformationConstants.DIVIDE),
        SUM (TransformationConstants.SUM),
        SUBTRACT (TransformationConstants.SUBTRACT),
        ROUND (TransformationConstants.ROUND),

        COMBINE (TransformationConstants.COMBINE),
        SPLIT (TransformationConstants.SPLIT);

        public String name;

        Operation(String name){
            this.name = name;
        }

        public static Operation fromString(String text) {
            if (text != null) {
                for (Operation operation : Operation.values()) {
                    if (text.equals(operation.name)) {
                        return operation;
                    }
                }
            }
            return null;
        }

        @Override
        public String toString() {return name;}
    }

    private String field;
    private List<String> source;
    private String operation;
    private Integer order;
    private List<Condition> conditions;
    private Map<String, String> parameters;
    private FieldValueInfo value;


    public String getField() {return field;}
    public void setField(String name) {this.field = name;}

    public List<String> getSource() {return source;}
    public void setSource(List<String> source) {this.source = source;}

    public String getOperation() {return operation;}
    public void setOperation(String operation) {this.operation = operation;}

    public Integer getOrder() {return order;}
    public void setOrder(Integer order) {this.order = order;}

    public List<Condition> getConditions() {return conditions;}
    public void setConditions(List<Condition> conditions) {this.conditions = conditions;}

    public Map<String, String> getParameters() {return parameters;}
    public void setParameters(Map<String, String> parameters) {this.parameters = parameters;}

    public FieldValueInfo getValue() {return value;}
    public void setValue(FieldValueInfo value) {this.value = value;}

    @Override
    public String toString() {
        return operation +
                (null != field? " (" + field + ")" :
                        (null != source? " (" + source + ")" : ""));
    }
}
