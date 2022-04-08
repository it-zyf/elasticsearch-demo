package com.example.demo.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSON;
import com.example.demo.controller.APIResponse;
import com.example.demo.service.MyDocService;
import com.example.demo.vo.EsEntity;
import com.example.demo.vo.SearchEntity;
import com.example.demo.vo.User;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.elasticsearch.index.reindex.DeleteByQueryRequestBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

/**
 * @author: zyf
 * @create: 2022-03-29 17:59
 **/
@Service
@Slf4j
public class MyDocServiceImpl implements MyDocService {
    @Autowired
    private RestHighLevelClient restHighLevelClient;

    private static final String indexName="dev-data";

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
            BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
            boolQuery.must(QueryBuilders.termQuery("dataSource", esEntity.getDataSource()));
            boolQuery.must(QueryBuilders.termQuery("id",esEntity.getId()));
            searchRequest.source(new SearchSourceBuilder().query(boolQuery));
            SearchResponse entity = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            if (null != entity.getHits().getHits() && entity.getHits().getTotalHits().value==1) {
                for (SearchHit hit : entity.getHits().getHits()) {
                    //修改
                    UpdateRequest updateRequest = new UpdateRequest(indexName, hit.getId());
                    updateRequest.doc(BeanUtil.beanToMap(entity),XContentType.JSON);
                    restHighLevelClient.update(updateRequest, RequestOptions.DEFAULT);
                }
            }
            return apiResponse;
        } catch (Exception e) {
            log.error("更新文档失败，异常信息：{}", e);
            return apiResponse;
        }
    }

    @Override
    public APIResponse del(String indexName, EsEntity esEntity) {
        APIResponse apiResponse = new APIResponse();
        apiResponse.setResponseCode(APIResponse.OK);
        try {
            DeleteByQueryRequest request = new DeleteByQueryRequest(indexName);
            BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
            boolQuery.must(QueryBuilders.termQuery("dataSource", esEntity.getDataSource()));
            boolQuery.must(QueryBuilders.termQuery("id",esEntity.getId()));
            request.setQuery(boolQuery);
            restHighLevelClient.deleteByQuery(request,RequestOptions.DEFAULT);
        } catch (Exception e) {
            apiResponse.setResponseCode(APIResponse.ERROR);
            log.error("批量新增文档失败，异常信息：{}", e);
        }
        return apiResponse;
    }

    @Override
    public APIResponse query(SearchEntity searchEntity) {
        APIResponse apiResponse = new APIResponse();
        apiResponse.setResponseCode(APIResponse.OK);
        SearchRequest searchRequest = new SearchRequest(indexName);
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

        //全文查询
        MultiMatchQueryBuilder builder = QueryBuilders.multiMatchQuery(searchEntity.getKeyWord(),
                "loopId",
                "loopTitle",
                "loopNo",
                "assetsName",
                "assetsIP",
                "patchName",
                "toolName",
                "knowledgeName")
                .operator(Operator.AND);
        sourceBuilder.query(builder);
        sourceBuilder.from((searchEntity.getPageNo()-1)*searchEntity.getPageSize());
        sourceBuilder.size(searchEntity.getPageSize());
        searchRequest.source(sourceBuilder);
        try {
            SearchResponse response = restHighLevelClient.search(searchRequest,RequestOptions.DEFAULT);
            SearchHits hits = response.getHits();
            long total = hits.getTotalHits().value;
            //结果集处理
            List<Map<String, Object>> result=new ArrayList<>();
            SearchHit[] hits1 = hits.getHits();
            for (SearchHit entity : hits1) {
                result.add(entity.getSourceAsMap());
            }
            apiResponse.setReasonCode(String.valueOf(total));
            apiResponse.setData(result);
        } catch (IOException e) {
            apiResponse.setResponseCode(APIResponse.ERROR);
            log.error("查询文档失败，异常信息：{}", e);
        }
        return apiResponse;
    }

}
