package com.metara.metara.models.dto;

import com.metara.metara.models.entity.Event;
import com.metara.metara.models.entity.Participation;
import com.metara.metara.models.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema
public class ParticipationDto {


    @Schema(description = "Event reference", requiredMode = Schema.RequiredMode.REQUIRED)
    private Event event;
    @Schema(description = "User participating in the event", requiredMode = Schema.RequiredMode.REQUIRED)
    private User user;

    @Schema(description = "Participation status", example = "CONFIRMED", allowableValues = {"PENDING", "CONFIRMED", "CANCELLED"})
    private Participation.Status status;

    @Schema(description = "Registration timestamp", example = "2024-12-11T10:30:00")
    private LocalDateTime registeredAt;

    public Event getEvent() {
        return event;
    }
    public void setEvent(Event event) {
        this.event = event;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Participation.Status getStatus() {
        return status;
    }

    public void setStatus(Participation.Status status) {
        this.status = status;
    }

    public LocalDateTime getRegisteredAt() {
        return registeredAt;
    }

    public void setRegisteredAt(LocalDateTime registeredAt) {
        this.registeredAt = registeredAt;
    }
}
