package org.example.springbootdeveloper.common.constant;

public class ApiMappingPattern {
    // 인증 절차 생성 로직
    // : 회원가입, 로그인
    public static final String AUTH = "/api/v1/auth";

    // 일발 회원 관련 로직: 회원 조회(단건, 전체, 필터링), 수정, 삭제
    public static final String USER = "/api/v1/users";

    public static final String POST = "/api/v1/posts";
    public static final String BOOK = "/api/v1/books";

    public static final String MENU = "/api/v1/menus";
//    public static final String GROUP = "/api/group";
    public static final String TASK = "/api/v1/todos";
}
