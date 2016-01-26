package org.generator.model.configuration;

import org.generator.model.data.Transformation;

import java.util.List;

public class ProcessInfo {
    private GeneratorInfo generator;
    private List<FieldInfo> model;
    private List<Transformation> transformations;

    public GeneratorInfo getGenerator() {return generator;}
    public void setGenerator(GeneratorInfo generator) {
        this.generator = generator;
    }

    public List<FieldInfo> getModel() {return model;}
    public void setModel(List<FieldInfo> model) {
        this.model = model;
    }

    public List<Transformation> getTransformations() {return transformations;}
    public void setTransformations(List<Transformation> transformations) {
        this.transformations = transformations;
    }
}
