package org.generator.launcher.output;

import java.util.Map;

public interface Output {
    void write (Map<String, Object> document) throws Exception;
    void flush () throws Exception;
}
