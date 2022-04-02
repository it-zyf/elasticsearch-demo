package com.example.demo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;

@Data
public class EsEntity {

    /**
     * 漏洞，资产，工具，补丁
     */
    private String dataSource;

    /**
     * id
     */
    private String id;


    /**
     * 数据来源 众测，情报，漏扫
     */
    private String platform;

    /**
     * 数据类型 ，事件，通用
     */
    private Integer type;

    /**
     * 漏洞id
     */
    private String loopId;

    /**
     * 名称
     */
    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_smart")
    private String loopTitle;

    /**
     * 编号
     */
    private String loopCode;

    /**
     * 漏洞类型
     */
    private String loopType;

    /**
     * 等级
     */
    private String loopLevel;

    /**
     * 发现时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date findTime;

    /**
     * 录入时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date recordingTime;

    /**
     * 资产名称
     */
    private String assetsName;

    /**
     * 资产ip
     */
    private String assetsIP;

    /**
     * 补丁名称
     */
    private String patchName;

    /**
     * 工具名称
     */
    private String toolName;

    /**
     * 知识标题
     */
    private String knowledgeName;

    /*
    ---------------资产独有--------------
     */
    /**
     * 资产地址
     */
    private String assetsAddress;

    /**
     * 资产类型
     */
    private String assetsType;

    /**
     * 所属单位
     */
    private String webOrg;


    /**
     * 漏洞总数
     */
    private Integer loopCount;


    /**
     * web it
     */
    private String webOrIt;


    /**
     * 主机名称
     */
    private String hostName;


    /**
     * 操作系统
     */
    private String os;


    /**
     * 风险评分
     */
    private Double riskScore;


    /*
       ---------------补丁独有--------------
        */
    /**
     * 补丁编号     录入是可能为情报补丁链接
     */
    private String patchNo;

    /**
     * 补丁大小    补丁附件大小
     */
    private String patchSize;

    /**
     * 重要级别     为漏洞的等级
     */
    private String riskLevel;


    /**
     * CVE漏洞编号
     */
    private String cveNo;

    /**
     * 发布时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date releaseTime;
}
