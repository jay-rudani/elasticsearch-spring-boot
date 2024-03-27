package com.esc.controller;

import co.elastic.clients.elasticsearch._types.Result;
import com.esc.model.Product;
import com.esc.service.ElasticsearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/app")
@RequiredArgsConstructor
public class SampleController {

    private final ElasticsearchService elasticsearchService;

    @PostMapping("/index/create/{indexName}")
    public String createIndex(@PathVariable String indexName) throws IOException {
        return elasticsearchService.createIndex(indexName);
    }

    @PostMapping("/document/ingest/{indexName}/{id}")
    public Result ingestDocument(@PathVariable String indexName, @PathVariable String id, @RequestBody Product product) throws IOException {
        return elasticsearchService.ingestDocument(indexName, id, product);
    }

    @GetMapping("/document/get-by-id/{indexName}/{id}")
    public Object getProductById(@PathVariable String indexName, @PathVariable String id) throws IOException {
        return elasticsearchService.getProductById(indexName, id);
    }

    @GetMapping("/documents/find/{indexName}/{fieldName}/{text}")
    public List<Product> searchByName(@PathVariable String indexName, @PathVariable String fieldName, @PathVariable String text) throws IOException {
        return elasticsearchService.searchDocuments(indexName, fieldName, text);
    }

    @GetMapping("/documents/{indexName}")
    public List<Product> getAllDocuments(@PathVariable String indexName) throws IOException {
        return elasticsearchService.getAllDocuments(indexName);
    }
}
