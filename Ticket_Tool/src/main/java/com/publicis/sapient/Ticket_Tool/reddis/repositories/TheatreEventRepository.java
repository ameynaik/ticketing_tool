package com.publicis.sapient.Ticket_Tool.reddis.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.publicis.sapient.Ticket_Tool.reddis.entities.TheatreEvent;

@Repository
public interface TheatreEventRepository extends CrudRepository<TheatreEvent, Integer> {

    List<TheatreEvent> findByEventDateAndTheatreId(LocalDate eventDate, Integer theatreId);
    List<TheatreEvent> findByTheatreId(Integer theatreId);
    List<TheatreEvent> findByEventId(Integer eventId);
}
