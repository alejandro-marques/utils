package org.generator.model.configuration;

import java.util.HashMap;
import java.util.Map;

public class FieldValueInfo {

    private String type;
    private String subtype;

    private Map<String, String> initial = new HashMap<>();
    private Map<String, String> variation = new HashMap<>();

    private Map<String, String> options = new HashMap<>();


    public String getType() {return type;}
    public void setType(String type) {this.type = type;}

    public String getSubtype() {return subtype;}
    public void setSubtype(String subtype) {this.subtype = subtype;}


    public Map<String, String> getInitial() {return initial;}
    public void setInitial(Map<String, String> initial) {this.initial = initial;}

    public Map<String, String> getVariation() {return variation;}
    public void setVariation(Map<String, String> variation) {this.variation = variation;}


    public Map<String, String> getOptions() {return options;}
    public void setOptions(Map<String, String> options) {this.options = options;}
}
