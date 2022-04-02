package com.example.demo.vo;

import lombok.Data;

/**
 * @author: zyf
 * @create: 2022-04-01 17:27
 **/
@Data
public class SearchEntity {

    private Integer pageNo;

    private Integer pageSize;

    private String keyWord;
}
