package com.example.demo.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.example.demo.controller.APIResponse;
import com.example.demo.service.MyDocService;
import com.example.demo.vo.EsEntity;
import com.example.demo.vo.User;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.elasticsearch.index.reindex.DeleteByQueryRequestBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author: zyf
 * @create: 2022-03-29 17:59
 **/
@Service
@Slf4j
public class MyDocServiceImpl implements MyDocService {
    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Override
    public APIResponse bulkInsertRecord(String indexName, List<User> list) {
        APIResponse apiResponse = new APIResponse();
        apiResponse.setResponseCode(APIResponse.OK);
        try {
            BulkRequest bulkRequest = new BulkRequest();
            Optional.ofNullable(list).orElseGet(ArrayList::new)
                    .forEach(doc -> {
                        Map<String, Object> map = BeanUtil.beanToMap(doc);
                        IndexRequest indexRequest = new IndexRequest(indexName).source(map, XContentType.JSON);
                        bulkRequest.add(indexRequest);
                    });
            restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
        } catch (Exception e) {
            apiResponse.setResponseCode(APIResponse.ERROR);
            log.error("批量新增文档失败，异常信息：{}", e);
        }
        return apiResponse;
    }

    @Override
    public APIResponse updateRecord(String indexName, EsEntity esEntity) {
        APIResponse apiResponse = new APIResponse();
        apiResponse.setResponseCode(APIResponse.OK);
        try {
            SearchRequest searchRequest = new SearchRequest(indexName);
            SearchSourceBuilder builder = new SearchSourceBuilder();
            builder.query(QueryBuilders.termQuery("name", esEntity.getName()));
            searchRequest.source(builder);
            SearchResponse entity = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            if (null != entity.getHits().getHits()) {
                for (SearchHit hit : entity.getHits().getHits()) {
                    //修改
                    UpdateRequest updateRequest = new UpdateRequest(indexName, hit.getId());
                    updateRequest.doc(BeanUtil.beanToMap(esEntity));
                    restHighLevelClient.update(updateRequest, RequestOptions.DEFAULT);
                }
            }
        } catch (Exception e) {
            apiResponse.setResponseCode(APIResponse.ERROR);
            log.error("批量新增文档失败，异常信息：{}", e);
        }
        return apiResponse;
    }

    @Override
    public APIResponse del(String indexName, EsEntity esEntity) {
        APIResponse apiResponse = new APIResponse();
        apiResponse.setResponseCode(APIResponse.OK);
        try {
            DeleteByQueryRequest request = new DeleteByQueryRequest(indexName);
            request.setQuery(QueryBuilders.termQuery("name", esEntity.getName()));
            restHighLevelClient.deleteByQuery(request,RequestOptions.DEFAULT);
        } catch (Exception e) {
            apiResponse.setResponseCode(APIResponse.ERROR);
            log.error("批量新增文档失败，异常信息：{}", e);
        }
        return apiResponse;
    }
}
