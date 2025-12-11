package com.metara.metara.service;

import com.metara.metara.models.entity.Participation;
import com.metara.metara.repository.ParticipationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ParticipationService {

    @Autowired
    private ParticipationRepository participationRepository;

    // CREATE
    public Participation createParticipation(Participation participation) {
        if (participation.getUser() == null || participation.getEvent() == null) {
            throw new RuntimeException("Participation must have both user and event");
        }
        return participationRepository.save(participation);
    }

    // READ
    public Participation getParticipationByIdOrThrow(Long id) {
        return participationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Participation not found with id: " + id));
    }

    public Optional<Participation> getParticipationById(Long id) {
        return participationRepository.findById(id);
    }

    public Optional<Participation> getParticipationByUserId(Long userId) {
        return participationRepository.findByUserId(userId);
    }

    public Optional<Participation> getParticipationByEventId(Long eventId) {
        return participationRepository.findByEventId(eventId);
    }

    public Optional<Participation> getParticipationByStatus(Participation.Status status) {
        return participationRepository.findByStatus(status);
    }

    public Optional<Participation> getParticipationByRegisteredAt(LocalDateTime date) {
        return participationRepository.findByRegisteredAt(date);
    }

    public List<Participation> getParticipationsByRegisteredAtAfter(LocalDateTime date) {
        return participationRepository.findByRegisteredAtAfter(date);
    }

    public List<Participation> getParticipationsByRegisteredAtBefore(LocalDateTime date) {
        return participationRepository.findByRegisteredAtBefore(date);
    }

    public List<Participation> getAllParticipations() {
        return participationRepository.findAll();
    }

    // UPDATE
    public Participation updateParticipation(Long id, Participation updatedParticipation) {
        Participation existingParticipation = getParticipationByIdOrThrow(id);

        if (updatedParticipation.getUser() == null || updatedParticipation.getEvent() == null) {
            throw new RuntimeException("Participation must have both user and event");
        }

        existingParticipation.setUser(updatedParticipation.getUser());
        existingParticipation.setEvent(updatedParticipation.getEvent());
        existingParticipation.setStatus(updatedParticipation.getStatus());
        existingParticipation.setRegisteredAt(updatedParticipation.getRegisteredAt());

        return participationRepository.save(existingParticipation);
    }

    public Participation updateParticipationStatus(Long id, Participation.Status status) {
        Participation existingParticipation = getParticipationByIdOrThrow(id);
        existingParticipation.setStatus(status);
        return participationRepository.save(existingParticipation);
    }

    // DELETE
    public void deleteParticipation(Long id) {
        if (!participationRepository.existsById(id)) {
            throw new RuntimeException("Participation not found with id: " + id);
        }
        participationRepository.deleteById(id);
    }
}
