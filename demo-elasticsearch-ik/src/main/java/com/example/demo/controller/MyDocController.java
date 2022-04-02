package com.example.demo.controller;

import com.example.demo.service.MyDocService;
import com.example.demo.vo.EsEntity;
import com.example.demo.vo.Parameter2;
import com.example.demo.vo.SearchEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author: zyf
 * @create: 2022-03-29 17:55
 **/
@RestController
@RequestMapping("/es")
public class MyDocController {

    @Autowired
    private MyDocService myDocService;

    /**
     * 添加
     * @param indexName
     * @param parameter2
     * @return
     */
    @PostMapping("/index/add/{indexName}")
    public APIResponse bulkInsertRecord(@PathVariable String indexName, @RequestBody Parameter2 parameter2) {

        return myDocService.bulkInsertRecord(indexName,parameter2.getList());
    }

    /**
     * 修改,效率较低,以后优化
     * @param indexName
     * @param esEntity
     * @return
     */
    @PostMapping("/index/update/{indexName}")
    public APIResponse updateRecord(@PathVariable String indexName, @RequestBody EsEntity esEntity){
        return myDocService.updateRecord(indexName,esEntity);
    }


    @PostMapping("/index/del/{indexName}")
    public APIResponse del(@PathVariable String indexName, @RequestBody EsEntity esEntity){
        return myDocService.del(indexName,esEntity);
    }


    @PostMapping("/index/query")
    public APIResponse query(@RequestBody SearchEntity searchEntity){
        return myDocService.query(searchEntity);
    }


}
