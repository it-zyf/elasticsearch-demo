package com.example.demo.controller;

import com.example.demo.service.IndexService;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.json.JsonXContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;

@RestController
public class IndexController {
    private final static Logger log = LoggerFactory.getLogger(IndexController.class);

    @Autowired
    private IndexService indexService;


    /**
     * 创建索引
     * @param indexName
     * @param config
     * @return
     */
    @PostMapping("/es/index/create/{indexName}")
    public APIResponse create(@PathVariable String indexName, @RequestBody Map<String, Map<String, Object>> config) {
        APIResponse apiResponse = new APIResponse();
        apiResponse.setResponseCode(APIResponse.OK);
        try {
            CreateIndexRequest createIndexRequest = new CreateIndexRequest(indexName);
            createIndexRequest.mapping(config);
            indexService.create(indexName, createIndexRequest);
        } catch (Exception e) {
            apiResponse.setResponseCode(APIResponse.ERROR);
            log.error("创建索引失败，异常信息：{}", e);
        }
        return apiResponse;
    }

    @PostMapping("/es/index/{indexName}")
    public APIResponse index(@PathVariable String indexName) throws IOException {


        return indexService.index(indexName);
    }



}
