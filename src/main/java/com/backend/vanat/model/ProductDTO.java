package com.backend.vanat.model;

import lombok.Data;

@Data
public class ProductDTO {
    private int id;
    private String name;
    private String description;
    private Double price;
    private String category;
    private String imageUrl;

    public ProductDTO(VanatData vanatData) {
        this.id = vanatData.getId();
        this.name = vanatData.getName();
        this.description = vanatData.getDescription();
        this.price = vanatData.getPrice();
        this.category = vanatData.getCategory();
        this.imageUrl = "/api/products/images/" + vanatData.getId(); // Assuming this is the new endpoint
    }
}
