package com.metara.metara.service;

import com.metara.metara.models.entity.Comment;
import com.metara.metara.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private EventService eventService;

    @Autowired
    private UserService userService;


    //CREATE
    public Comment createComment(Comment comment){
        if(comment.getUser() == null || comment.getEvent() == null){
            return null;
        }
        return commentRepository.save(comment);

    }

    //READ
    public Optional<Comment> getCommentById(Long id) { return commentRepository.findById(id);}

    public Comment getCommentByIdOrThrow(Long id){
        return commentRepository.findById(id).orElseThrow(() -> new RuntimeException("Profile not found with id" + id));
    }

    public Optional<Comment> getCommentByUserId(Long userId){
        return commentRepository.findByUserId(userId);
    }
    public Optional<Comment> getCommentByEventId(Long eventId){
        return commentRepository.findByEventId(eventId);
    }
    public Optional<Comment> getCommentByCreatedAt(LocalDateTime localDateTime){
        return commentRepository.findByCreatedAt(localDateTime);
    }

    public List<Comment> getAllComments() {return commentRepository.findAll();}

    public List<Comment> getAllCommentsByEventId(Long eventId) { return commentRepository.findAllByEventId(eventId);}
    public List<Comment> getAllCommentsByUserId(Long userId) { return commentRepository.findAllByUserId(userId);}

    public List<Comment> findByCreatedAtBeforeOrderByCreatedAtAsc(LocalDateTime now) { return commentRepository.findByCreatedAtBeforeOrderByCreatedAtAsc(now);}
    public List<Comment> findByCreatedAtBeforeOrderByCreatedAtDesc(LocalDateTime now) { return commentRepository.findByCreatedAtBeforeOrderByCreatedAtDesc(now);}
    public List<Comment> findByCreatedAtAfterOrderByCreatedAtAsc(LocalDateTime now) { return commentRepository.findByCreatedAtAfterOrderByCreatedAtAsc(now);}
    public List<Comment> findByCreatedAtAfterOrderByCreatedAtDesc(LocalDateTime now) { return commentRepository.findByCreatedAtAfterOrderByCreatedAtDesc(now);}

    //UPDATE

    public Comment updateComment(Long id, Comment updatedComment){
        Comment existingComment = getCommentByIdOrThrow(id);

        existingComment.setText(updatedComment.getText());
        existingComment.setCreatedAt(updatedComment.getCreatedAt());

        return commentRepository.save(existingComment);

    }

    //DELETE
    public void deleteComment(Long id){
        if(!commentRepository.existsById(id)){
            throw new RuntimeException("Comment not found with id:" + id);
        }
        commentRepository.deleteById(id);
    }


}
