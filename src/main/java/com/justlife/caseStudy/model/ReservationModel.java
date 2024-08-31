package com.justlife.caseStudy.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class ReservationModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "reservation id")
    private Long id;

    @Schema(description = "Start time of the reservation", example = "2024-08-31T11:00:00")
    private LocalDateTime startTime;

    @Schema(description = "End time of the reservation", example = "2024-08-31T11:30:00")
    private LocalDateTime endTime;

    @Schema(description = "Duration of service", example = "2,4")
    private int duration;

    @Schema(description = "Cleaning professional count", example = "1,2 or 3")
    private int cleaningProfessionalCount;

    @ManyToOne
    @JoinColumn(name = "cleaning_professional_id")
    @Schema(description = "Cleaning professional assigned for reservation")
    private CleaningProfessionalModel cleaningProfessionalModel;

    public ReservationModel() {
    }

    public ReservationModel(Long id, LocalDateTime startTime, LocalDateTime endTime, int duration, int cleaningProfessionalCount, CleaningProfessionalModel cleaningProfessionalModel) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.duration = duration;
        this.cleaningProfessionalCount = cleaningProfessionalCount;
        this.cleaningProfessionalModel = cleaningProfessionalModel;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getCleaningProfessionalCount() {
        return cleaningProfessionalCount;
    }

    public void setCleaningProfessionalCount(int cleaningProfessionalCount) {
        this.cleaningProfessionalCount = cleaningProfessionalCount;
    }

    public CleaningProfessionalModel getCleaningProfessionalModel() {
        return cleaningProfessionalModel;
    }

    public void setCleaningProfessionalModel(CleaningProfessionalModel cleaningProfessionalModel) {
        this.cleaningProfessionalModel = cleaningProfessionalModel;
    }
}
