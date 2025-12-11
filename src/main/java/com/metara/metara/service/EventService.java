package com.metara.metara.service;

import com.metara.metara.models.entity.Event;
import com.metara.metara.repository.EventRepository;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    //CREATE
    public Event createEvent(Event event){
        if(event.getUser() == null){
            return null;
        }
        
        return eventRepository.save(event);
    }

    //READ
    public Event getEventByIdOrThrow(Long id){
        return eventRepository.findById(id).orElseThrow(() -> new RuntimeException("Event not found with id" + id));
    }

    public Optional<Event> getEventByUserId(Long userId){
        return eventRepository.findByUserId(userId);
    }

    public List<Event> getAllEvents() { return eventRepository.findAll();}

    public List<Event> getEventsByTitle(String title) { return eventRepository.findByTitle(title);}

    public List<Event> getEventsByLocation(String location) { return eventRepository.findByLocation(location);}

    public List<Event> getEventsByEventDateAfterAndTitleContainingIgnoreCase(LocalDateTime date, String keyword){
        return eventRepository.findByEventDateAfterAndTitleContainingIgnoreCase(date,keyword);
    }

    public List<Event> getEventsByEventDateBeforeAndTitleContainingIgnoreCase(LocalDateTime date, String keyword){
        return eventRepository.findByEventDateBeforeAndTitleContainingIgnoreCase(date,keyword);
    }

    public List<Event> getEventsByEventDateAfter(LocalDateTime date){
        return  eventRepository.findByEventDateAfter(date);
    }
    public List<Event> getEventsByEventDateBefore(LocalDateTime date){
        return  eventRepository.findByEventDateBefore(date);
    }

    public List<Event> getEventsByTitleContainingIgnoreCase(String keyword){
        return eventRepository.findByTitleContainingIgnoreCase(keyword);
    }

    public List<Event> getEventsByEventDateAfterOrderByEventDateAsc(LocalDateTime date){
        return eventRepository.findByEventDateAfterOrderByEventDateAsc(date);
    }

    public List<Event> getEventsByEventDateAfterOrderByEventDateDesc(LocalDateTime date){
        return eventRepository.findByEventDateAfterOrderByEventDateDesc(date);
    }

    public List<Event> getEventsByEventDateBeforeOrderByEventDateAsc(LocalDateTime date){
        return eventRepository.findByEventDateBeforeOrderByEventDateAsc(date);
    }

    public List<Event> getEventsByEventDateBeforeOrderByEventDateDesc(LocalDateTime date){
        return eventRepository.findByEventDateBeforeOrderByEventDateDesc(date);
    }



    public Page<Event> getPageEventsByEventDateAfter(LocalDateTime date, Pageable pageable){
        return  eventRepository.findByEventDateAfter(date, pageable);
    }
    public Page<Event> getPageEventsByEventDateBefore(LocalDateTime date, Pageable pageable){
        return  eventRepository.findByEventDateBefore(date, pageable);
    }

    // UPDATE

    public Event updateEvent(Long id, Event updatedEvent){
        Event existingEvent = getEventByIdOrThrow(id);

        if(updatedEvent.getUser() == null){
            throw new RuntimeException("Event have to have a user");
        }

        existingEvent.setUser(updatedEvent.getUser());
        existingEvent.setComments(updatedEvent.getComments());
        existingEvent.setTitle(updatedEvent.getTitle());
        existingEvent.setDescription(updatedEvent.getDescription());
        existingEvent.setLocation(updatedEvent.getLocation());

        return eventRepository.save(existingEvent);
    }

    // DELETE
    public void deleteEvent(Long id){
        if(!eventRepository.existsById(id)){
            throw new RuntimeException("User not found with id" + id);
        }
        eventRepository.deleteById(id);
    }
}
