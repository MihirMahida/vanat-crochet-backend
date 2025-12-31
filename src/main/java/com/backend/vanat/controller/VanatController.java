package com.backend.vanat.controller;

import com.backend.vanat.model.VanatData;
import com.backend.vanat.service.VanatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class VanatController {

    @Value("${ADMIN_API_KEY}")
    private String adminKey;

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
    public ResponseEntity<?> createProduct(@RequestBody VanatData product, @RequestHeader("X-API-KEY") String receivedKey) {

        if (!adminKey.equals(receivedKey)) {
            return ResponseEntity.status(401).body("Unauthorized: Invalid API Key");
        }

        return ResponseEntity.ok(service.save(product));
    }

    @PostMapping("/multipleProducts")
    public ResponseEntity<?> createMultipleProductsProduct(@RequestBody List<VanatData> products, @RequestHeader("X-API-KEY") String receivedKey) {

        if (!adminKey.equals(receivedKey)) {
            return ResponseEntity.status(401).body("Unauthorized: Invalid API Key");
        }

        return ResponseEntity.ok(service.saveAll(products));
    }

    @PostMapping("/uploadImage")
    public ResponseEntity<?> uploadImage(
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("price") Double price,
            @RequestParam("category") String category,
            @RequestParam("image") MultipartFile imageFile,
            @RequestHeader("X-API-KEY") String receivedKey
    ) throws IOException {

        if (!adminKey.equals(receivedKey)) {
            return ResponseEntity.status(401).body("Unauthorized: Invalid API Key");
        }

        VanatData product = new VanatData();
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setCategory(category);
        product.setImageName(imageFile.getOriginalFilename());
        product.setImageType(imageFile.getContentType());
        product.setImageData(imageFile.getBytes());
        return ResponseEntity.ok(service.save(product));
    }

    @PostMapping("/uploadMultipleImages")
    public ResponseEntity<?> uploadMultipleImages(
            @RequestParam("name") List<String> names,
            @RequestParam("description") List<String> descriptions,
            @RequestParam("price") List<Double> prices,
            @RequestParam("category") List<String> categories,
            @RequestParam("image") List<MultipartFile> imageFiles,
            @RequestHeader("X-API-KEY") String receivedKey
    ) throws IOException {

        if (!adminKey.equals(receivedKey)) {
            return ResponseEntity.status(401).body("Unauthorized: Invalid API Key");
        }

        List<VanatData> products = new ArrayList<>();

        for (int i = 0; i < names.size(); i++) {
            VanatData product = new VanatData();
            product.setName(names.get(i));
            product.setDescription(descriptions.get(i));
            product.setPrice(prices.get(i));
            product.setCategory(categories.get(i));

            MultipartFile file = imageFiles.get(i);
            product.setImageName(file.getOriginalFilename());
            product.setImageType(file.getContentType());
            product.setImageData(file.getBytes());

            products.add(product);
        }

        // Ensure this service method is designed to save a List
        service.saveAll(products);
        return ResponseEntity.ok("Successfully uploaded " + products.size() + " products.");
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

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable Integer id, @RequestBody VanatData productDetails, @RequestHeader("X-API-KEY") String receivedKey) {
        if (!adminKey.equals(receivedKey)) {
            return ResponseEntity.status(401).body("Unauthorized: Invalid API Key");
        }
        return service.updateProduct(id, productDetails);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Integer id, @RequestHeader("X-API-KEY") String receivedKey) {
        if (!adminKey.equals(receivedKey)) {
            return ResponseEntity.status(401).body("Unauthorized: Invalid API Key");
        }
        return service.deleteProduct(id);
    }
}
