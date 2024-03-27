package com.esc.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.Result;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchAllQuery;
import co.elastic.clients.elasticsearch.core.GetResponse;
import co.elastic.clients.elasticsearch.core.IndexRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.esc.model.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ElasticsearchService {

    private final ElasticsearchClient elasticsearchClient;

    public String createIndex(String indexName) throws IOException {
        return elasticsearchClient.indices()
                .create(c -> c.index(indexName)).index();
    }

    public Result ingestDocument(String indexName, String id, Product product) throws IOException {
        IndexRequest<Object> indexRequest = IndexRequest.of(
                request -> request
                        .index(indexName)
                        .id(id)
                        .document(product)
        );
        return elasticsearchClient.index(indexRequest).result();
    }

    public Object getProductById(String indexName, String id) throws IOException {
        GetResponse<Product> response = elasticsearchClient.get(
                g -> g.index(indexName).id(id),
                Product.class
        );
        if (response.found()) {
            return response.source();
        } else {
            return "Product Not Found";
        }
    }

    public List<Product> searchDocuments(String indexName, String fieldName, String text) throws IOException {
        List<Product> responseList = new ArrayList<>();
        SearchResponse<Product> searchResponse = elasticsearchClient.search(s -> s
                        .index(indexName)
                        .query(q -> q
                                .term(t -> t
                                        .field(fieldName)
                                        .value(v -> v.stringValue(text))
                                )),
                Product.class);
        for (Hit<Product> hit : searchResponse.hits().hits()) {
            responseList.add(hit.source());
        }
        return responseList;
    }

    public List<Product> getAllDocuments(String indexName) throws IOException {
        List<Product> responseList = new ArrayList<>();
        SearchResponse<Product> searchResponse = elasticsearchClient.search(s -> s
                .index(indexName).query(q -> q.matchAll(MatchAllQuery.of(m -> m.queryName("match_all")))), Product.class);
        for (Hit<Product> hit : searchResponse.hits().hits()) {
            responseList.add(hit.source());
        }
        return responseList;
    }
}
