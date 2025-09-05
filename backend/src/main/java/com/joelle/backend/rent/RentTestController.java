package com.joelle.backend.rent;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class RentTestController {

    private final RentService rentService;

    public RentTestController(RentService rentService) {
        this.rentService = rentService;
    }

    @PostMapping("/createRent")
    public String createRentManually(@RequestParam Long staffId, @RequestParam Double amount) {
        rentService.createRentForStaff(staffId, amount);
        return "Rent created/checked for staff " + staffId;
    }
}
