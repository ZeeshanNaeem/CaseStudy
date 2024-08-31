package com.justlife.caseStudy.repository;

import com.justlife.caseStudy.model.CleaningProfessionalModel;
import com.justlife.caseStudy.model.ReservationModel;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<ReservationModel,Long> {

    @Query(value = "SELECT * FROM Reservation_Model a " +
            "WHERE a.cleaning_professional_id = :cleaningProfessionalId " +
            "AND DATE(a.start_time) = :date", nativeQuery = true)
    List<ReservationModel> findReservationByProfessionalIDAndDate(
            @Param("cleaningProfessionalId") Long cleaningProfessionalId,
            @Param("date") LocalDate date);


    @Query(value = "SELECT * FROM Reservation_Model a " +
            "WHERE a.cleaning_professional_id = :cleaningProfessionalId " +
            "AND DATE(a.start_time) = :date " +
            "AND TIME(a.start_time) BETWEEN :startTime AND :endTime", nativeQuery = true)
    List<ReservationModel> findReservationsByProfessionalIdAndDateAndTime(
            @Param("cleaningProfessionalId") Long cleaningProfessionalId,
            @Param("date") LocalDate date,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime);


}
