package com.joelle.backend.katiawork;


import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class KatiaWorkService {

    private final KatiaWorkRepository repository;

    public KatiaWorkService(KatiaWorkRepository repository) {
        this.repository = repository;
    }

    public KatiaWork createWork(KatiaWork work) {
        work.calculateCuts();
        return repository.save(work);
    }

    public List<KatiaWork> getAllWork() {
        return repository.findAll();
    }

    public KatiaWork getWorkById(Long id) {
        return repository.findById(id).orElse(null);
    }



    
public Double getShareInRange(Long staffId, LocalDate from, LocalDate to) {
    List<KatiaWork> works = repository.findAll();

    return works.stream()
            .filter(work -> {
                LocalDate d = work.getServiceDate();
                return !d.isBefore(from) && !d.isAfter(to);
            })
            .mapToDouble(work -> {
                // Example: staffId == 2 is Tamer → he gets tamerCut
                if (staffId == 5L) {
                    return work.getTamerCut() != null ? work.getTamerCut() : 0.0;
                }
                if (staffId == 1L) {
                    return work.getDimaCut() != null ? work.getDimaCut() : 0.0;
                }
                // Katia keeps her net, but if you need her share too:
                // if (staffId == 3L) {
                //     return work.getKatiaNet() != null ? work.getKatiaNet() : 0.0;
                // }
                return 0.0;
            })
            .sum();
}
}
