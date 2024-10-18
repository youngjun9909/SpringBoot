package org.example.springbootdeveloper.service;

import lombok.RequiredArgsConstructor;
import org.example.springbootdeveloper.dto.request.BookRequestDto;
import org.example.springbootdeveloper.dto.request.BookRequestUpdateDto;
import org.example.springbootdeveloper.dto.response.BookResponseDto;
import org.example.springbootdeveloper.entity.Book;
import org.example.springbootdeveloper.entity.Category;
import org.example.springbootdeveloper.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    // 게시글 생성
    public BookResponseDto createBook(BookRequestDto requestDto) {
        Book book = new Book(
                null,
                requestDto.getWriter(),
                requestDto.getTitle(),
                requestDto.getContent(),
                requestDto.getCategory()
        );
        Book savedBook = bookRepository.save(book);
        return convertToResponseDto(savedBook);
    }

    // Entity -> ResponseDto 로 변환
    private BookResponseDto convertToResponseDto(Book book) {
        return new BookResponseDto(
                book.getId(),
                book.getWriter(),
                book.getTitle(),
                book.getContent(),
                book.getCategory()
        );
    }

    // 전체 책 조회
    public List<BookResponseDto> getAllBooks() {
        return bookRepository.findAll().stream()
                .map(this :: convertToResponseDto)
                .collect(Collectors.toList());
    }

    // 특정 단어가 포함된 책 조회
    public List<BookResponseDto> getBooksByTitleContaining(String keyword) {
        List<Book>  books = bookRepository.findByTitleContaining(keyword);
               return books.stream().map(this :: convertToResponseDto)
                .collect(Collectors.toList());
    }

    // 특정 책 조회
    public BookResponseDto getBookById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("책을 찾을 수 없습니다 " + id));
        return convertToResponseDto(book);
    }

    // 카테고리로 책 조회
    public List<BookResponseDto> getBooksByCategory(Category category) {
        return bookRepository.findByCategory(category)
                .stream().map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    public List<BookResponseDto> getBooksByCategoryAndWriter(Category category, String writer) {
        List<Book> books;
        if(category == null) {
            books = bookRepository.findByWriter(writer);
        }else{
            books = bookRepository.findByCategoryAndWriter(category, writer);
        }
        return books.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    //둘다
    public List<BookResponseDto> getBookByTitleContainingAndCategory(String keyword, Category category) {
        List<Book> books;
        if(category == null) {
            books = bookRepository.findByTitleContaining(keyword);
        }else{
            books = bookRepository.findByTitleContainingAndCategory(keyword, category);
        }
        return books.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }



    // 특정 책 수정
    public BookResponseDto updateBook(Long id, BookRequestUpdateDto updateDto) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("책을 찾을 수 없습니다 " + id));
        book.setTitle(updateDto.getTitle());
        book.setContent(updateDto.getContent());
        book.setCategory(updateDto.getCategory());

        Book updatedBook = bookRepository.save(book);
        return convertToResponseDto(updatedBook);
    }

    // 책 삭제
    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }
}
