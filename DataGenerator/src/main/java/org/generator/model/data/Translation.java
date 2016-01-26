package org.generator.model.data;

import java.util.Map;

public class Translation {
    String value;
    Map<String, Object> fields;

    public String getValue() {return value;}
    public void setValue(String value) {this.value = value;}

    public Map<String, Object> getFields() {return fields;}
    public void setFields(Map<String, Object> fields) {this.fields = fields;}
}
