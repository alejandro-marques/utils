package org.generator.model.data;

import java.util.List;

public class Word {
    private String value;
    private Integer weight;
    private List<Word> next;

    public String getValue() {return value;}
    public void setValue(String value) {this.value = value;}

    public Integer getWeight() {return weight;}
    public void setWeight(Integer weight) {this.weight = weight;}

    public List<Word> getNext() {return next;}
    public void setNext(List<Word> next) {this.next = next;}
}
