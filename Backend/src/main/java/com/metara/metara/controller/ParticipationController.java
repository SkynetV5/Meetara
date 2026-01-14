package com.metara.metara.controller;

import com.metara.metara.models.dto.ParticipationDto;
import com.metara.metara.models.entity.Participation;
import com.metara.metara.service.ParticipationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/participations")
@Tag(name="Participation Managment", description = "API Participation" )
public class ParticipationController {

    @Autowired
    private ParticipationService participationService;

    // CREATE
    @PostMapping
    public ResponseEntity<Participation> createParticipation(@RequestBody ParticipationDto participationDto) {
        try {
            Participation newParticipation = new Participation();
            newParticipation.setEvent(participationDto.getEvent());
            newParticipation.setUser(participationDto.getUser());
            newParticipation.setStatus(participationDto.getStatus());
            newParticipation.setRegisteredAt(participationDto.getRegisteredAt());
            Participation createdParticipation = participationService.createParticipation(newParticipation);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdParticipation);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // READ
    @GetMapping("/{id}")
    public ResponseEntity<Participation> getParticipationById(@PathVariable Long id) {
        try {
            Participation participation = participationService.getParticipationByIdOrThrow(id);
            return ResponseEntity.ok(participation);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<Participation>> getAllParticipations() {
        List<Participation> participations = participationService.getAllParticipations();
        return ResponseEntity.ok(participations);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Participation> getParticipationByUserId(@PathVariable Long userId) {
        Optional<Participation> participation = participationService.getParticipationByUserId(userId);
        return participation.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/event/{eventId}")
    public ResponseEntity<List<Participation>> getParticipationByEventId(@PathVariable Long eventId) {
        List<Participation> participations = participationService.getParticipationByEventId(eventId);
        return ResponseEntity.ok(participations);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<Participation> getParticipationByStatus(@PathVariable Participation.Status status) {
        Optional<Participation> participation = participationService.getParticipationByStatus(status);
        return participation.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/registered-at")
    public ResponseEntity<Participation> getParticipationByRegisteredAt(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date) {
        Optional<Participation> participation = participationService.getParticipationByRegisteredAt(date);
        return participation.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/registered-after")
    public ResponseEntity<List<Participation>> getParticipationsByRegisteredAtAfter(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date) {
        List<Participation> participations = participationService.getParticipationsByRegisteredAtAfter(date);
        return ResponseEntity.ok(participations);
    }

    @GetMapping("/registered-before")
    public ResponseEntity<List<Participation>> getParticipationsByRegisteredAtBefore(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date) {
        List<Participation> participations = participationService.getParticipationsByRegisteredAtBefore(date);
        return ResponseEntity.ok(participations);
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<Participation> updateParticipation(
            @PathVariable Long id,
            @RequestBody Participation participation) {
        try {
            Participation updatedParticipation = participationService.updateParticipation(id, participation);
            return ResponseEntity.ok(updatedParticipation);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Participation> updateParticipationStatus(
            @PathVariable Long id,
            @RequestParam Participation.Status status) {
        try {
            Participation updatedParticipation = participationService.updateParticipationStatus(id, status);
            return ResponseEntity.ok(updatedParticipation);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteParticipation(@PathVariable Long id) {
        try {
            participationService.deleteParticipation(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
