package com.example.demo.controller;

import com.example.demo.vo.Parameter;
import com.example.demo.vo.UserBirthdayVO;
import com.example.demo.service.DocService;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.core.TimeValue;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

@RestController
public class DocController {
    private final static Logger log = LoggerFactory.getLogger(DocController.class);

    @Autowired
    private DocService docService;


    /**
     * 批量新增文档
     *
     * @param indexName
     * @param parameter
     * @return
     */
    @PostMapping("/es/doc/add/batch/{indexName}")
    public APIResponse bulkInsertRecord(@PathVariable String indexName, @RequestBody Parameter parameter) {
        APIResponse apiResponse = new APIResponse();
        apiResponse.setResponseCode(APIResponse.OK);
        try {
            BulkRequest bulkRequest = new BulkRequest();
            Optional.ofNullable(parameter.getDocList()).orElseGet(ArrayList::new)
                    .forEach(doc -> {
                        IndexRequest indexRequest = new IndexRequest(indexName).source(doc, XContentType.JSON);
                        bulkRequest.add(indexRequest);
                    });
            docService.batch(bulkRequest);
        } catch (Exception e) {
            apiResponse.setResponseCode(APIResponse.ERROR);
            log.error("批量新增文档失败，异常信息：{}", e);
        }
        return apiResponse;
    }

    /**
     * 查询所有文档
     *
     * @param indexName
     * @return
     */
    @GetMapping("/es/doc/search/all/{indexName}")
    public APIResponse search(@PathVariable String indexName) {
        APIResponse apiResponse = new APIResponse();
        apiResponse.setResponseCode(APIResponse.OK);
        SearchRequest searchRequest = new SearchRequest(indexName);
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));
        searchRequest.source(sourceBuilder);
        try {
            SearchResponse response = docService.search(searchRequest);
            SearchHits hits = response.getHits();
            apiResponse.setData(hits);
        } catch (IOException e) {
            apiResponse.setResponseCode(APIResponse.ERROR);
            log.error("查询文档失败，异常信息：{}", e);
        }
        return apiResponse;
    }

    /**
     * 全文查询文档
     *
     * @param indexName
     * @param param
     * @return
     */
    @PostMapping("/es/doc/search/byquery/{indexName}")
    public APIResponse search(@PathVariable String indexName, @RequestBody UserBirthdayVO param) {
        APIResponse apiResponse = new APIResponse();
        apiResponse.setResponseCode(APIResponse.OK);
        SearchRequest searchRequest = new SearchRequest();
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

        //全文查询
//        MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("name", param.getName());
        MultiMatchQueryBuilder fuzziness = QueryBuilders.multiMatchQuery(param.getName(), "name")
                .fuzziness(Fuzziness.AUTO);//模糊匹配
        sourceBuilder.query(fuzziness);

//        sourceBuilder.query(matchQueryBuilder);
        sourceBuilder.from(1);
        sourceBuilder.size(10);
        searchRequest.source(sourceBuilder);
        try {
            SearchResponse response = docService.search(searchRequest);
            SearchHits hits = response.getHits();
            //结果集处理
            SearchHit[] hits1 = hits.getHits();
            apiResponse.setData(this.packResult(hits1));
        } catch (IOException e) {
            apiResponse.setResponseCode(APIResponse.ERROR);
            log.error("查询文档失败，异常信息：{}", e);
        }
        return apiResponse;
    }


    private List<Map<String, Object>> packResult(SearchHit[] hits) {
        List<Map<String, Object>> list = new ArrayList<>();
        if (null != hits && hits.length > 0) {
            for (SearchHit hit : hits) {
                Map<String, Object> map = new HashMap<>(2);
                String name = hit.getSourceAsMap().get("name").toString();
                map.put("name", name);
                String age = hit.getSourceAsMap().get("age").toString();
                map.put("age", age);
                list.add(map);
            }
        }
        return list;
    }


}
