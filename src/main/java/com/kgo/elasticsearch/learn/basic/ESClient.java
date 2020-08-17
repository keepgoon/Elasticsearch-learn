package com.kgo.elasticsearch.learn.basic;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ESClient {
    private Logger logger = LoggerFactory.getLogger(ESClient.class);
    private static ESClient instance;
    private RestClient client;

    public static ESClient getInstance(String hosts, String userName, String passWord) {
        if (instance == null) {
            synchronized (ESClient.class) {
                if (instance == null) {
                    instance = new ESClient(hosts, userName, passWord);
                }
            }
        }
        return instance;
    }
    public RestClient getClient(){
        return client;
    }

    /**
     * 带密码认证的
     *
     * @param hosts
     * @param userName
     * @param passWord
     */
    public ESClient(String hosts, String userName, String passWord) {
        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(userName, passWord));  //es账号密码
        String[] hostsAndPorts = hosts.split(",");
        HttpHost[] httpHosts = new HttpHost[hostsAndPorts.length];
        for (int i = 0; i < hostsAndPorts.length; i++) {
            httpHosts[i] = HttpHost.create(hostsAndPorts[i]);
        }
        client = RestClient.builder(httpHosts).setHttpClientConfigCallback(new RestClientBuilder.HttpClientConfigCallback() {
            @Override
            public HttpAsyncClientBuilder customizeHttpClient(HttpAsyncClientBuilder httpClientBuilder) {
                httpClientBuilder.disableAuthCaching();
                return httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
            }
        }).build();
    }

}
