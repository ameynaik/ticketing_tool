package com.publicis.sapient.Ticket_Tool.reddis.entities;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;
@RedisHash("TheatreEvent")
public record TheatreEvent(@Id Integer id, @Indexed Integer eventId, LocalTime eventTime, @Indexed LocalDate eventDate, @Indexed Integer theatreId) {
    
}
