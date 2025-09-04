package com.joelle.backend.katiawork;


import org.springframework.stereotype.Service;

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
}
