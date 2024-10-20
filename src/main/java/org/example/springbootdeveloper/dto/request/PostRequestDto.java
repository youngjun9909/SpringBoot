package org.example.springbootdeveloper.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostRequestDto {
    private String title;
    private String content;
    private String author;
}