package com.example.api;

import com.example.model.dto.internal.AccommodationRequest;
import com.example.model.dto.response.AccommodationResponse;
import com.example.services.AccommodationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class AccommodationsControllerTest {

    @Mock
    private AccommodationService accommodationService;

    @InjectMocks
    private AccommodationsController accommodationsController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(accommodationsController).build();
    }

    @Test
    void getAccommodations_Success() throws Exception {
        Date startDate = new SimpleDateFormat("yyyy-MM-dd").parse("2024-06-01");
        Date endDate = new SimpleDateFormat("yyyy-MM-dd").parse("2024-06-10");
        AccommodationResponse response = new AccommodationResponse(100, 1.0, "Hotel Name", "source", 4.5, 90);
        List<AccommodationResponse> responseList = Collections.singletonList(response);

        when(accommodationService.getAccommodations(any(AccommodationRequest.class))).thenReturn(responseList);

        mockMvc.perform(get("/accommodations")
                        .param("start_date", "2024-06-01")
                        .param("end_date", "2024-06-10")
                        .param("max_price", "200")
                        .param("city_iata", "XYZ"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].price").value(100))
                .andExpect(jsonPath("$[0].distance").value(1.0))
                .andExpect(jsonPath("$[0].name").value("Hotel Name"))
                .andExpect(jsonPath("$[0].source").value("source"))
                .andExpect(jsonPath("$[0].rating").value(4.5))
                .andExpect(jsonPath("$[0].reviewCount").value(90));
    }

    @Test
    void getAccommodations_InvalidParameters() throws Exception {
        mockMvc.perform(get("/accommodations")
                        .param("start_date", "")
                        .param("end_date", "")
                        .param("max_price", "200")
                        .param("city_iata", "XYZ"))
                .andExpect(status().isBadRequest());
    }
}
