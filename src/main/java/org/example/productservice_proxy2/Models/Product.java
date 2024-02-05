package org.example.productservice_proxy2.Models;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Product extends BaseModel {
    private String title;
    private String description;
    private String imageUrl;
    private Double price;
    private Category category;
}
