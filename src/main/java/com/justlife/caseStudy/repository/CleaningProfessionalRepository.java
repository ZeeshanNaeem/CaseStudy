package com.justlife.caseStudy.repository;

import com.justlife.caseStudy.model.CleaningProfessionalModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CleaningProfessionalRepository extends JpaRepository<CleaningProfessionalModel,Long> {
}
