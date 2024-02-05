package org.example.productservice_proxy2.Models;

import com.fasterxml.jackson.databind.ser.Serializers;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Category extends BaseModel {
    private String name;
    private String description;
    private List<Product> products;
}
