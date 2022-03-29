package com.example.demo.controller;

import com.example.demo.service.MyDocService;
import com.example.demo.vo.Parameter;
import com.example.demo.vo.Parameter2;
import com.example.demo.vo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author: zyf
 * @create: 2022-03-29 17:55
 **/
@RestController
@RequestMapping("/es")
public class MyDocController {

    @Autowired
    private MyDocService myDocService;

    @PostMapping("/index/insert/{indexName}")
    public APIResponse bulkInsertRecord(@PathVariable String indexName, @RequestBody Parameter2 parameter2) {

        return myDocService.bulkInsertRecord(indexName,parameter2.getList());
    }
}
