package org.example.productservice_proxy2.Services;

import org.example.productservice_proxy2.Clients.FakeStore.Client.FakeStoreApiClient;
import org.example.productservice_proxy2.Clients.FakeStore.Dtos.FakeStoreClientProductDto;
import org.example.productservice_proxy2.Models.Category;
import org.example.productservice_proxy2.Models.Product;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import java.util.ArrayList;
import java.util.List;

@Service
public class FakeStoreProductService implements IProductService {
    RestTemplateBuilder restTemplateBuilder;
    FakeStoreApiClient fakeStoreApiClient;

    public FakeStoreProductService(RestTemplateBuilder restTemplateBuilder, FakeStoreApiClient fakeStoreApiClient) {
        this.restTemplateBuilder = restTemplateBuilder;
        this.fakeStoreApiClient = fakeStoreApiClient;
    }

    @Override
    public List<Product> getAllProducts() {
        RestTemplate restTemplate = restTemplateBuilder.build();
        ResponseEntity<FakeStoreClientProductDto[]> responseEntityProductDtos = restTemplate.getForEntity("https://fakestoreapi.com/products", FakeStoreClientProductDto[].class);
        List<Product> products = new ArrayList<>();
        for(FakeStoreClientProductDto fakeStoreProductDto : responseEntityProductDtos.getBody()) {
            products.add(getProduct(fakeStoreProductDto));
        }
        return products;
    }

    @Override
    public Product getProduct(Long id) {
        //RestTemplate restTemplate = restTemplateBuilder.build();
        //ResponseEntity<FakeStoreClientProductDto> responseEntityFSProductDto = restTemplate.getForEntity("https://fakestoreapi.com/products/{id}", FakeStoreClientProductDto.class,id);
        //Product product = getProduct(responseEntityFSProductDto.getBody());
        FakeStoreClientProductDto fakeStoreClientProductDto = fakeStoreApiClient.getProduct(id);
        Product product = getProduct(fakeStoreClientProductDto);
        return product;
    }

    @Override
    public Product createProduct(Product product) {
        RestTemplate restTemplate = restTemplateBuilder.build();
        FakeStoreClientProductDto fakeStoreClientProductDto = getFakeStoreProduct(product);
        ResponseEntity<FakeStoreClientProductDto> fakeStoreClientProductDtoResponseEntity = restTemplate.postForEntity("https://fakestoreapi.com/products",fakeStoreClientProductDto, FakeStoreClientProductDto.class);
        return getProduct(fakeStoreClientProductDtoResponseEntity.getBody());
    }

//    public Product updateProductUsingInBuiltMethod(Product product, Long productId) {
//        RestTemplate restTemplate = restTemplateBuilder.build();
//        FakeStoreClientProductDto fakeStoreClientProductDto = getFakeStoreProduct(product);
//        FakeStoreClientProductDto fakeStoreClientProductDto1 = restTemplate.patchForObject("https://fakestoreapi.com/products",fakeStoreClientProductDto, FakeStoreClientProductDto.class);
//        return getProduct(fakeStoreClientProductDto1);
//    }

    @Override
    public Product updateProduct(Product product, Long productId) {
//        return updateProductUsingInBuiltMethod(product,productId); WILL NOT WORK, GIVE RUNTIME EXCEPTION 404 NOT FOUND
        FakeStoreClientProductDto fakeStoreClientProductDto = getFakeStoreProduct(product);
        ResponseEntity<FakeStoreClientProductDto> fakeStoreClientProductDtoResponseEntity = patchForEntity(HttpMethod.PATCH,
                "https://fakestoreapi.com/products/{id}",
                fakeStoreClientProductDto,
                FakeStoreClientProductDto.class,
                productId);

        return getProduct(fakeStoreClientProductDtoResponseEntity.getBody());
    }


    @Override
    public String deleteProduct(Long id) {
        return null;
    }


    public <T> ResponseEntity<T> patchForEntity(HttpMethod httpMethod,String url, @Nullable Object request, Class<T> responseType, Object... uriVariables) throws RestClientException {
        RestTemplate restTemplate = restTemplateBuilder.build();
        RequestCallback requestCallback = restTemplate.httpEntityCallback(request, responseType);
        ResponseExtractor<ResponseEntity<T>> responseExtractor = restTemplate.responseEntityExtractor(responseType);
        return restTemplate.execute(url, httpMethod, requestCallback, responseExtractor, uriVariables);
    }

    //Alternative of PatchForEntity if some exception comes up in Patch Method
    private <T> ResponseEntity<T> requestForEntity(HttpMethod httpMethod, String url, @Nullable Object request,
                                                   Class<T> responseType, Object... uriVariables) throws RestClientException {
        RestTemplate restTemplate = restTemplateBuilder.requestFactory(
                HttpComponentsClientHttpRequestFactory.class
        ).build();
        RequestCallback requestCallback = restTemplate.httpEntityCallback(request, responseType);
        ResponseExtractor<ResponseEntity<T>> responseExtractor = restTemplate.responseEntityExtractor(responseType);
        return restTemplate.execute(url, httpMethod, requestCallback, responseExtractor, uriVariables);
    }

    private Product getProduct(FakeStoreClientProductDto productDto) {
        Product product = new Product();
        product.setTitle(productDto.getTitle());
        product.setDescription(productDto.getDescription());
        product.setImageUrl(productDto.getImage());
        product.setPrice(productDto.getPrice());
        product.setId(productDto.getId());
        Category category = new Category();
        category.setName(productDto.getCategory());
        product.setCategory(category);
        return product;
    }

    private FakeStoreClientProductDto getFakeStoreProduct(Product product) {
        FakeStoreClientProductDto fakeStoreClientProductDto = new FakeStoreClientProductDto();
        fakeStoreClientProductDto.setCategory(product.getCategory().getName());
        fakeStoreClientProductDto.setPrice(product.getPrice());
        fakeStoreClientProductDto.setTitle(product.getTitle());
        fakeStoreClientProductDto.setDescription(product.getDescription());
        fakeStoreClientProductDto.setId(product.getId());
        fakeStoreClientProductDto.setImage(product.getImageUrl());
        return fakeStoreClientProductDto;
    }
}
