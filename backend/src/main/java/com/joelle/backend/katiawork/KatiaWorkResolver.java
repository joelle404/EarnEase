package com.joelle.backend.katiawork;
 
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

import java.time.LocalDate;
import java.util.List;

@Controller
public class KatiaWorkResolver {

    private final KatiaWorkService service;

    public KatiaWorkResolver(KatiaWorkService service) {
        this.service = service;
    }
@PreAuthorize("isAuthenticated()")
    @QueryMapping
    public List<KatiaWork> getAllKatiaWork() {
        return service.getAllWork();
    }
@PreAuthorize("isAuthenticated()")
    @QueryMapping
    public KatiaWork getKatiaWorkById(@Argument Long id) {
        return service.getWorkById(id);
    }
    @PreAuthorize("isAuthenticated()")
@MutationMapping
public Boolean deleteKatiaWork(@Argument Long id) {
    try {
        return service.deleteWork(id); // delegate to service
    } catch (Exception e) {
        return false;
    }
}

@PreAuthorize("isAuthenticated()")
    @MutationMapping
    public KatiaWork createKatiaWork(@Argument KatiaWorkInput input) {
        KatiaWork work = new KatiaWork();
        work.setClientName(input.clientName());
        work.setServiceDate(LocalDate.parse(input.serviceDate()));
        work.setGrossAmount(input.grossAmount());
        work.setTamerPercent(input.tamerPercent());
        return service.createWork(work);
    }

    // DTO record for input
    public record KatiaWorkInput(String clientName, String serviceDate, Double grossAmount, Double tamerPercent) {}
}
