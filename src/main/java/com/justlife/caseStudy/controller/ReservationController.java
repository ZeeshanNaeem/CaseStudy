package com.justlife.caseStudy.controller;

import com.justlife.caseStudy.model.CleaningProfessionalModel;
import com.justlife.caseStudy.model.ReservationModel;
import com.justlife.caseStudy.service.ReservationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/reservation")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @Operation(summary = "Get Cleaning professionals based on availability", description = "Get Cleaning professionals based on availability.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully found cleaning professional"),
            @ApiResponse(responseCode = "400", description = "cleaning professional not found"),
    })
    @GetMapping("/cleaning/professional/availability")
    public ResponseEntity<List<CleaningProfessionalModel>> getCleaningProfessionalsBasedOnAvailability(
            @RequestParam String date,
            @RequestParam(required = false) Integer professionalsRequired,
            @RequestParam(required = false) String startTime,
            @RequestParam(required = false) Integer duration
    ) {

        professionalsRequired = professionalsRequired != null ? professionalsRequired : 1;

        if (professionalsRequired < 1 || professionalsRequired > 3) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid number of professionals required");
        }

        if (duration != null && duration != 2 && duration != 4) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid booking duration. Must be 2 or 4 hours.");
        }
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate parsedDate = LocalDate.parse(date, dateFormatter);

        List<CleaningProfessionalModel> cleaningProfessionalModelList = new ArrayList<>();

        if (date!=null && (startTime == null || duration == null || professionalsRequired == null)) {
            cleaningProfessionalModelList = reservationService.findCleaningProfessionalsByDate(parsedDate, professionalsRequired);
        }else{
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
            LocalDateTime parsedStartTime = LocalDateTime.parse(startTime, dateTimeFormatter);

            cleaningProfessionalModelList = reservationService.findCleaningProfessionalsByDateAndTime(parsedDate,professionalsRequired,duration,parsedStartTime);
        }

        if(!cleaningProfessionalModelList.isEmpty()){
            return ResponseEntity.status(HttpStatus.OK).body(cleaningProfessionalModelList);
        }else{
            throw new IllegalStateException("Not enough professionals available for the requested time");

        }
    }



    @Operation(summary = "Create a new reservation", description = "Creates a new reservation.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully created booking"),
            @ApiResponse(responseCode = "400", description = "Invalid input parameters")
    })
    @PostMapping
    public List<ReservationModel> createReservation(@RequestBody ReservationModel reservationModel) {
        if (reservationModel.getCleaningProfessionalCount() < 1 || reservationModel.getCleaningProfessionalCount() > 3) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid number of professionals required");
        }
        if (reservationModel.getDuration() != 2 && reservationModel.getDuration() != 4) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid booking duration. Must be 2 or 4 hours.");
        }

        return reservationService.createReservation(reservationModel);
    }





    @Operation(summary = "Update existing reservation", description = "Update existing reservation.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated booking"),
            @ApiResponse(responseCode = "400", description = "Invalid input parameters")
    })
    @PutMapping("/{id}")
    public List<ReservationModel> updateReservation(@PathVariable Long id ,
                                                    @RequestBody ReservationModel reservationModel) {
        if (reservationModel.getCleaningProfessionalCount() < 1 || reservationModel.getCleaningProfessionalCount() > 3) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid number of professionals required");
        }
        if (reservationModel.getDuration() != 2 && reservationModel.getDuration() != 4) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid booking duration. Must be 2 or 4 hours.");
        }

        return reservationService.updateReservation(id,reservationModel);
    }



    @Operation(summary = "Get reservation", description = "Get reservation.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully returned reservation"),
            @ApiResponse(responseCode = "400", description = "Invalid input parameters")
    })
    @GetMapping("/{id}")
    public ReservationModel getReservation(@PathVariable Long id) {
        return reservationService.getReservation(id);
    }

}
