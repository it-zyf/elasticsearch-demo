package com.example.demo.service.impl;

import com.example.demo.service.DocService;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service("docService")
public class DocServiceImpl implements DocService {
    private final static Logger log = LoggerFactory.getLogger(DocServiceImpl.class);
    @Autowired
    private RestHighLevelClient restHighLevelClient;


    @Override
    public void batch(BulkRequest bulkRequest) throws IOException {
        restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
    }

    @Override
    public SearchResponse search(SearchRequest searchRequest) throws IOException {
        return restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
    }
}
