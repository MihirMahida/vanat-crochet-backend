package com.backend.vanat.repo;

import com.backend.vanat.model.VanatData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VanatRepo extends JpaRepository<VanatData, Integer> {
    @Query("SELECT v FROM VanatData v WHERE v.category = ?1")
    List<VanatData> findByCategory(String category);

    List<VanatData> findByCategoryOrderByPriceAsc(String category);
    List<VanatData> findByCategoryOrderByPriceDesc(String category);
}
