package org.generator.model.data;

import java.util.HashMap;
import java.util.Map;

public class FieldValue {
    private Object value;
    private Map<String, Object> properties;

    public FieldValue (Object value){this(value, null);}
    public FieldValue (Object value, Map<String, Object> properties){
        this.value = value;
        this.properties = properties;
    }

    public Object getValue() {
        return value;
    }
    public void setValue(Object value) {
        this.value = value;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }
    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }
    public void addProperty (String propertyName, Object propertyValue){
        if (null == properties){properties = new HashMap<>();}
        properties.put(propertyName, propertyValue);
    }
}
