package com.justlife.caseStudy.controller;

import com.justlife.caseStudy.model.VehicleModel;
import com.justlife.caseStudy.service.VehicleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/vehicle")
public class VehicleController {

    @Autowired
    private VehicleService vehicleService;

    @Operation(summary = "Create new vehicle", description = "Create new vehicle.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully created vehicle")
    })
    @PostMapping
    public ResponseEntity<VehicleModel> createVehicle(@RequestBody VehicleModel vehicleModel) {
        VehicleModel model = vehicleService.createVehicle(vehicleModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(model);
    }


    @Operation(summary = "Get Vehicle by id", description = "Get Vehicle by id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully found vehicle"),
            @ApiResponse(responseCode = "400", description = "vehicle not found"),
    })
    @GetMapping("/{vehicleId}")
    public ResponseEntity<VehicleModel> getVehicleById(@PathVariable Long vehicleId) {
        Optional<VehicleModel> model = vehicleService.getVehicleById(vehicleId);
        if(model.isPresent()){
            return ResponseEntity.status(HttpStatus.OK).body(model.get());
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
