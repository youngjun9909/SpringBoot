package org.example.springbootdeveloper.repository;

import org.example.springbootdeveloper.entity.Book;
import org.example.springbootdeveloper.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    // 사용자 쿼리 메서드
    // 1. 제목으로 게시글 검색

    // 인터페이스 메서드 기본 구조
    // 반환타입 메서드명 (매개변수, ...);
    List<Book> findByTitleContaining(String keyword);
    // SQL 문 변환 ) SELECT * FORM book WHERE title LIKE %keyword%;

    // 2. 카테고리 별 책 조회
    List<Book> findByCategory(Category category);
    // SQL 문 변환) SELECT * FROM book WHERE category=category

    // 키워드 and 카테고리  조회
    List<Book> findByTitleContainingAndCategory(String keyword, Category category);

    // 3. 특정 카테고리에서 작성자의 책 조회
    List<Book> findByCategoryAndWriter(Category category, String writer);

    // +) 카테고리를 지정하지 않을 경우 - 모든 카테고리에서 작성자의 책 조회
    List<Book> findByWriter(String writer);



    // cf) 일반 메서드
    // 반환타입 메서드명 (매개변수, ..) { 메서드 구현부 }


}
