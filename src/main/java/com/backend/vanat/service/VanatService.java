package com.backend.vanat.service;

import com.backend.vanat.model.VanatData;
import com.backend.vanat.repo.VanatRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VanatService {

    private final VanatRepo repo;

    @Autowired
    public VanatService(VanatRepo repo) {
        this.repo = repo;
    }

    public List<VanatData> getAllProducts() {
        return repo.findAll();
    }

    public ResponseEntity<VanatData> findById(Integer id) {
        return repo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    public VanatData save(VanatData product) {
        return repo.save(product);
    }

    public List<VanatData> saveAll(List<VanatData> products) {
        return repo.saveAll(products);
    }

    public List<VanatData> getProductsByCategorySorted(String category, String sort) {
        if ("desc".equalsIgnoreCase(sort)) {
            return repo.findByCategoryOrderByPriceDesc(category);
        }
        return repo.findByCategoryOrderByPriceAsc(category);
    }

    public List<VanatData> getAllProductsSorted(String sort) {
        if ("desc".equalsIgnoreCase(sort)) {
            return repo.findAllByOrderByPriceDesc();
        }
        return repo.findAllByOrderByPriceAsc();
    }

    public ResponseEntity<?> deleteProduct(Integer id) {
        if (repo.existsById(id)) {
            repo.deleteById(id);
            return ResponseEntity.ok().body("Product with id " + id + " has been deleted.");
        }
        return ResponseEntity.notFound().build();
    }

    public ResponseEntity<VanatData> updateProduct(Integer id, VanatData productDetails) {
        return repo.findById(id)
                .map(product -> {
                    product.setName(productDetails.getName());
                    product.setDescription(productDetails.getDescription());
                    product.setPrice(productDetails.getPrice());
                    product.setCategory(productDetails.getCategory());
                    if (productDetails.getImageData() != null) {
                        product.setImageData(productDetails.getImageData());
                        product.setImageName(productDetails.getImageName());
                        product.setImageType(productDetails.getImageType());
                    }
                    return ResponseEntity.ok(repo.save(product));
                })
                .orElse(ResponseEntity.notFound().build());
    }
}