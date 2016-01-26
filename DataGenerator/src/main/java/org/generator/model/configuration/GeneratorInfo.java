package org.generator.model.configuration;

import java.util.List;

public class GeneratorInfo {
    private Integer maxDocuments;
    private Integer minRelatedDocuments;
    private Integer maxRelatedDocuments;

    private boolean auxiliaryFields;

    private List<FieldInfo> idFields;



    public Integer getMaxDocuments() {return maxDocuments;}
    public void setMaxDocuments(Integer maxDocuments) {this.maxDocuments = maxDocuments;}

    public Integer getMinRelatedDocuments() {return minRelatedDocuments;}
    public void setMinRelatedDocuments(Integer minRelatedDocuments) {
        this.minRelatedDocuments = minRelatedDocuments;
    }

    public Integer getMaxRelatedDocuments() {return maxRelatedDocuments;}
    public void setMaxRelatedDocuments(Integer maxRelatedDocuments) {
        this.maxRelatedDocuments = maxRelatedDocuments;
    }

    public boolean isAuxiliaryFields() {return auxiliaryFields;}
    public void setAuxiliaryFields(boolean auxiliaryFields) {this.auxiliaryFields = auxiliaryFields;}


    public List<FieldInfo> getIdFields() {return idFields;}
    public void setIdFields(List<FieldInfo> idFields) {this.idFields = idFields;}
}