package org.generator.model.configuration;

import org.generator.model.data.Field;

import java.util.List;

public class GeneratorConfiguration {
    private int maxDocuments;

    private List<Field> id;

    private int minRelatedDocuments;
    private int maxRelatedDocuments;


    public int getMaxDocuments() {return maxDocuments;}
    public void setMaxDocuments(int maxDocuments) {this.maxDocuments = maxDocuments;}

    public List<Field> getId() {return id;}
    public void setId(List<Field> id) {this.id = id;}

    public int getMinRelatedDocuments() {return minRelatedDocuments;}
    public void setMinRelatedDocuments(int minRelatedDocuments) {
        this.minRelatedDocuments = minRelatedDocuments;
    }

    public int getMaxRelatedDocuments() {return maxRelatedDocuments;}
    public void setMaxRelatedDocuments(int maxRelatedDocuments) {
        this.maxRelatedDocuments = maxRelatedDocuments;
    }
}