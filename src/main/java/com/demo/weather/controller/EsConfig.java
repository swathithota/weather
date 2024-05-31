package com.demo.weather.controller;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EsConfig {

  @Value("${es.ELASTIC_SEARCH_HOST}")
  private String elasticSearchHost;

  @Value("${es.ELASTIC_SEARCH_USER_NAME}")
  private String elasticSearchUserName;

  @Value("${es.ELASTIC_SEARCH_PASSWORD}")
  private String elasticSearchPassword;

  @Value("${es.ELASTIC_SEARCH_TABLE}")
  private String elasticSearchTable;

  @Value("${es.ELASTIC_SEARCH_PORT}")
  private String elasticSearchPort;

  @Bean(destroyMethod = "close")
  public RestHighLevelClient restHighLevelClient() {

    final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
    credentialsProvider.setCredentials(
        AuthScope.ANY,
        new UsernamePasswordCredentials(elasticSearchUserName, elasticSearchPassword));

    return new RestHighLevelClient(
        RestClient.builder(new HttpHost(elasticSearchHost, Integer.valueOf(elasticSearchPort)))
            .setHttpClientConfigCallback(
                httpClientBuilder -> {
                  return httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
                }));
  }
}
