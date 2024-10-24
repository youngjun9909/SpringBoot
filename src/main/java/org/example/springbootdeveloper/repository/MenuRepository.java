package org.example.springbootdeveloper.repository;

import org.example.springbootdeveloper.entity.Category;
import org.example.springbootdeveloper.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {
    // 2. 카테고리 별 책 조회
    List<Menu> findByCategory(String category);
}
