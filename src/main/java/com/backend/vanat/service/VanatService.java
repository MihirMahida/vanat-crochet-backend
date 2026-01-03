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

    // Fetch products WITHOUT images (fast)
    @Transactional(readOnly = true)
    public List<VanatData> getAllProducts() {
        return repo.findAllWithoutImage();
    }

    // Fetch single product WITH image data
    @Transactional(readOnly = true)
    public Optional<VanatData> findById(Integer id) {
        return repo.findById(id);
    }

    // Fetch ONLY image data (optimized)
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

    @Transactional
    public VanatData save(VanatData product) {
        return repo.save(product);
    }

    @Transactional
    public List<VanatData> saveAll(List<VanatData> products) {
        return repo.saveAll(products);
    }

    // Fetch products by category WITHOUT images (fast)
    @Transactional(readOnly = true)
    public List<VanatData> getProductsByCategorySorted(String category, String sort) {
        logger.info("Service: getProductsByCategorySorted called for category: {}, sort: {}", category, sort);
        List<VanatData> products;
        if ("desc".equalsIgnoreCase(sort)) {
            products = repo.findByCategoryOrderByPriceDescWithoutImage(category);
        } else {
            products = repo.findByCategoryOrderByPriceAscWithoutImage(category);
        }
        logger.info("Service: Found {} products for category: {}", products.size(), category);
        return products;
    }

    // Fetch all products sorted WITHOUT images (fast)
    @Transactional(readOnly = true)
    public List<VanatData> getAllProductsSorted(String sort) {
        logger.info("Service: getAllProductsSorted called with sort: {}", sort);
        List<VanatData> products;
        if ("desc".equalsIgnoreCase(sort)) {
            products = repo.findAllByOrderByPriceDescWithoutImage();
        } else {
            products = repo.findAllByOrderByPriceAscWithoutImage();
        }
        logger.info("Service: Found {} products after sorting", products.size());
        return products;
    }

    @Transactional
    public ResponseEntity<?> deleteProduct(Integer id) {
        if (repo.existsById(id)) {
            repo.deleteById(id);
            return ResponseEntity.ok().body("Product with id " + id + " has been deleted.");
        }
        return ResponseEntity.notFound().build();
    }

    @Transactional
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