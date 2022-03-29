package com.example.demo.vo;

import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;

import java.io.Serializable;

/**
 * @author: zyf
 * @create: 2022-03-29 17:57
 **/
@Data
@Document(indexName = "user")
public class User implements Serializable {
    private String name;

    private String age;

    private String sex;

}
