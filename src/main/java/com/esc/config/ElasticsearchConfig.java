package com.esc.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.TransportUtils;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.net.ssl.SSLContext;

@Configuration
public class ElasticsearchConfig {

    private static final String HTTP_CA_FINGERPRINT = "67f5d502777cfa0b3fd632ff1284f2303cf767c82b4a2d4860b4e078263378da";

    @Bean
    public RestClient restClient() {
        SSLContext sslContext = TransportUtils.sslContextFromCaFingerprint(HTTP_CA_FINGERPRINT);
        BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials("elastic", "x=2IAP=-5bwhPT95TL98"));

        return RestClient.builder(new HttpHost("localhost", 9200, "https"))
                .setHttpClientConfigCallback(
                        hc -> hc
                                .setSSLContext(sslContext)
                                .setDefaultCredentialsProvider(credentialsProvider)
                ).build();
    }

    @Bean
    public ElasticsearchTransport elasticsearchTransport() {
        return new RestClientTransport(restClient(), new JacksonJsonpMapper());
    }

    @Bean
    public ElasticsearchClient elasticsearchClient() {
        return new ElasticsearchClient(elasticsearchTransport());
    }
}
