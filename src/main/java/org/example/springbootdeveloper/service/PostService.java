package org.example.springbootdeveloper.service;

import org.example.springbootdeveloper.dto.request.PostRequestDto;
import org.example.springbootdeveloper.dto.response.CommentResponseDto;
import org.example.springbootdeveloper.dto.response.PostResponseDto;
import org.example.springbootdeveloper.dto.response.ResponseDto;
import org.example.springbootdeveloper.entity.Post;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostService {
    public ResponseDto<List<PostResponseDto>> getAllPosts;

    private  PostResponseDto convertToResponseDto(Post post) {
        return  new PostResponseDto(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getAuthor(),
                post.getComments().stream().map(comment -> new CommentResponseDto(
                        comment.getId(),
                        comment.getContent(),
                        comment.getCommenter()

                )).collect(Collectors.toUnmodifiableList());
        )
    }

    public ResponseDto<PostResponseDto> createPost(PostRequestDto dto) {
      return null;
    }

    public ResponseDto<PostResponseDto> getPostById(Long postId) {
        return null;
    }

    public ResponseDto<PostResponseDto> updatePost(Long postId, PostRequestDto dto) {
        return null;
    }

    public ResponseDto<Void> deletePost(Long postId) {
        return null;
    }
}
