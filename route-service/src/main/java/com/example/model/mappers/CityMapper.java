package com.example.model.mappers;

import com.example.CityDto;
import com.example.model.entities.db.City;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CityMapper {
    CityMapper INSTANCE = Mappers.getMapper(CityMapper.class);

    CityDto toCityDto(City book);

    City toCity(CityDto book);
}
