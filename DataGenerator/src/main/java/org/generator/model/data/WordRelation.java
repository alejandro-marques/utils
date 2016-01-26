package org.generator.model.data;

import java.util.List;

public class WordRelation {
    private String value;
    private List<Word> next;

    public String getValue() {return value;}
    public void setValue(String value) {this.value = value;}

    public List<Word> getNext() {return next;}
    public void setNext(List<Word> next) {this.next = next;}
}
