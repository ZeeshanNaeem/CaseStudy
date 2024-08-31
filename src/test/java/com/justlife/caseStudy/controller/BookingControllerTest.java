package com.justlife.caseStudy.controller;

import com.justlife.caseStudy.model.CleaningProfessionalModel;
import com.justlife.caseStudy.model.ReservationModel;
import com.justlife.caseStudy.service.ReservationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PrimitiveIterator;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private CleaningProfessionalModel cleaningProfessionalModel;
    private List<CleaningProfessionalModel> cleaningProfessionalModelList;

    private ReservationModel reservationModel;

    @MockBean
    private ReservationService reservationService;

    @BeforeEach
    void setUp() {
        cleaningProfessionalModel = new CleaningProfessionalModel();
        cleaningProfessionalModel.setId(1L);
        cleaningProfessionalModel.setName("Zeeshan Naeem");

        cleaningProfessionalModelList = new ArrayList<>();
        cleaningProfessionalModelList.add(cleaningProfessionalModel);

        reservationModel = new ReservationModel();
        reservationModel.setId(1L);
        reservationModel.setStartTime(LocalDateTime.of(2024, 8, 31, 10, 0));
        reservationModel.setDuration(2);
        reservationModel.setCleaningProfessionalCount(1);
    }


    @Test
    void testReservation_ValidRequest() throws Exception {
        when(reservationService.findCleaningProfessionalsByDateAndTime(
                any(LocalDate.class), any(Integer.class), any(Integer.class),any(LocalDateTime.class)))
                .thenReturn(cleaningProfessionalModelList);

        mockMvc.perform(get("/reservation/cleaning/professional/availability")
                        .param("date", "2024-08-31")
                        .param("startTime", "2024-08-31T10:00:00")
                        .param("duration", "2")
                        .param("professionalsRequired", "1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("[{'id':1,'name':'Zeeshan Naeem'}]"));
    }

    @Test
    void testReservation_InvalidDuration() throws Exception {
        mockMvc.perform(get("/reservation/cleaning/professional/availability")
                        .param("date", "2024-08-31")
                        .param("startTime", "2024-08-31T10:00:00")
                        .param("duration", "8")
                        .param("professionalsRequired", "1"))
                .andExpect(status().isBadRequest());
    }


    @Test
    void testReservation_InvalidProfessionals() throws Exception {
        mockMvc.perform(get("/reservation/cleaning/professional/availability")
                        .param("date", "2024-08-31")
                        .param("startTime", "2024-08-31T10:00:00")
                        .param("duration", "2")
                        .param("professionalsRequired", "15"))
                .andExpect(status().isBadRequest());
    }


    @Test
    void testCreateBooking_ValidRequest() throws Exception {
        ReservationModel reservationModel = new ReservationModel();
        reservationModel.setStartTime(LocalDateTime.parse("2024-09-30T10:00:00"));
        reservationModel.setEndTime(LocalDateTime.parse("2024-09-30T12:00:00"));
        reservationModel.setDuration(2);
        reservationModel.setCleaningProfessionalCount(1);

        // Create a list containing the single reservation model
        List<ReservationModel> reservationModelList = Collections.singletonList(reservationModel);

        // Mock the service to return this list when createReservation is called
        when(reservationService.createReservation(any(ReservationModel.class))).thenReturn(reservationModelList);

        String reservationJson = "{\"startTime\":\"2024-09-30T10:00:00\",\"duration\":2,\"cleaningProfessionalCount\":1}";

        mockMvc.perform(post("/reservation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(reservationJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }


    @Test
    void testCreateBooking_InValidRequest() throws Exception {
        ReservationModel reservationModel = new ReservationModel();
        reservationModel.setStartTime(LocalDateTime.parse("2024-09-30T10:00:00"));
        reservationModel.setEndTime(LocalDateTime.parse("2024-09-30T12:00:00"));
        reservationModel.setDuration(12);
        reservationModel.setCleaningProfessionalCount(1);

        // Create a list containing the single reservation model
        List<ReservationModel> reservationModelList = Collections.singletonList(reservationModel);

        // Mock the service to return this list when createReservation is called
        when(reservationService.createReservation(any(ReservationModel.class))).thenReturn(reservationModelList);

        String reservationJson = "{\"startTime\":\"2024-09-30T10:00:00\",\"duration\":2,\"cleaningProfessionalCount\":12}";

        mockMvc.perform(post("/reservation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(reservationJson))
                .andExpect(status().isBadRequest());

    }
}
