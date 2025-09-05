package com.joelle.backend.rent;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.Argument;

import java.time.LocalDate;

@Component
public class RentResolver {

    @Autowired
    private RentService rentService;

    @MutationMapping
    public Rent addRent(
            @Argument Long staffId,
            @Argument String month,
            @Argument Double amount
    ) {
        Rent rent = new Rent();
        rent.setStaffId(staffId);
        rent.setMonth(month);
        rent.setAmount(amount);
        rent.setPaidDate(LocalDate.now());
        return rentService.saveRent(rent);
    }

    @QueryMapping
    public Rent getRent(
            @Argument Long staffId,
            @Argument String month
    ) {
        return rentService.getRent(staffId, month).orElse(null);
    }

    // RentResolver.java
@MutationMapping
public Rent updateRent(@Argument Long id, @Argument Double amount) {
    return rentService.updateRent(id, amount);
}

}
