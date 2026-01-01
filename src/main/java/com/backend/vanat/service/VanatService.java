package com.backend.vanat.service;

import com.backend.vanat.model.ImageDTO;
import com.backend.vanat.model.VanatData;
import com.backend.vanat.repo.VanatRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class VanatService {

    private static final Logger logger = LoggerFactory.getLogger(VanatService.class);

    private final VanatRepo repo;

    @Autowired
    public VanatService(VanatRepo repo) {
        this.repo = repo;
    }

    public List<VanatData> getAllProducts() {
        return repo.findAll();
    }

    public Optional<VanatData> findById(Integer id) {
        return repo.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<ImageDTO> getImage(Integer id) {
        logger.info("Service: getImage called for id: {}", id);
        Optional<VanatData> productOptional = repo.findById(id);
        if (productOptional.isPresent()) {
            VanatData product = productOptional.get();
            logger.info("Service: Product found for id: {}. Image data present: {}", id, product.getImageData() != null);
            return Optional.of(new ImageDTO(product.getImageData(), product.getImageType()));
        } else {
            logger.warn("Service: Product not found for id: {}", id);
            return Optional.empty();
        }
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