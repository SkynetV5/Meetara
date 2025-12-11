package com.metara.metara.controller;

import com.metara.metara.models.dto.CommentDto;
import com.metara.metara.models.entity.Comment;
import com.metara.metara.models.entity.User;
import com.metara.metara.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/comments")
@Tag(name="Comment Managment", description = "API Comments" )
public class CommentController {

    @Autowired
    private CommentService commentService;

    //CREATE

    @Operation(summary = "Create new Comment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Comment created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping("/create")
    public ResponseEntity<?> createComment(@Valid @RequestBody CommentDto commentDto){
        try{
            Comment newComment = new Comment();
            newComment.setText(commentDto.getText());
            newComment.setCreatedAt(LocalDateTime.now());
            newComment.setUser(commentDto.getUser());
            newComment.setEvent(commentDto.getEvent());

            Comment createComment = commentService.createComment(newComment);
            return ResponseEntity.status(HttpStatus.CREATED).body(createComment);
        }catch (RuntimeException e){
            return  ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    //READ

    @Operation(summary = "Get all comments")
    @GetMapping
    public ResponseEntity<List<Comment>> getAllComments(){
        List<Comment> comments = commentService.getAllComments();
        return ResponseEntity.ok(comments);
    }

    @Operation(summary = "Get comment by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comment found"),
            @ApiResponse(responseCode = "404", description = "Comment not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getCommentById(@Parameter(description = "Comment ID") @PathVariable Long id){
        try{
            Comment comment = commentService.getCommentByIdOrThrow(id);
            return ResponseEntity.ok(comment);
        } catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }

    @Operation(summary = "Get comments by User ID")
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getCommentsByUserId(@Parameter(description = "User ID") @PathVariable Long userId){

            List<Comment> comments = commentService.getAllCommentsByUserId(userId);
            return ResponseEntity.ok(comments);

    }
    @Operation(summary = "Get comments by Event ID")
    @GetMapping("/event/{eventId}")
    public ResponseEntity<?> getCommentsByEventId(@Parameter(description = "Event ID") @PathVariable Long eventId){

        List<Comment> comments = commentService.getAllCommentsByEventId(eventId);
        return ResponseEntity.ok(comments);

    }

    @Operation(summary = "Get comments by Created at")
    @GetMapping("/createdAt/{createdAt}")
    public ResponseEntity<?> getCommentsByCreatedAt(@Parameter(description = "CreatedAt") @PathVariable LocalDateTime date){

        List<Comment> comments = commentService.getAllCommentsByCreatedAt(date);
        return ResponseEntity.ok(comments);

    }

    @Operation(summary = "Get comment Before created at ordered by text asc")
    @GetMapping("/before/createdAt/orderByText/asc/{createdAt}")
    public ResponseEntity<?> getCommentsBeforeCreatedAtOrderedByTextAsc(@Parameter(description = "CreatedAt") @PathVariable LocalDateTime date){
        List<Comment> comments = commentService.findByCreatedAtBeforeOrderByTextAsc(date);
        return ResponseEntity.ok(comments);
    }

    @Operation(summary = "Get comment Before created at ordered by text desc")
    @GetMapping("/before/createdAt/orderByText/desc/{createdAt}")
    public ResponseEntity<?> getCommentsBeforeCreatedAtOrderedByTextDesc(@Parameter(description = "CreatedAt") @PathVariable LocalDateTime date){
        List<Comment> comments = commentService.findByCreatedAtBeforeOrderByTextDesc(date);
        return ResponseEntity.ok(comments);
    }

    @Operation(summary = "Get comment After created at ordered by text asc")
    @GetMapping("/after/createdAt/orderByText/asc/{createdAt}")
    public ResponseEntity<?> getCommentsAfterCreatedAtOrderedByTextAsc(@Parameter(description = "CreatedAt") @PathVariable LocalDateTime date){
        List<Comment> comments = commentService.findByCreatedAtAfterOrderByTextAsc(date);
        return ResponseEntity.ok(comments);
    }

    @Operation(summary = "Get comment After created at ordered by text desc")
    @GetMapping("/after/createdAt/orderByText/desc/{createdAt}")
    public ResponseEntity<?> getCommentsAfterCreatedAtOrderedByTextDesc(@Parameter(description = "CreatedAt") @PathVariable LocalDateTime date){
        List<Comment> comments = commentService.findByCreatedAtAfterOrderByTextDesc(date);
        return ResponseEntity.ok(comments);
    }

    // UPDATE
    @Operation(summary = "Update comment", description = "Updates comment information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comment updated successfully"),
            @ApiResponse(responseCode = "404", description = "Comment not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> updateComment(
            @Parameter(description = "Comment ID")
            @PathVariable Long id,
            @RequestBody Comment updatedComment){
        try{
            Comment comment = commentService.updateComment(id,updatedComment);
            return  ResponseEntity.ok(comment);
        }
        catch (RuntimeException e){
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }


    //DELETE

    @Operation(summary = "Delete comment", description = "Deletes comment by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comment deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Comment not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteComment(@Parameter(description = "Comment ID")
                                           @PathVariable Long id){
        try{
            commentService.deleteComment(id);
            return  ResponseEntity.ok(Map.of("message", "Comment deleted successfully"));
        } catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }


}
