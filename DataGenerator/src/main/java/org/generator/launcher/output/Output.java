package org.generator.launcher.output;

import org.generator.launcher.LauncherConstants.Format;

import java.util.Map;

public interface Output {
    void write (Map<String, Object> document, Format format) throws Exception;
    void flush () throws Exception;
    void close () throws Exception;
}
