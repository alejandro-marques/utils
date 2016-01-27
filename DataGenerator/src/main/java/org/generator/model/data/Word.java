package org.generator.model.data;

public class Word {
    private String value;
    private Double weight;

    public Word (String value, Double weight){
        this.value = value;
        this.weight = weight;
    }

    public String getValue() {return value;}
    public void setValue(String value) {this.value = value;}

    public Double getWeight() {return weight;}
    public void setWeight(Double weight) {this.weight = weight;}
}
