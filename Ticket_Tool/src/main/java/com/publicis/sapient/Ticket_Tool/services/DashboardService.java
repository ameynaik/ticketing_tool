package com.publicis.sapient.Ticket_Tool.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.publicis.sapient.Ticket_Tool.EventType;
import com.publicis.sapient.Ticket_Tool.pojo.TheatreDTO;
import com.publicis.sapient.Ticket_Tool.pojo.TheatreEventDTO;
import com.publicis.sapient.Ticket_Tool.reddis.entities.City;
import com.publicis.sapient.Ticket_Tool.reddis.entities.Event;
import com.publicis.sapient.Ticket_Tool.reddis.entities.Theatre;
import com.publicis.sapient.Ticket_Tool.reddis.repositories.CityRepository;
import com.publicis.sapient.Ticket_Tool.reddis.repositories.EventsRepository;
import com.publicis.sapient.Ticket_Tool.reddis.repositories.TheatreEventRepository;
import com.publicis.sapient.Ticket_Tool.reddis.repositories.TheatreRepository;

import reactor.util.function.Tuples;

@Service
public class DashboardService {

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private EventsRepository eventsRepository;

    @Autowired
    private TheatreRepository theatreRepository;

    @Autowired
    private TheatreEventRepository theatreEventRepository;

    public List<City> getAllCities() {
        List<City> cities = new ArrayList<>();
        cityRepository.findAll().forEach(cities::add);
        return cities;
    }

    public List<TheatreEventDTO> getEventsByCityId(Integer cityId) {
        return eventsRepository.findByCityId(cityId)
            .stream()
            .flatMap(event -> this.theatreEventRepository.findByEventId(event.id())
                .stream()
                .map(theatreEvent -> Tuples.of(event.name(), theatreEvent)))
            .map(te -> new TheatreEventDTO(te.getT2().id(), te.getT2().theatreId(), te.getT1(), te.getT2().eventDate().toString(), te.getT2().eventTime().toString()))
            .toList();
    }

    public List<TheatreEventDTO> getEventsByTypeAndCity(EventType type, Integer cityId) {
        return eventsRepository.findByTypeAndCityId(type, cityId)
            .stream()
            .flatMap(event -> this.theatreEventRepository.findByEventId(event.id())
                .stream()
                .map(theatreEvent -> Tuples.of(event.name(), theatreEvent)))
            .map(te -> new TheatreEventDTO(te.getT2().id(), te.getT2().theatreId(), te.getT1(), te.getT2().eventDate().toString(), te.getT2().eventTime().toString()))
            .toList();
    }

    public List<TheatreDTO> getTheatresByCityId(Integer cityId) {
        return theatreRepository.findByCityId(cityId)
        .stream()
        .map(theatre -> new TheatreDTO(theatre.id(), theatre.name(), theatre.cityId(), this.getTheatreEventsByCityIdAndTheatreId(cityId, theatre.id())))
        .toList();
    }

    public List<TheatreDTO> getTheatresByEventAndCity(String eventName, Integer cityId) {
       
        List<Theatre> theatres = theatreRepository.findByCityId(cityId);
        Map<Integer, List<TheatreEventDTO>> eventsByTheatre = theatres.stream()
                .flatMap(t -> theatreEventRepository.findByTheatreId(t.id()).stream())
                .filter(te -> {
                    Event event = eventsRepository.findById(te.eventId()).orElse(null);
                    return event != null && event.name() != null && event.name().contains(eventName);
                })
                .map(te -> {
                    Event event = eventsRepository.findById(te.eventId()).orElse(null);
                    return new TheatreEventDTO(te.id(), te.theatreId(), event != null ? event.name() : null, te.eventDate().toString(), te.eventTime().toString());
                })
                .collect(Collectors.groupingBy(TheatreEventDTO::theatreId));
        return theatres.stream()
                .filter(theatre -> eventsByTheatre.containsKey(theatre.id()))
                .map(theatre -> new TheatreDTO(
                        theatre.id(), theatre.name(), theatre.cityId(),
                        eventsByTheatre.get(theatre.id())))
                .toList();
    }

    public List<TheatreEventDTO> getTheatreEventsByDateAndId(LocalDate date, Integer theatreId) {
       return theatreEventRepository.findByEventDateAndTheatreId(date, theatreId)   
                .stream()
                .map(te -> {
                    Event event = eventsRepository.findById(te.eventId()).orElse(null);
                    return new TheatreEventDTO(
                        te.id(), theatreId, te.eventId() != null && event != null ? event.name() : null,
                        te.eventDate().toString(), te.eventTime().toString());
                })
                .toList();
    }

    private List<TheatreEventDTO> getTheatreEventsByCityIdAndTheatreId(Integer cityId, Integer theatreId) {
        return theatreRepository.findByCityId(cityId)
            .stream()
            .flatMap(t -> theatreEventRepository.findByTheatreId(t.id()).stream())
            .map(te -> {
                Event event = eventsRepository.findById(te.eventId()).orElse(null);
                return new TheatreEventDTO(te.id(), te.theatreId(), event != null ? event.name() : null, te.eventDate().toString(), te.eventTime().toString());
            })
            .toList();
    }
}
