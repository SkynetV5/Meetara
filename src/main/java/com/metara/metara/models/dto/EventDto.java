package com.metara.metara.models.dto;


import com.metara.metara.models.entity.Comment;
import com.metara.metara.models.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.Set;

@Schema
public class EventDto {
    @Schema(description = "Title", example = "Lan Party")
    private String title;
    @Schema(description = "Description", example = "Impreza Lan Party")
    private String description;

    @Schema(description = "Event date and time", example = "2024-12-25T18:00:00", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime eventDate;

    @Schema(description = "Event location", example = "Centrum Konferencyjne, ul. Główna 15, Warszawa")
    private String location;

    @Schema(description = "Event organizer")
    private User user;

    @Schema(description = "Event comments")
    private Set<Comment> comments;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getEventDate() {
        return eventDate;
    }

    public void setEventDate(LocalDateTime eventDate) {
        this.eventDate = eventDate;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Comment> getComments() {
        return comments;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }
}
