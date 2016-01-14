package org.generator.model.configuration;

import org.generator.model.data.Field;
import org.generator.model.data.Transformation;

import java.util.List;

public class ProcessConfiguration {
    private GeneratorConfiguration generator;
    private List<Field> model;
    private List<Transformation> transformations;

    public GeneratorConfiguration getGenerator() {return generator;}
    public void setGenerator(GeneratorConfiguration generator) {
        this.generator = generator;
    }

    public List<Field> getModel() {return model;}
    public void setModel(List<Field> model) {
        this.model = model;
    }

    public List<Transformation> getTransformations() {return transformations;}
    public void setTransformations(List<Transformation> transformations) {
        this.transformations = transformations;
    }
}
