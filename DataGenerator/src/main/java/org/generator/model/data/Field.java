package org.generator.model.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Field {

    private String name;
    private String type;
    private String subtype;

    private Map<String, String> parameters = new HashMap<>();
    private Map<String, String> value = new HashMap<>();
    private Map<String, String> variation = new HashMap<>();

    private List<Field> subFields = new ArrayList<>();

    public String getName() {return name;}
    public void setName(String name) {this.name = name;}

    public String getType() {return type;}
    public void setType(String type) {this.type = type;}

    public String getSubtype() {return subtype;}
    public void setSubtype(String subtype) {this.subtype = subtype;}


    public Map<String, String> getParameters() {return parameters;}
    public void setParameters(Map<String, String> parameters) {this.parameters = parameters;}

    public Map<String, String> getValue() {return value;}
    public void setValue(Map<String, String> value) {this.value = value;}

    public Map<String, String> getVariation() {return variation;}
    public void setVariation(Map<String, String> variation) {this.variation = variation;}

    public List<Field> getSubFields() {return subFields;}
    public void setSubFields(List<Field> subFields) {this.subFields = subFields;}
}
