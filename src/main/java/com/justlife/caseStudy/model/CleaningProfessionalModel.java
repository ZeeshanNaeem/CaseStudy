package com.justlife.caseStudy.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

@Entity
public class CleaningProfessionalModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "cleaning professional id")
    private Long id;

    @Schema(description = "Name of the cleaning professional")
    private String name;

    @Schema(description = "Working hour starts at 08:00", example = "08:00")
    private String startingWorkingHours = "08:00";

    @Schema(description = "Working hour ends at 22:00", example = "22:00")
    private String endingWorkingHours = "22:00";

    @ManyToOne
    @JoinColumn(name = "vehicle_id")
    @Schema(description = "Vehicle assigned to the cleaning professional")
    private VehicleModel vehicle;

    public CleaningProfessionalModel(){}

    public CleaningProfessionalModel(Long id, String name, String startingWorkingHours, String endingWorkingHours, VehicleModel vehicle) {
        this.id = id;
        this.name = name;
        this.startingWorkingHours = startingWorkingHours;
        this.endingWorkingHours = endingWorkingHours;
        this.vehicle = vehicle;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStartingWorkingHours() {
        return startingWorkingHours;
    }

    public void setStartingWorkingHours(String startingWorkingHours) {
        this.startingWorkingHours = startingWorkingHours;
    }

    public String getEndingWorkingHours() {
        return endingWorkingHours;
    }

    public void setEndingWorkingHours(String endingWorkingHours) {
        this.endingWorkingHours = endingWorkingHours;
    }

    public VehicleModel getVehicle() {
        return vehicle;
    }

    public void setVehicle(VehicleModel vehicle) {
        this.vehicle = vehicle;
    }
}
