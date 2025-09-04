package com.joelle.backend.katiawork;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KatiaWorkRepository extends JpaRepository<KatiaWork, Long> {
}
