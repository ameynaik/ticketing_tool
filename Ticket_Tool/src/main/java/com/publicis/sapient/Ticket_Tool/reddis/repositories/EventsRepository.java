package com.publicis.sapient.Ticket_Tool.reddis.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.publicis.sapient.Ticket_Tool.EventType;
import com.publicis.sapient.Ticket_Tool.reddis.entities.Event;

@Repository
public interface EventsRepository extends CrudRepository<Event, Integer> {

    List<Event> findByTypeAndCityId(EventType type, Integer cityId);

    List<Event> findByCityId(Integer cityId);
}
