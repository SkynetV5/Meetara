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
        return commentRepository.findById(id).orElseThrow(() -> new RuntimeException("Comment not found with id" + id));
    }



    public List<Comment> getAllComments() {return commentRepository.findAll();}

    public List<Comment> getAllCommentsByEventId(Long eventId) { return commentRepository.findAllByEventId(eventId);}
    public List<Comment> getAllCommentsByUserId(Long userId) { return commentRepository.findAllByUserId(userId);}

    public List<Comment> getAllCommentsByCreatedAt(LocalDateTime date) { return commentRepository.findAllByCreatedAt(date);}
    public List<Comment> findByCreatedAtBeforeOrderByTextAsc(LocalDateTime now) { return commentRepository.findByCreatedAtBeforeOrderByTextAsc(now);}
    public List<Comment> findByCreatedAtBeforeOrderByTextDesc(LocalDateTime now) { return commentRepository.findByCreatedAtBeforeOrderByTextDesc(now);}
    public List<Comment> findByCreatedAtAfterOrderByTextAsc(LocalDateTime now) { return commentRepository.findByCreatedAtAfterOrderByTextAsc(now);}
    public List<Comment> findByCreatedAtAfterOrderByTextDesc(LocalDateTime now) { return commentRepository.findByCreatedAtAfterOrderByTextDesc(now);}

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
