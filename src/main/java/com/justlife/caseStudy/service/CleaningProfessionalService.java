package com.justlife.caseStudy.service;

import com.justlife.caseStudy.model.CleaningProfessionalModel;
import com.justlife.caseStudy.model.VehicleModel;
import com.justlife.caseStudy.repository.CleaningProfessionalRepository;
import com.justlife.caseStudy.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CleaningProfessionalService {
    @Autowired
    private CleaningProfessionalRepository cleaningProfessionalRepository;

    public CleaningProfessionalModel createCleaningProfessional(CleaningProfessionalModel cleaningProfessionalModel) {
        return cleaningProfessionalRepository.save(cleaningProfessionalModel);
    }

    public Optional<CleaningProfessionalModel> getCleaningProfessionalById(Long id) {
        Optional<CleaningProfessionalModel> model = cleaningProfessionalRepository.findById(id);
        return model;
    }
}
