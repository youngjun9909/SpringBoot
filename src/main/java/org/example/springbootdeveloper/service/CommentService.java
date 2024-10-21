package org.example.springbootdeveloper.service;

import org.example.springbootdeveloper.dto.request.CommentRequestDto;
import org.example.springbootdeveloper.dto.response.CommentResponseDto;
import org.example.springbootdeveloper.dto.response.ResponseDto;
import org.example.springbootdeveloper.entity.Comment;
import org.example.springbootdeveloper.entity.Post;
import org.example.springbootdeveloper.repository.CommentRepository;
import org.example.springbootdeveloper.repository.PostRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    public CommentService(CommentRepository commentRepository, PostRepository postRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
    }

    public ResponseDto<CommentResponseDto> createComment(CommentRequestDto dto) {
        try{
            Post post = postRepository.findById(dto.getPostId()).
                    orElseThrow(() -> new IllegalArgumentException("게시물 조회에 실패 하였습니다"));
            Comment comment = Comment.builder()
                                    .post(post)
                                    .content(dto.getContent())
                                    .commenter(dto.getCommenter())
                                    .build();
            commentRepository.save(comment);

            return ResponseDto.setSuccess("댓글이 등록되었습니다.", convertToCommentResponseDto(comment));
        } catch (Exception e) {
            return ResponseDto.setFailed("댓글 등록 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    public ResponseDto<List<CommentResponseDto>> getCommentsByPost(Long postId) {
        try{
            Comment comment = commentRepository.findById(postId)
                    .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다" + postId));
            return ResponseDto.setSuccess("조회 성공", Collections.singletonList(convertToCommentResponseDto(comment)));
        } catch (Exception e) {
            return ResponseDto.setFailed("댓글 조회 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    public ResponseDto<CommentResponseDto> updateComment(Long id, CommentRequestDto dto) {
        try{
            Comment comment = commentRepository.findById(id)
                    .orElseThrow(() ->  new IllegalArgumentException("게시글을 찾을 수 없습니다" + id));
            comment.setContent(dto.getContent());
            Comment savedComment = commentRepository.save(comment);

            return ResponseDto.setSuccess("수정이 완료 되었습니다", convertToCommentResponseDto(savedComment));

        } catch (Exception e) {
            return ResponseDto.setFailed("댓글 수정 중 오류가 발생했습니다: " + e.getMessage());
        }
    }


    public ResponseDto<Void> deleteComment(Long commentId) {
        try{
            Comment comment = commentRepository.findById(commentId)
                    .orElseThrow(() -> new IllegalArgumentException("댓글 조회 중 오류" + commentId));
            commentRepository.delete(comment);
            return ResponseDto.setSuccess("댓글이 삭제 되었습니다.", null);
        } catch (Exception e) {
            return ResponseDto.setFailed("댓글 삭제 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    private CommentResponseDto convertToCommentResponseDto(Comment comment) {
        return new CommentResponseDto(
                comment.getId(), comment.getPost().getId(), comment.getContent()
                , comment.getCommenter()
        );
    }
}
