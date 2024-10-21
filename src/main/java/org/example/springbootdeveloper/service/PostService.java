package org.example.springbootdeveloper.service;

import org.example.springbootdeveloper.dto.request.PostRequestDto;
import org.example.springbootdeveloper.dto.response.CommentResponseDto;
import org.example.springbootdeveloper.dto.response.PostResponseDto;
import org.example.springbootdeveloper.dto.response.ResponseDto;
import org.example.springbootdeveloper.entity.Post;
import org.example.springbootdeveloper.repository.PostRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostService {
    private final PostRepository postRepository;
    public ResponseDto<List<PostResponseDto>> getAllPosts;

    public PostService(PostRepository  postRepository) {
        this.postRepository = postRepository;
    }


    public ResponseDto<PostResponseDto> createPost(PostRequestDto dto) {
        try {
            Post post = Post.builder()
                    .title(dto.getTitle())
                    .content(dto.getContent())
                    .author(dto.getAuthor())
                    .build();
            postRepository.save(post);
            return ResponseDto.setSuccess("게시글이 정상적으로 등록되었습니다.", convertToPostResponseDto(post));
        } catch (Exception e) {
            return ResponseDto.setFailed("게시글 등록 중 오류가 발생했습니다: " + e.getMessage());
        }

    }

    public ResponseDto<PostResponseDto> getPostById(Long postId) {
        try{
            Post post = postRepository.findById(postId)
                    .orElseThrow(() -> new IllegalArgumentException("게시물을 찾을 수 없습니다"));
            return ResponseDto.setSuccess("게시글 조회 완료.", convertToPostResponseDto(post));
        } catch (Exception e) {
            return ResponseDto.setFailed(("게시글 조회 중 오류가 발생했습니다." + e.getMessage()));
        }

    }

    public ResponseDto<PostResponseDto> updatePost(Long postId, PostRequestDto dto) {
        try{
            Post post = postRepository.findById(postId)
                    .orElseThrow(() -> new IllegalArgumentException("게시물을 찾을 수 없습니다." + postId));
            post.setTitle(dto.getTitle());
            post.setContent(dto.getContent());
            Post updatedPost = postRepository.save(post);

            return ResponseDto.setSuccess("게시물 수정 완료.", convertToPostResponseDto(updatedPost));

        } catch (Exception e) {
            return ResponseDto.setFailed(("게시물 수정에 실패하였습니다."+ e.getMessage()));
        }

    }

    public ResponseDto<Void> deletePost(Long postId) {
        try{
            Post post = postRepository.findById(postId)
                    .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다." + postId));
            postRepository.delete(post);
            return ResponseDto.setSuccess("게시글 삭제 완료.", null);
        } catch (Exception e) {
            return ResponseDto.setFailed(("게시물 삭제에 실패하였습니다."+ e.getMessage()));
        }
    }

    private PostResponseDto convertToPostResponseDto(Post post) {
        List<CommentResponseDto> commentDtos = (post.getComments() != null) ? post.getComments().stream()
                .map(comment -> new CommentResponseDto(comment.getId(), post.getId(), comment.getContent(), comment.getCommenter()))
                .collect(Collectors.toList()) : new ArrayList<CommentResponseDto>();

        return new PostResponseDto(
                post.getId(), post.getTitle(), post.getContent(), post.getAuthor(), commentDtos
        );
    }
}
