package com.joelle.backend.purchase;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.joelle.backend.staff.Staff;
import com.joelle.backend.staff.StaffRepository;

@Controller
public class ProductPurchaseResolver {
    
    private final ProductPurchaseRepository purchaseRepository;
    private final StaffRepository staffRepository;

    @Autowired
    public ProductPurchaseResolver(ProductPurchaseRepository purchaseRepository,
                                   StaffRepository staffRepository) {
        this.purchaseRepository = purchaseRepository;
        this.staffRepository = staffRepository;
    }

    // @QueryMapping
    // public List<ProductPurchase> allProductPurchases() {
    //     return purchaseRepository.findAll();
    // }

    @QueryMapping
    public ProductPurchase allProductPurchases(@Argument Long staffId) {
        return purchaseRepository.findByStaffId(staffId,Sort.by(Sort.Direction.DESC, "date", "time"));
    }

    @MutationMapping
    public ProductPurchase createProductPurchase(@Argument Long staffId,
                                                 @Argument String productName,
                                                 @Argument Double amountSpent,
                                                 @Argument LocalDateTime date) {
        Staff staff = staffRepository.findById(staffId).orElse(null);
        if (staff == null) {
            System.out.println("Invalid staff ID: " + staffId);
            return null;
        }

        ProductPurchase purchase = new ProductPurchase(staff, productName, amountSpent, date);
        try {
            return purchaseRepository.save(purchase);
        } catch (Exception e) {
            System.out.println("Error saving product purchase: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
