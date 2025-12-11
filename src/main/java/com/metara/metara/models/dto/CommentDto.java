package com.metara.metara.models.dto;

import com.metara.metara.models.entity.Event;
import com.metara.metara.models.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Data requires to create comment")
public class CommentDto {

    @Schema(description = "Text comment", example = "Super pomysł.")
    private String text;

    @Schema(description = "Event", example = "")
    private Event event;
    @Schema(description = "User", example = "")
    private User user;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

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
}
