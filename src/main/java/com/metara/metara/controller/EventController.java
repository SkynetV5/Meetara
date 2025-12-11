package com.metara.metara.controller;

import com.metara.metara.models.dto.EventDto;
import com.metara.metara.models.entity.Event;
import com.metara.metara.service.EventService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/events")
@Tag(name="Event Managment", description = "API Events" )
public class EventController {

    @Autowired
    private EventService eventService;

    // CREATE
    @PostMapping
    public ResponseEntity<Event> createEvent(@Valid  @RequestBody EventDto eventDto) {
        Event newEvent = new Event();
        newEvent.setTitle(eventDto.getTitle());
        newEvent.setDescription(eventDto.getDescription());
        newEvent.setLocation(eventDto.getLocation());
        newEvent.setEventDate(eventDto.getEventDate());
        newEvent.setUser(eventDto.getUser());
        newEvent.setComments(eventDto.getComments());

        Event createdEvent = eventService.createEvent(newEvent);
        if (createdEvent == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(createdEvent);
    }

    // READ
    @GetMapping("/{id}")
    public ResponseEntity<Event> getEventById(@PathVariable Long id) {
        try {
            Event event = eventService.getEventByIdOrThrow(id);
            return ResponseEntity.ok(event);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Event> getEventByUserId(@PathVariable Long userId) {
        Optional<Event> event = eventService.getEventByUserId(userId);
        return event.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Event>> getAllEvents() {
        List<Event> events = eventService.getAllEvents();
        return ResponseEntity.ok(events);
    }

    @GetMapping("/title/{title}")
    public ResponseEntity<List<Event>> getEventsByTitle(@PathVariable String title) {
        List<Event> events = eventService.getEventsByTitle(title);
        return ResponseEntity.ok(events);
    }

    @GetMapping("/location/{location}")
    public ResponseEntity<List<Event>> getEventsByLocation(@PathVariable String location) {
        List<Event> events = eventService.getEventsByLocation(location);
        return ResponseEntity.ok(events);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Event>> searchEventsByKeyword(@RequestParam String keyword) {
        List<Event> events = eventService.getEventsByTitleContainingIgnoreCase(keyword);
        return ResponseEntity.ok(events);
    }

    @GetMapping("/after")
    public ResponseEntity<List<Event>> getEventsAfterDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date) {
        List<Event> events = eventService.getEventsByEventDateAfter(date);
        return ResponseEntity.ok(events);
    }

    @GetMapping("/before")
    public ResponseEntity<List<Event>> getEventsBeforeDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date) {
        List<Event> events = eventService.getEventsByEventDateBefore(date);
        return ResponseEntity.ok(events);
    }

    @GetMapping("/after/search")
    public ResponseEntity<List<Event>> getEventsAfterDateWithKeyword(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date,
            @RequestParam String keyword) {
        List<Event> events = eventService.getEventsByEventDateAfterAndTitleContainingIgnoreCase(date, keyword);
        return ResponseEntity.ok(events);
    }

    @GetMapping("/before/search")
    public ResponseEntity<List<Event>> getEventsBeforeDateWithKeyword(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date,
            @RequestParam String keyword) {
        List<Event> events = eventService.getEventsByEventDateBeforeAndTitleContainingIgnoreCase(date, keyword);
        return ResponseEntity.ok(events);
    }

    @GetMapping("/after/sorted")
    public ResponseEntity<List<Event>> getEventsAfterDateSorted(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date,
            @RequestParam(defaultValue = "asc") String order) {
        List<Event> events;
        if ("desc".equalsIgnoreCase(order)) {
            events = eventService.getEventsByEventDateAfterOrderByEventDateDesc(date);
        } else {
            events = eventService.getEventsByEventDateAfterOrderByEventDateAsc(date);
        }
        return ResponseEntity.ok(events);
    }

    @GetMapping("/before/sorted")
    public ResponseEntity<List<Event>> getEventsBeforeDateSorted(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date,
            @RequestParam(defaultValue = "asc") String order) {
        List<Event> events;
        if ("desc".equalsIgnoreCase(order)) {
            events = eventService.getEventsByEventDateBeforeOrderByEventDateDesc(date);
        } else {
            events = eventService.getEventsByEventDateBeforeOrderByEventDateAsc(date);
        }
        return ResponseEntity.ok(events);
    }

    @GetMapping("/after/page")
    public ResponseEntity<Page<Event>> getPagedEventsAfterDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date,
            Pageable pageable) {
        Page<Event> events = eventService.getPageEventsByEventDateAfter(date, pageable);
        return ResponseEntity.ok(events);
    }

    @GetMapping("/before/page")
    public ResponseEntity<Page<Event>> getPagedEventsBeforeDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date,
            Pageable pageable) {
        Page<Event> events = eventService.getPageEventsByEventDateBefore(date, pageable);
        return ResponseEntity.ok(events);
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<Event> updateEvent(@PathVariable Long id, @RequestBody Event event) {
        try {
            Event updatedEvent = eventService.updateEvent(id, event);
            return ResponseEntity.ok(updatedEvent);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        try {
            eventService.deleteEvent(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
