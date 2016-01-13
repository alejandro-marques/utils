package org.generator.model.configuration;

import org.generator.model.data.Field;

import java.util.List;

public class ProcessConfiguration {
    private GeneratorConfiguration generator;
    private List<Field> model;

    public GeneratorConfiguration getGenerator() {return generator;}
    public void setGenerator(GeneratorConfiguration generator) {
        this.generator = generator;
    }

    public List<Field> getModel() {return model;}
    public void setModel(List<Field> model) {
        this.model = model;
    }
}
