package org.generator.model.configuration;

import java.util.HashMap;
import java.util.Map;

public class FieldInfo {

    private String name;
    private Integer order;
    private FieldValueInfo value;

    private Map<String, String> options = new HashMap<>();


    public String getName() {return name;}
    public void setName(String name) {this.name = name;}

    public Integer getOrder() {return order;}
    public void setOrder(Integer order) {this.order = order;}

    public FieldValueInfo getValue() {return value;}
    public void setValue(FieldValueInfo type) {this.value = value;}


    public Map<String, String> getOptions() {return options;}
    public void setOptions(Map<String, String> options) {this.options = options;}
}
