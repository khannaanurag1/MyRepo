package org.example.productservice_proxy2.Services;

import org.example.productservice_proxy2.Models.Product;

import java.util.List;

public interface IProductService {
    List<Product> getAllProducts();

    Product getProduct(Long id);

    Product createProduct(Product productDto);

    Product updateProduct(Product product, Long productId);

    String deleteProduct(Long id);
}
