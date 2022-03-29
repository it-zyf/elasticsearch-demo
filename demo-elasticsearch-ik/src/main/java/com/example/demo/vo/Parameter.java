package com.example.demo.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author: zyf
 * @create: 2022-03-29 10:08
 **/
@Data
public class Parameter implements Serializable {
    private List<Map<String, Object>> docList;
}
