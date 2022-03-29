package com.example.demo.service;

import org.elasticsearch.client.indices.CreateIndexRequest;

import java.io.IOException;

public interface IndexService {
    void create(String index, CreateIndexRequest request) throws Exception;
    boolean exists(String index) throws Exception;
}

