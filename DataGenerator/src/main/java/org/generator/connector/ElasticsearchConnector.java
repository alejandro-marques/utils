package org.generator.connector;

import com.google.gson.Gson;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.IdsQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import java.util.List;
import java.util.Map;

public class ElasticsearchConnector implements DatabaseConnector{
    private final int BUFFER_SIZE = 1000;

    private Gson gson = new Gson();

    private TransportClient client;
    private BulkRequestBuilder bulkRequest = null;
    private String index;
    private String type;

    public ElasticsearchConnector (String hostName, int hostPort, String clusterName){
        Settings settings = ImmutableSettings.settingsBuilder().put("cluster.name", clusterName).build();
        client = new TransportClient(settings);
        client.addTransportAddress(new InetSocketTransportAddress(hostName, hostPort));
        bulkRequest = client.prepareBulk();
    }


    @Override
    public void setupConnector(String databaseName, String tableName) throws Exception {
        this.index = databaseName;
        this.type = tableName;
    }

    public void indexJson (String json){
       client.prepareIndex(index, type).setSource(json).execute();
    }

    @Override
    public void indexMap(Map<String, Object> map) throws Exception {
        indexJson(gson.toJson(map));
    }

    public void bufferedIndexJson (String json){
        bulkRequest.add(client.prepareIndex(index, type).setSource(json));

        if (bulkRequest.numberOfActions() >= BUFFER_SIZE) {
            flushBuffer();
        }
    }

    @Override
    public void bufferedIndexMap(Map<String, Object> map) throws Exception {
        bufferedIndexJson(gson.toJson(map));
    }

    public void flushBuffer(){
        if (bulkRequest.numberOfActions() > 0){
            bulkRequest.execute().actionGet();
            bulkRequest = client.prepareBulk();
        }
    }

    @Override
    public Map<String, Object> getById(String id, String idField) {
        SearchRequestBuilder requestBuilder = client.prepareSearch(index);
        // TODO: Hacer query por campo, no por id
        IdsQueryBuilder queryBuilder = QueryBuilders.idsQuery(type);
        queryBuilder.addIds(id);
        SearchResponse response = requestBuilder.execute().actionGet();
        return null;
        // TODO: Mapear de response a Map
    }

    @Override
    public long count() throws Exception {
        return 0;
    }

    @Override
    public List<Map<String, Object>> getFirst(int results) throws Exception {
        return null;
    }
}
