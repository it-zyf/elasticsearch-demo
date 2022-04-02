package com.example.demo.service;

import com.example.demo.controller.APIResponse;
import com.example.demo.vo.EsEntity;
import com.example.demo.vo.SearchEntity;
import com.example.demo.vo.User;

import java.util.List;

/**
 * @author: zyf
 * @create: 2022-03-29 17:59
 **/
public interface MyDocService {
    /**
     * 插入数据
     * @param indexName
     * @param list
     * @return
     */
    APIResponse bulkInsertRecord(String indexName, List<User> list);

    /**
     * 修改数据
     * @param indexName
     * @param esEntity
     * @return
     */
    APIResponse updateRecord(String indexName, EsEntity esEntity);

    /**
     * 根据条件删除
     * @param indexName
     * @param esEntity
     * @return
     */
    APIResponse del(String indexName, EsEntity esEntity);

    /**
     * 全文检索查询
     * @param searchEntity
     * @return
     */
    APIResponse query(SearchEntity searchEntity);

}
