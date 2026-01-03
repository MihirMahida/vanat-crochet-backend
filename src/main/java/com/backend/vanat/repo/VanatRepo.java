package com.backend.vanat.repo;

import com.backend.vanat.model.VanatData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VanatRepo extends JpaRepository<VanatData, Integer> {

    // Fetch products WITHOUT image data for list views (much faster)
    @Query("SELECT new com.backend.vanat.model.VanatData(v.id, v.name, v.description, v.price, v.category, v.imageName, v.imageType) FROM VanatData v ORDER BY v.price ASC")
    List<VanatData> findAllByOrderByPriceAscWithoutImage();

    @Query("SELECT new com.backend.vanat.model.VanatData(v.id, v.name, v.description, v.price, v.category, v.imageName, v.imageType) FROM VanatData v ORDER BY v.price DESC")
    List<VanatData> findAllByOrderByPriceDescWithoutImage();

    @Query("SELECT new com.backend.vanat.model.VanatData(v.id, v.name, v.description, v.price, v.category, v.imageName, v.imageType) FROM VanatData v WHERE v.category = :category ORDER BY v.price ASC")
    List<VanatData> findByCategoryOrderByPriceAscWithoutImage(@Param("category") String category);

    @Query("SELECT new com.backend.vanat.model.VanatData(v.id, v.name, v.description, v.price, v.category, v.imageName, v.imageType) FROM VanatData v WHERE v.category = :category ORDER BY v.price DESC")
    List<VanatData> findByCategoryOrderByPriceDescWithoutImage(@Param("category") String category);

    @Query("SELECT new com.backend.vanat.model.VanatData(v.id, v.name, v.description, v.price, v.category, v.imageName, v.imageType) FROM VanatData v")
    List<VanatData> findAllWithoutImage();
}