package com.justlife.caseStudy.repository;

import com.justlife.caseStudy.model.VehicleModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VehicleRepository extends CrudRepository<VehicleModel,Long> {
}
