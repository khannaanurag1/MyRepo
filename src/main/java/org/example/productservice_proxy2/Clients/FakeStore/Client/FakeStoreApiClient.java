package org.example.productservice_proxy2.Clients.FakeStore.Client;

import org.example.productservice_proxy2.Clients.FakeStore.Dtos.FakeStoreClientProductDto;
import org.example.productservice_proxy2.Models.Product;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class FakeStoreApiClient {
    private RestTemplateBuilder restTemplateBuilder;

    public FakeStoreApiClient(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplateBuilder = restTemplateBuilder;
    }

    public FakeStoreClientProductDto getProduct(Long id) {
        RestTemplate restTemplate = restTemplateBuilder.build();
        ResponseEntity<FakeStoreClientProductDto> responseEntityFSProductDto = restTemplate.getForEntity("https://fakestoreapi.com/products/{id}", FakeStoreClientProductDto.class,id);
        return responseEntityFSProductDto.getBody();
    }
}
