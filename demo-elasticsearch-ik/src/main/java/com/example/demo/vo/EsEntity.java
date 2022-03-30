package com.example.demo.vo;

import lombok.Data;

/**
 * @author: zyf
 * @create: 2022-03-30 10:47
 **/
@Data
public class EsEntity {
    //数据来源 综测，情报，漏扫
    private String platform;

    //数据类型 ，事件，通用
    private String type;


    //id
    private String loopId;

    //名称
    private String loopTitle;

    //编号
    private String loopNo;

    //漏洞类型
    private String loopType;

    //等级
    private String loopLevel;

    //发现时间
    private String findTime;

    //公布时间
    private String releaseTime;

    //资产名称
    private String assetsName;

    //资产ip
    private String assetsIP;

    //补丁名称
    private String patchName;

    //工具名称
    private String toolName;

    //知识标题
    private String knowledgeName;

    private String name;

    private Integer age;
}
