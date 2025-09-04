package com.joelle.backend.purchase;

import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductPurchaseRepository extends JpaRepository<ProductPurchase, Long> {

    List<ProductPurchase> findByStaffId(Long staffId, Sort sort);
    List<ProductPurchase> findByStaffId(Long staffId);

    List<ProductPurchase> findByDateBetween(String startDate, String endDate, Sort sort);

    List<ProductPurchase> findByStaffIdAndDateBetween(Long staffId, String startDate, String endDate, Sort sort);
}
