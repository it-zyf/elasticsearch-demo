package com.example.demo.service;

import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;

import java.io.IOException;

public interface DocService {
    void batch(BulkRequest bulkRequest) throws IOException;
    SearchResponse search(SearchRequest searchRequest) throws IOException;
}
