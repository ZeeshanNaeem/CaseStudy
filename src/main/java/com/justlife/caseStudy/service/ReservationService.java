package com.justlife.caseStudy.service;

import com.justlife.caseStudy.model.CleaningProfessionalModel;
import com.justlife.caseStudy.model.ReservationModel;
import com.justlife.caseStudy.model.VehicleModel;
import com.justlife.caseStudy.repository.CleaningProfessionalRepository;
import com.justlife.caseStudy.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReservationService {

    @Autowired
    private CleaningProfessionalRepository cleaningProfessionalRepository;

    @Autowired
    private ReservationRepository reservationRepository;


    // Method to check if the provided date is a Friday
    public static boolean isFriday(LocalDate date) {
        return date.getDayOfWeek() == DayOfWeek.FRIDAY;
    }

    // Method to check if the time is between 8 AM and 10 PM
    public static boolean isTimeBetween(LocalTime time) {
        LocalTime startTime = LocalTime.of(8, 0);  // 8:00 AM
        LocalTime endTime = LocalTime.of(22, 0);   // 10:00 PM

        // Check if the time is between or equal to the start and end times
        return !time.isBefore(startTime) && !time.isAfter(endTime);
    }



    public boolean isProfessionalAvailableOnGivenDateAndTime(CleaningProfessionalModel cleaningProfessionalModel, LocalDate parsedDate,
                                                             LocalDateTime requestedStartTime, LocalDateTime requestedEndTime){

        if(isFriday(parsedDate)){
            return false;
        }

        LocalTime startTime = LocalTime.parse(cleaningProfessionalModel.getStartingWorkingHours());
        LocalTime endTime = LocalTime.parse(cleaningProfessionalModel.getEndingWorkingHours());

        boolean isWithinWorkingHours = !requestedStartTime.toLocalTime().isBefore(startTime) && !requestedEndTime.toLocalTime().isAfter(endTime);

        if (!isWithinWorkingHours) {
            return false;
        }

        Duration minimumBreakDuration = Duration.ofMinutes(30);

        // Check for existing bookings based on Date and time
        List<ReservationModel> reservationModelList = reservationRepository.
                findReservationsByProfessionalIdAndDateAndTime(cleaningProfessionalModel.getId(),parsedDate,requestedStartTime.toLocalTime(),requestedEndTime.toLocalTime());

        for (ReservationModel reservationModel : reservationModelList) {
            LocalDateTime reservationModelStartTime = reservationModel.getStartTime();
            LocalDateTime reservationModelEndTime = reservationModel.getEndTime();

            // Check if the requested appointment overlaps with any existing reservation
            if (requestedStartTime.isBefore(reservationModelEndTime.plus(minimumBreakDuration)) &&
                    requestedEndTime.isAfter(reservationModelStartTime.minus(minimumBreakDuration))) {
                return false;
            }
        }

        return true;
    }


    public boolean isProfessionalAvailableOnGivenDate(CleaningProfessionalModel cleaningProfessionalModel, LocalDate parsedDate){

        if(isFriday(parsedDate)){
            return false;
        }
        // Check for existing bookings based on Date
        List<ReservationModel> reservationModelList = reservationRepository.findReservationByProfessionalIDAndDate(cleaningProfessionalModel.getId(),parsedDate);
        if(reservationModelList.isEmpty()){
            return true;
        }
        return false;
    }

    public List<CleaningProfessionalModel> findCleaningProfessionalsByDate(LocalDate parsedDate, int professionalsRequired) {

        List<CleaningProfessionalModel> cleaningProfessionalModelList = cleaningProfessionalRepository.findAll();
        // Filter professionals based on availability
        return cleaningProfessionalModelList.stream()
                .filter(professional -> isProfessionalAvailableOnGivenDate(professional, parsedDate))
                .limit(professionalsRequired)
                .collect(Collectors.toList());
    }


    public List<CleaningProfessionalModel> findCleaningProfessionalsByDateAndTime(LocalDate parsedDate, int professionalsRequired, Integer duration, LocalDateTime startTime) {

        LocalDateTime endTime = startTime.plusHours(duration);

        List<CleaningProfessionalModel> cleaningProfessionalModelList = cleaningProfessionalRepository.findAll();
        // Filter professionals based on availability
        return cleaningProfessionalModelList.stream()
                .filter(professional -> isProfessionalAvailableOnGivenDateAndTime(professional, parsedDate,startTime,endTime))
                .limit(professionalsRequired)
                .collect(Collectors.toList());
    }


    @Transactional
    public List<ReservationModel> createReservation(ReservationModel reservationModel) {

        LocalDateTime startTime = reservationModel.getStartTime();
        LocalDateTime endTime = startTime.plusHours(reservationModel.getDuration());
        reservationModel.setEndTime(endTime);

        List<CleaningProfessionalModel> availableProfessionals = findCleaningProfessionalsByDateAndTime(reservationModel.getStartTime().toLocalDate(),reservationModel.getCleaningProfessionalCount(), reservationModel.getDuration(), reservationModel.getStartTime());

        if (availableProfessionals.size() < reservationModel.getCleaningProfessionalCount()) {
            throw new IllegalStateException("Not enough professionals available for the requested time");
        }

        VehicleModel vehicle = availableProfessionals.get(0).getVehicle();
        List<CleaningProfessionalModel> assignedProfessionals = filterProfessionalsByVehicle(vehicle.getId(), availableProfessionals, reservationModel.getCleaningProfessionalCount());

        List<ReservationModel> reservationModelList = new ArrayList<>();
        for (CleaningProfessionalModel professional : assignedProfessionals) {
            reservationModel.setCleaningProfessionalModel(professional);
            reservationModelList.add(reservationRepository.save(reservationModel));
        }

        return reservationModelList;
    }

    private List<CleaningProfessionalModel> filterProfessionalsByVehicle(Long vehicleId, List<CleaningProfessionalModel> availableProfessionals, int professionalsRequired) {
        List<CleaningProfessionalModel> assignedProfessionals = availableProfessionals.stream()
                .filter(pro -> pro.getVehicle().getId().equals(vehicleId))
                .limit(professionalsRequired)
                .collect(Collectors.toList());

        if (assignedProfessionals.size() < professionalsRequired) {
            throw new IllegalStateException("Not enough professionals from the same vehicle available for the requested time");
        }

        return assignedProfessionals;
    }

    public List<ReservationModel> updateReservation(Long id, ReservationModel updatedReservationModel) {

        ReservationModel existingReservation = reservationRepository.findById(id).
                orElseThrow(() -> new IllegalStateException("Reservation not found"));

        LocalDateTime startTime = updatedReservationModel.getStartTime();
        LocalDateTime endTime = startTime.plusHours(updatedReservationModel.getDuration());
        updatedReservationModel.setEndTime(endTime);

        List<CleaningProfessionalModel> availableProfessionals = findCleaningProfessionalsByDateAndTime(updatedReservationModel.getStartTime().toLocalDate(),updatedReservationModel.getCleaningProfessionalCount(), updatedReservationModel.getDuration(), updatedReservationModel.getStartTime());

        if (availableProfessionals.size() < updatedReservationModel.getCleaningProfessionalCount()) {
            throw new IllegalStateException("Not enough professionals available for the requested time");
        }

        VehicleModel vehicle = availableProfessionals.get(0).getVehicle();
        List<CleaningProfessionalModel> assignedProfessionals = filterProfessionalsByVehicle(vehicle.getId(), availableProfessionals, updatedReservationModel.getCleaningProfessionalCount());

        existingReservation.setDuration(updatedReservationModel.getDuration());
        existingReservation.setCleaningProfessionalCount(updatedReservationModel.getCleaningProfessionalCount());
        existingReservation.setStartTime(updatedReservationModel.getStartTime());
        existingReservation.setEndTime(updatedReservationModel.getEndTime());
        List<ReservationModel> reservationModelList = new ArrayList<>();
        for (CleaningProfessionalModel professional : assignedProfessionals) {
            existingReservation.setCleaningProfessionalModel(professional);
            reservationModelList.add(reservationRepository.save(existingReservation));
        }

        return reservationModelList;

    }

    public ReservationModel getReservation(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(()-> new IllegalStateException("Reservation does not exist"));
    }
}
