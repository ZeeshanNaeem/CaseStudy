package com.justlife.caseStudy.controller;

import com.justlife.caseStudy.model.CleaningProfessionalModel;
import com.justlife.caseStudy.model.ReservationModel;
import com.justlife.caseStudy.service.CleaningProfessionalService;
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
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CleaningProfessionalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private CleaningProfessionalModel cleaningProfessionalModel;
    private List<CleaningProfessionalModel> cleaningProfessionalModelList;

    @MockBean
    private CleaningProfessionalService cleaningProfessionalService;

    @BeforeEach
    void setUp() {
        cleaningProfessionalModel = new CleaningProfessionalModel();
        cleaningProfessionalModel.setId(1L);
        cleaningProfessionalModel.setName("Zeeshan Naeem");

        cleaningProfessionalModelList = new ArrayList<>();
        cleaningProfessionalModelList.add(cleaningProfessionalModel);
    }


    @Test
    void testGetCleaningProfessional_ValidRequest() throws Exception {
        when(cleaningProfessionalService.getCleaningProfessionalById(any()))
                .thenReturn(Optional.ofNullable(cleaningProfessionalModel));


        mockMvc.perform(get("/cleaningprofessional/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"id\":1,\"name\":\"Zeeshan Naeem\",\"startingWorkingHours\":\"08:00\",\"endingWorkingHours\":\"22:00\",\"vehicle\":null}"));
    }


    @Test
    void testCreateCleaningProfessional_ValidRequest() throws Exception {
        when(cleaningProfessionalService.createCleaningProfessional(any()))
                .thenReturn(cleaningProfessionalModel);


        String json = "{\n" +
                "    \"name\": \"Arslan\",\n" +
                "  \"vehicle\": {\n" +
                "    \"id\": 1\n" +
                "  }\n" +
                "}";
        mockMvc.perform(post("/cleaningprofessional")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

}
