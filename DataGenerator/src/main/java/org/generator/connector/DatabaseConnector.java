package org.generator.connector;

import java.util.List;
import java.util.Map;

public interface DatabaseConnector {

    public void setupConnector(String databaseName, String tableName) throws Exception;

    public void indexJson (String json) throws Exception;
    public void indexMap (Map<String, Object> map) throws Exception;
    public void bufferedIndexJson (String json) throws Exception;
    public void bufferedIndexMap (Map<String, Object> map) throws Exception;
    public void flushBuffer() throws Exception;

    public Map<String, Object> getById(String id, String idField) throws Exception;
    public long count() throws Exception;
    public List<Map<String, Object>> getFirst(int results) throws Exception;

    public void close() throws Exception;
}
