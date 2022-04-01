package com.example.demo.service.impl;

import com.example.demo.controller.APIResponse;
import com.example.demo.service.IndexService;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.get.GetIndexRequest;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.json.JsonXContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;


@Service("indexService")
public class IndexServiceImpl implements IndexService {
    private final static Logger log = LoggerFactory.getLogger(IndexServiceImpl.class);

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    /**
     *  创建索引
     * @param index       索引名称
     * @param request     创建索引的REQUEST
     * @throws IOException
     */
    @Override
    public void create(String index, CreateIndexRequest request) throws Exception {
        if (!exists(index)) {
            //同步方式创建索引
            restHighLevelClient.indices().create(request, RequestOptions.DEFAULT);

//            //异步方式创建索引不会阻塞并立即返回
//            ActionListener<CreateIndexResponse> listener = new ActionListener<CreateIndexResponse>() {
//                @Override
//                public void onResponse(CreateIndexResponse createIndexResponse) {
//                    log.info("创建索引成功");
//                }
//                @Override
//                public void onFailure(Exception e) {
//                    log.info("创建索引失败，异常信息：{}", e);
//                }
//            };
//            restHighLevelClient.indices().createAsync(request, RequestOptions.DEFAULT, listener);//要执行的CreateIndexRequest和执行完成时要使用的ActionListener
        } else {
            log.warn("该索引：{}已经存在，不能再创建。", index);
        }
    }

    /**
     * 判断索引是否存在
     * @param index 索引名称
     * @throws IOException
     */
    public boolean exists(String index) throws Exception {
        GetIndexRequest request = new GetIndexRequest();
        request.indices(index);
        boolean exists = restHighLevelClient.indices().exists(request, RequestOptions.DEFAULT);
        return exists;
    }

    @Override
    public APIResponse index(String indexName) throws IOException{

        XContentBuilder xContentBuilder = JsonXContent.contentBuilder()
                .startObject()
                    .startObject("properties")
                    .startObject("name")
                    .field("type", "text")
                    .endObject()
                    .endObject()
                .endObject();
        CreateIndexRequest createIndexRequest = new CreateIndexRequest(indexName).mapping(xContentBuilder);
        restHighLevelClient.indices().create(createIndexRequest,RequestOptions.DEFAULT);
        return new APIResponse();
    }

}
