package com.example.api;

import com.example.model.entities.db.City;
import com.example.services.CityService.CityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CitiesControllerTest {

    @Mock
    private CityService cityService;

    @InjectMocks
    private CitiesController citiesController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(citiesController).build();
    }

    @Test
    void getCities_Success() throws Exception {
        Page<City> cityPage = new PageImpl<>(Collections.singletonList(new City()));
        when(cityService.searchCity(anyString(), anyInt(), anyInt())).thenReturn(cityPage);

        mockMvc.perform(get("/cities")
                        .param("search", "searchQuery")
                        .param("page", "1")
                        .param("page_size", "10"))
                .andExpect(status().isOk());
    }

    @Test
    void getCities_InvalidParameters() throws Exception {
        mockMvc.perform(get("/cities")
                        .param("page", "-1")
                        .param("page_size", "0"))
                .andExpect(status().isBadRequest());
    }
}
