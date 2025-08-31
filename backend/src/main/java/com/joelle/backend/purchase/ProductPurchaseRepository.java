package com.joelle.backend.purchase;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductPurchaseRepository extends JpaRepository<ProductPurchase,Long>{

    ProductPurchase findByStaffId(Long staffId, Sort by);

    

    
}
