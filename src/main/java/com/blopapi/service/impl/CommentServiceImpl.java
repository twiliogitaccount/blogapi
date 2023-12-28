package com.blopapi.service.impl;

import com.blopapi.entity.Comment;
import com.blopapi.entity.Post;
import com.blopapi.exceptions.ResourceNotFoundException;
import com.blopapi.payload.CommentDto;
import com.blopapi.repository.CommentRepository;
import com.blopapi.repository.PostRepository;
import com.blopapi.service.CommentService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    private PostRepository postRepo;
    private CommentRepository commentRepo;

    public CommentServiceImpl(PostRepository postRepo, CommentRepository commentRepo) {
        this.postRepo = postRepo;
        this.commentRepo = commentRepo;
    }

    @Override
    public CommentDto createComment(long postId, CommentDto commentDto) {
        Post post = postRepo.findById(postId).orElseThrow(
                ()->new ResourceNotFoundException(postId)
        );

        Comment comment = new Comment();
        comment.setName(commentDto.getName());
        comment.setEmail(commentDto.getEmail());
        comment.setBody(commentDto.getBody());

        comment.setPost(post);

        Comment savedComment = commentRepo.save(comment);

        CommentDto dto = new CommentDto();
        dto.setId(savedComment.getId());
        dto.setName(savedComment.getName());
        dto.setEmail(savedComment.getEmail());
        dto.setBody(savedComment.getBody());

        return dto;
    }

    public List<CommentDto>  findCommentByPostId(long postId){

        Post post = postRepo.findById(postId).orElseThrow(
                ()->new ResourceNotFoundException(postId)
        );

        List<Comment> comments = commentRepo.findByPostId(postId);

       return comments.stream().map(comment->mapToDto(comment)).collect(Collectors.toList());

    }

    @Override
    public void deleteCommentById(long postId, long id) {
        Post post = postRepo.findById(postId).orElseThrow(
                ()->new ResourceNotFoundException(postId)
        );

        Comment comment = commentRepo.findById(id).orElseThrow(
                ()->new ResourceNotFoundException(id)
        );

        commentRepo.deleteById(id);
    }

    @Override
    public CommentDto getCommentById(long postId, long id) {
        Post post = postRepo.findById(postId).orElseThrow(
                ()->new ResourceNotFoundException(postId)
        );

        Comment comment = commentRepo.findById(id).orElseThrow(
                ()->new ResourceNotFoundException(id)
        );


        CommentDto commentDto = mapToDto(comment);

        return commentDto;
    }

    @Override
    public CommentDto updateComment(Long postId, Long commentId, CommentDto commentDto) {
        // retrieve post entity by id
        Post post = postRepo.findById(postId).orElseThrow(
                () -> new ResourceNotFoundException(postId));

        // retrieve comment by id
        Comment comment = commentRepo.findById(commentId).orElseThrow(() ->
                new ResourceNotFoundException(commentId));


        comment.setName(commentDto.getName());
        comment.setEmail(commentDto.getEmail());
        comment.setBody(commentDto.getBody());

        Comment updatedComment = commentRepo.save(comment);
        return mapToDto(updatedComment);
    }





    CommentDto mapToDto(Comment comment){
        CommentDto dto = new CommentDto();
        dto.setId(comment.getId());
        dto.setName(comment.getName());
        dto.setEmail(comment.getEmail());
        dto.setBody(comment.getBody());
        return dto;
    }
}
