package com.example.services;

import com.example.model.dto.internal.AccommodationRequest;
import com.example.model.dto.response.AccommodationResponse;

import java.util.List;

public interface AccommodationService {
    List<AccommodationResponse> getAccommodations(AccommodationRequest accommodationRequest);
}
