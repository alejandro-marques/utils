package org.generator.model.data;

import org.generator.constants.TransformationConstants;

import java.util.List;

public class Condition {

    public enum Type{

        EQUAL(TransformationConstants.EQUAL);

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

    private String condition;
    private List<String> source;
    private String value;

    public String getCondition() {return condition;}
    public void setCondition(String condition) {this.condition = condition;}

    public List<String> getSource() {return source;}
    public void setSource(List<String> source) {this.source = source;}

    public String getValue() {return value;}
    public void setValue(String value) {this.value = value;}
}
