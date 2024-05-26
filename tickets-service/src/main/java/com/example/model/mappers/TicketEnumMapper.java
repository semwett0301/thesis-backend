package com.example.model.mappers;

import com.example.model.dto.request.TicketEnum;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class TicketEnumMapper implements Converter<String, TicketEnum> {

    @Override
    public TicketEnum convert(String source) {
        return TicketEnum.getRoleFromString(source);
    }
}
