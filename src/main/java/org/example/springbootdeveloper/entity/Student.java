package org.example.springbootdeveloper.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Table(name="students") // 해당 엔터티가 "students"라는 테이블과 매핑됨 명시
@Entity // JPA 엔터티임을 선언, DB 테이블과 매핑
@Getter
@Setter
public class Student {
    @Id // 해당 필드가 테이블의 기본 키 (PK)임을 명시
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 기본 키 값을 자동 생성 - DB의 AUTO_INCREMENT
    private Long id;

    private String name;
    private String email;

    protected  Student() {}
    // JPA는 엔터티 생성 시 기본 생성자를 사용 - 필수

    public Student(String name, String email) {
        this.name = name;
        this.email = email;
    }
}
