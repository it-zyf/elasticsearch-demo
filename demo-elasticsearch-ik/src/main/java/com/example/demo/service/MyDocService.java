package com.example.demo.service;

import com.example.demo.controller.APIResponse;
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
}
