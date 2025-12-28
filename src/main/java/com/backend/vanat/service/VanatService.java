package com.backend.vanat.service;

import com.backend.vanat.model.VanatData;
import com.backend.vanat.repo.VanatRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VanatService {

    @Autowired
    public VanatRepo repo;

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

    public List<VanatData> saveMultipleProducts(VanatData product) {
        return repo.saveAll(List.of(product));
    }

    public List<VanatData> saveMultipleProducts(List<VanatData> products) {
        return repo.saveAll(products);
    }

    public List<VanatData> getProductsByCategory(String category) {
        return repo.findByCategory(category);
    }

    public List<VanatData> getProductsByCategorySorted(String category, String sort) {
        if ("desc".equalsIgnoreCase(sort)) {
            return repo.findByCategoryOrderByPriceDesc(category);
        }
        return repo.findByCategoryOrderByPriceAsc(category);
    }
}
