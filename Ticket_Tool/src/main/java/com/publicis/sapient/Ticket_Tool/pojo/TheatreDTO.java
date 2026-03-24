package com.publicis.sapient.Ticket_Tool.pojo;

import java.util.List;

public record TheatreDTO(Integer theatreId, String name, Integer cityId, List<TheatreEventDTO> events) {
    
}
