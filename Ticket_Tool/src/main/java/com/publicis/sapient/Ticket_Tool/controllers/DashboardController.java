package com.publicis.sapient.Ticket_Tool.controllers;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.publicis.sapient.Ticket_Tool.EventType;
import com.publicis.sapient.Ticket_Tool.pojo.TheatreDTO;
import com.publicis.sapient.Ticket_Tool.pojo.TheatreEventDTO;
import com.publicis.sapient.Ticket_Tool.reddis.entities.City;
import com.publicis.sapient.Ticket_Tool.services.DashboardService;

@RestController
@RequestMapping("/api/v1")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/cities")
    public List<City> getAllCities() {
        return dashboardService.getAllCities();
    }

    @GetMapping("/cities/{cityId}/events")
    public List<TheatreEventDTO> getEventsByCity(@PathVariable Integer cityId) {
        return dashboardService.getEventsByCityId(cityId);
    }

    @GetMapping("/events/type/{type}/cities/{cityId}")
    public List<TheatreEventDTO> getEventsByTypeAndCity(@PathVariable EventType type, @PathVariable Integer cityId) {
        return dashboardService.getEventsByTypeAndCity(type, cityId);
    }

    @GetMapping("/cities/{cityId}/theatres")
    public List<TheatreDTO> getTheatresByCity(@PathVariable Integer cityId) {
        return dashboardService.getTheatresByCityId(cityId);
    }

    @GetMapping("/cities/{cityId}/events/{eventName}/theatres")
    public List<TheatreDTO> getTheatresByEventAndCity(
            @PathVariable Integer cityId,
            @PathVariable String eventName) {
        return dashboardService.getTheatresByEventAndCity(eventName, cityId);
    }

    @GetMapping("/theatres/{theatreId}/events")
    public List<TheatreEventDTO> getTheatreEventsByDateAndId(
            @PathVariable Integer theatreId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return dashboardService.getTheatreEventsByDateAndId(date, theatreId);
    }
}
    
