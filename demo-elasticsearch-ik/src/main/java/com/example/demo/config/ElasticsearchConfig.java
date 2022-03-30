package com.example.demo.config;


import org.apache.http.HttpHost;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author v-zhangyafeng3
 */
@Configuration
public class ElasticsearchConfig {
    /**
     * 使用的协议
     */
    final static String schema = "http";
    /**
     * 连接超时时间
     */
    final static int connectTimeOut = 1000;
    /**
     * 连接超时时间
     */
    final static int socketTimeOut = 30000;
    /**
     * 获取连接的超时时间
     */
    final static int connectionRequestTimeOut = 500;
    /**
     * 最大连接数
     */
    final static int maxConnectNum = 100;
    /**
     * 最大路由连接数
     */
    final static int maxConnectPerRoute = 100;

    @Value("${spring.elasticsearch.host}")
    private String host;

    @Value("${spring.elasticsearch.port}")
    private Integer port;



    @Bean(name = "restHighLevelClient")
    public RestHighLevelClient client() {
        RestClientBuilder builder = RestClient.builder(new HttpHost(host, port, "http"));

        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
//        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(username, password));

        builder.setHttpClientConfigCallback(httpClientBuilder -> {
            httpClientBuilder.disableAuthCaching();
            return httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
        });

        // 异步httpclient连接延时配置
        builder.setRequestConfigCallback(requestConfigBuilder -> {
            requestConfigBuilder.setConnectTimeout(connectTimeOut);
            requestConfigBuilder.setSocketTimeout(socketTimeOut);
            requestConfigBuilder.setConnectionRequestTimeout(connectionRequestTimeOut);
            return requestConfigBuilder;
        });

        // 异步httpclient连接数配置
        builder.setHttpClientConfigCallback(httpClientBuilder -> {
            httpClientBuilder.setMaxConnTotal(maxConnectNum);
            httpClientBuilder.setMaxConnPerRoute(maxConnectPerRoute);
            return httpClientBuilder;
        });
        return new RestHighLevelClient(builder);
    }

//    List<HttpHost> hostList(){
//        List<HttpHost> nodeList = new ArrayList<>();
//        Arrays.asList(nodes).stream().forEach(each -> {
//            String[] array = each.split(":");
//            nodeList.add(new HttpHost(array[0],Integer.valueOf(array[1]),schema));
//        });
//        return nodeList;
//    }

}