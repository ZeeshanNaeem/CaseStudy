package com.justlife.caseStudy.controller;

import com.justlife.caseStudy.model.CleaningProfessionalModel;
import com.justlife.caseStudy.model.VehicleModel;
import com.justlife.caseStudy.service.CleaningProfessionalService;
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
@RequestMapping("/cleaningprofessional")
public class CleaningProfessionalController {

    @Autowired
    private CleaningProfessionalService service;

    @Operation(summary = "Create new CleaningProfessional", description = "Create new CleaningProfessional.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully created CleaningProfessional")
    })
    @PostMapping
    public ResponseEntity<CleaningProfessionalModel> createCleaningProfessional(@RequestBody CleaningProfessionalModel cleaningProfessionalModel) {
        CleaningProfessionalModel model = service.createCleaningProfessional(cleaningProfessionalModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(model);
    }


    @Operation(summary = "Get cleaning professional by id", description = "Get cleaning professional by id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully found cleaning professional"),
            @ApiResponse(responseCode = "400", description = "Cleaning professional not found"),
    })
    @GetMapping("/{id}")
    public ResponseEntity<CleaningProfessionalModel> getCleaningProfessionalById(@PathVariable Long id) {
        Optional<CleaningProfessionalModel> model = service.getCleaningProfessionalById(id);
        if(model.isPresent()){
            return ResponseEntity.status(HttpStatus.OK).body(model.get());
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
