package com.backend.vanat.controller;

import com.backend.vanat.model.VanatData;
import com.backend.vanat.service.VanatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class VanatController {

    @Autowired
    public VanatService service;

    @GetMapping
    public List<VanatData> getAllProducts() {
        return service.getAllProducts();
    }

    @GetMapping("/{id}")
    public ResponseEntity<VanatData> getProductById(@PathVariable Integer id) {
        return service.findById(id);
    }

    @PostMapping
    public VanatData createProduct(@RequestBody VanatData product) {
        return service.save(product);
    }

    @PostMapping("/multipleProducts")
    public List<VanatData> createMultipleProductsProduct(@RequestBody VanatData product) {
        return service.saveMultipleProducts(product);
    }

    @PostMapping("/uploadImage")
    public VanatData uploadImage(
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("price") Double price,
            @RequestParam("category") String category,
            @RequestParam("image") MultipartFile imageFile
    ) throws IOException {
        VanatData product = new VanatData();
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setCategory(category);
        product.setImageName(imageFile.getOriginalFilename());
        product.setImageType(imageFile.getContentType());
        product.setImageData(imageFile.getBytes());
        return service.save(product);
    }

    @PostMapping("/uploadMultipleImages")
    public List<VanatData> uploadMultipleImages(
            @RequestParam("name") List<String> names,
            @RequestParam("description") List<String> descriptions,
            @RequestParam("price") List<Double> prices,
            @RequestParam("category") List<String> categories,
            @RequestParam("image") List<MultipartFile> imageFiles
    ) throws IOException {
        int count = names.size();
        List<VanatData> products = new java.util.ArrayList<>();
        for (int i = 0; i < count; i++) {
            VanatData product = new VanatData();
            product.setName(names.get(i));
            product.setDescription(descriptions.get(i));
            product.setPrice(prices.get(i));
            product.setCategory(categories.get(i));
            MultipartFile imageFile = imageFiles.get(i);
            product.setImageName(imageFile.getOriginalFilename());
            product.setImageType(imageFile.getContentType());
            product.setImageData(imageFile.getBytes());
            products.add(product);
        }
        return service.saveMultipleProducts(products);
    }

    @GetMapping("/category/{category}")
    public List<VanatData> getProductsByCategory(@PathVariable String category) {
        return service.getProductsByCategory(category);
    }

    @GetMapping("/category/{category}/sort")
    public List<VanatData> getProductsByCategorySorted(
            @PathVariable String category,
            @RequestParam(defaultValue = "asc") String sort) {
        return service.getProductsByCategorySorted(category, sort);
    }
}
