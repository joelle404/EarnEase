package com.joelle.backend.rent;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.joelle.backend.purchase.ProductPurchase;
import com.joelle.backend.purchase.ProductPurchaseRepository;
import com.joelle.backend.staff.Staff;
import com.joelle.backend.staff.StaffRepository;

import jakarta.transaction.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class RentService {
    private final RentRepository rentRepository;
    private final ProductPurchaseRepository purchaseRepository;
    private final StaffRepository staffRepository;

    public RentService(RentRepository rentRepository, ProductPurchaseRepository purchaseRepository, StaffRepository staffRepository) {
        this.rentRepository = rentRepository;
        this.purchaseRepository = purchaseRepository;
        this.staffRepository = staffRepository;
    }

    public Rent saveRent(Rent rent) {
        return rentRepository.save(rent);
    }

    public Optional<Rent> getRent(Long staffId, String month) {
        return rentRepository.findByStaffIdAndMonth(staffId, month);
    }

// RentService.java
@Transactional
public Rent updateRent(Long id, Double amount) {
    Rent rent = rentRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Rent not found"));
    rent.setAmount(amount);
    return rentRepository.save(rent);
}

 // RentService.java

@Scheduled(cron = "0 0 1 1 * *") // Runs 1 AM on the 1st of every month
@Transactional
public void createMonthlyRents() {
    String currentMonth = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM"));

    Long tamerId = 5L; // Example: Tamer’s staff_id in DB
    boolean exists = rentRepository.existsByStaffIdAndMonth(tamerId, currentMonth);

    if (!exists) {
        // Create Rent entry
        Rent rent = new Rent();
        rent.setStaffId(tamerId);
        rent.setMonth(currentMonth);
        rent.setAmount(3000.0); // Tamer’s monthly rent
        rent.setPaidDate(LocalDate.now());
        rentRepository.save(rent);
Staff staff = staffRepository.findById(tamerId)
        .orElseThrow(() -> new RuntimeException("Staff not found"));

ProductPurchase purchase = new ProductPurchase();
purchase.setStaff(staff); 
purchase.setProductName("Rent");
purchase.setAmountSpent(3000.0);
purchase.setDate(LocalDate.now().withDayOfMonth(1).toString());


        purchaseRepository.save(purchase);
    }
}

   // Extracted reusable method
    @Transactional
    public void createRentForStaff(Long staffId, Double amount) {
        String currentMonth = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM"));
        boolean exists = rentRepository.existsByStaffIdAndMonth(staffId, currentMonth);

        if (!exists) {
            Rent rent = new Rent();
            rent.setStaffId(staffId);
            rent.setMonth(currentMonth);
            rent.setAmount(amount);
            rent.setPaidDate(LocalDate.now());
            rentRepository.save(rent);
        }
    }
}
