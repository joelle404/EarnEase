package com.joelle.backend.purchase;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

import com.joelle.backend.staff.Staff;
import com.joelle.backend.staff.StaffRepository;

@Controller
public class ProductPurchaseResolver {
    
    private final ProductPurchaseRepository purchaseRepository;
    private final StaffRepository staffRepository;
    private final ProductPurchaseService purchaseService;

    @Autowired
    public ProductPurchaseResolver(ProductPurchaseRepository purchaseRepository,
                                   StaffRepository staffRepository, ProductPurchaseService purchaseService) {
        this.purchaseRepository = purchaseRepository;
        this.staffRepository = staffRepository;
        this.purchaseService = purchaseService;
    }

    // @QueryMapping
    // public List<ProductPurchase> allProductPurchases() {
    //     return purchaseRepository.findAll();
    // }
@PreAuthorize("isAuthenticated()")
@QueryMapping
public List<ProductPurchase> allProductPurchases(@Argument Long staffId) {
    List<ProductPurchase> purchases = purchaseRepository.findByStaffId(staffId, Sort.by(Sort.Direction.DESC, "date"));
    return purchases != null ? purchases : List.of(); // return empty list if null
}


@PreAuthorize("isAuthenticated()")
    @MutationMapping
    public ProductPurchase createProductPurchase(@Argument Long staffId,
                                                 @Argument String productName,
                                                 @Argument Double amountSpent,
                                                 @Argument String date) {
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
    @PreAuthorize("isAuthenticated()")
  @QueryMapping
    public Double getTotalPurchasesAmount(@Argument Long staffId) {
        return purchaseService.getTotalAmount(staffId);
    }
@PreAuthorize("isAuthenticated()")
    @QueryMapping
    public Double getSumPurchasesLastWeek(@Argument Long staffId) {
        return purchaseService.getSumLastWeek(staffId);
    }
@PreAuthorize("isAuthenticated()")
    @QueryMapping
    public Double getSumPurchasesLastMonth(@Argument Long staffId) {
        return purchaseService.getSumLastMonth(staffId);
    }
@PreAuthorize("isAuthenticated()")
    @QueryMapping
    public Double getSumPurchasesLastYear(@Argument Long staffId) {
        return purchaseService.getSumLastYear(staffId);
    }
    @PreAuthorize("isAuthenticated()")
    @QueryMapping
public List<MonthlyExpensesDTO> getMonthlyExpenses(@Argument Long staffId) {
    Map<String, Double> data = purchaseService.getMonthlyExpenses(staffId);
    return data.entrySet().stream()
               .map(e -> new MonthlyExpensesDTO(e.getKey(), e.getValue()))
               .toList();
}
@PreAuthorize("isAuthenticated()")
@MutationMapping
public Boolean deleteProductPurchase(@Argument Long id) {
    try {
        if (purchaseRepository.existsById(id)) {
            purchaseRepository.deleteById(id);
            return true;
        } else {
            System.out.println("Purchase not found with id: " + id);
            return false;
        }
    } catch (Exception e) {
        System.out.println("Error deleting purchase: " + e.getMessage());
        e.printStackTrace();
        return false;
    }
}

}
