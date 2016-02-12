package org.generator.connector;

import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import org.apache.log4j.Logger;
import org.bson.Document;

import java.util.*;

public class MongoDBConnector implements DatabaseConnector{

    private final int BUFFER_SIZE = 1000;

    private Gson gson = new Gson();
    private Logger logger = Logger.getLogger(this.getClass());

    private MongoClient client;
    private MongoCollection<Document> collection;
    private List<Document> buffer = new ArrayList<Document>();

    public MongoDBConnector (String host, int port)
            throws Exception{
        client = new MongoClient(host, port);
    }

    public MongoDBConnector (String host, int port, String databaseName, String tableName)
            throws Exception{
        logger.info("Initializing MongoDB connection (" +
                "Host: " + host + ", Port: " + port + ", " +
                "Database: " + databaseName + ", Collection: " + tableName + ")");
        client = new MongoClient(host, port);
        setupConnector(databaseName, tableName);
        logger.info("Connection established.");
    }


    @Override
    public void setupConnector(String databaseName, String tableName) throws Exception{
        collection = client.getDatabase(databaseName).getCollection(tableName);
    }

    @Override
    public void indexJson (String json) throws Exception{
        if (null == collection){throw new Exception("Collection has not been initialized");}
        Document document = Document.parse(json);
        collection.insertOne(document);
    }

    @Override
    public void indexMap(Map<String, Object> map) throws Exception {
        indexJson(gson.toJson(map));
    }

    @Override
    public void bufferedIndexJson (String json) throws Exception{
        if (null == collection){throw new Exception("Collection has not been initialized");}
        Document document = Document.parse(json);
        buffer.add(document);

        if (buffer.size() >= BUFFER_SIZE) {
            collection.insertMany(buffer);
            buffer = new ArrayList<Document>();
        }
    }

    @Override
    public void bufferedIndexMap(Map<String, Object> map) throws Exception {
        bufferedIndexJson(gson.toJson(map));
    }

    @Override
    public void flushBuffer() throws Exception{
        if (null == collection){throw new Exception("Collection has not been initialized");}
        if (buffer.size() > 0) {
            collection.insertMany(buffer);
            buffer = new ArrayList<>();
        }
    }

    @Override
    public Map<String, Object> getById(String id, String idField) throws Exception{
        if (null == collection){throw new Exception("Collection has not been initialized");}
        BasicDBObject query = new BasicDBObject(idField, id);
        FindIterable<Document> result = collection.find(query);
        Document document = result.first();

        return documentToMap(document);
    }

    @Override
    public long count (){
        return collection.count();
    }

    public Iterator<Document> find (){
        return collection.find().iterator();
    }

    @Override
    public List<Map<String, Object>> getFirst(int results) throws Exception {
        List<Map<String, Object>> list = new ArrayList<>();
        FindIterable<Document> iterable = collection.find();
        int count = 0;
        for (Document document : iterable) {
            if (count++ >= results){
                break;
            }
            list.add(documentToMap(document));
        }
        return list;
    }

    @Override
    public void close() throws Exception {
        client.close();
    }

    public MongoCollection<Document> getCollection (){return collection;}

    public void search(int number){
        int count = 0;
        FindIterable<Document> iterable = collection.find();
        for (Document document : iterable) {
            System.out.println(document);
            if (count >= number){
                break;
            }
            count++;
        }
    }

    private Map<String, Object> documentToMap (Document document){
        Map<String, Object> map = null;

        if (null != document){
            map = new HashMap<>();
            for (Map.Entry<String, Object> entry : document.entrySet()){
                map.put(entry.getKey(), entry.getValue());
            }
        }
        return map;
    }
}