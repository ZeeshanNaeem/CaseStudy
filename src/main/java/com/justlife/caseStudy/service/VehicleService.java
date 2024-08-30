package com.justlife.caseStudy.service;

import com.justlife.caseStudy.model.VehicleModel;
import com.justlife.caseStudy.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class VehicleService {
    @Autowired
    private VehicleRepository vehicleRepository;
    public VehicleModel createVehicle(VehicleModel vehicleModel) {
        VehicleModel model = vehicleRepository.save(vehicleModel);
        return model;
    }

    public Optional<VehicleModel> getVehicleById(Long vehicleId) {
        Optional<VehicleModel> vehicleModel = vehicleRepository.findById(vehicleId);
        return vehicleModel;
    }
}
