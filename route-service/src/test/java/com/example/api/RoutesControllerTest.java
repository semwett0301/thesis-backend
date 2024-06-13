package com.example.api;

import com.example.model.dto.request.RouteRequest;
import com.example.model.dto.response.RouteResponse;
import com.example.model.dto.response.SavedRoutesResponse;
import com.example.services.RouteService.RouteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class RoutesControllerTest {

    @Mock
    private RouteService routeService;

    @InjectMocks
    private RoutesController routesController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(routesController).build();
    }

    @Test
    void createRoute_WithUsername() throws Exception {
        RouteResponse routeResponse = new RouteResponse();
        when(routeService.createRoute(any(RouteRequest.class), anyString())).thenReturn(routeResponse);

        mockMvc.perform(post("/routes")
                        .contentType("application/json")
                        .content("{\"startLocation\":\"loc1\", \"endLocation\":\"loc2\"}")
                        .principal(() -> "user"))
                .andExpect(status().isCreated());
    }

    @Test
    void createRoute_WithoutUsername() throws Exception {
        RouteResponse routeResponse = new RouteResponse();
        when(routeService.createRoute(any(RouteRequest.class))).thenReturn(routeResponse);

        mockMvc.perform(post("/routes")
                        .contentType("application/json")
                        .content("{\"startLocation\":\"loc1\", \"endLocation\":\"loc2\"}"))
                .andExpect(status().isCreated());
    }

    @Test
    void getRoute_Success() throws Exception {
        RouteResponse routeResponse = new RouteResponse();
        when(routeService.getRoute(any(UUID.class))).thenReturn(routeResponse);

        mockMvc.perform(get("/routes/{id}", UUID.randomUUID()))
                .andExpect(status().isOk());
    }

    @Test
    void getSavedRoutes_Success() throws Exception {
        SavedRoutesResponse savedRoutesResponse = new SavedRoutesResponse(List.of(), List.of());
        when(routeService.getRoutes(anyString())).thenReturn(savedRoutesResponse);

        mockMvc.perform(post("/routes/saved").principal(() -> "user"))
                .andExpect(status().isOk());
    }

    @Test
    void getSavedRoutes_Forbidden() throws Exception {
        mockMvc.perform(post("/routes/saved"))
                .andExpect(status().isForbidden());
    }

    @Test
    void saveRoute_Success() throws Exception {
        RouteResponse routeResponse = new RouteResponse();
        when(routeService.saveRoute(any(UUID.class))).thenReturn(routeResponse);

        mockMvc.perform(post("/routes/{id}/save", UUID.randomUUID()))
                .andExpect(status().isOk());
    }
}
