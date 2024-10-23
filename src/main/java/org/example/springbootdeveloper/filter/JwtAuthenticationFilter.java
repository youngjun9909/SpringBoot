package org.example.springbootdeveloper.filter;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.springbootdeveloper.provider.JwtProvider;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/*
    JWT 인증 필터
    - 요청에서 JWT 토큰을 추출
    - 해당 토큰을 검증하여 유효하다면 SecurityContext에 사용자 정보를 등록
    - OncePerRequestFilter
    : 모든 요청마다 한번식 필터가 실행 되도록 보장
 */

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

     // JWT 토큰을 처리하는 JwtProvider 의존성 주입
    private final JwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        try {
           // 요청의 Authorization 헤더에서 Bearer 토큰 추출
            String token = parseBearerToken(request);

            // 토큰이 없거나 유효하지 않으면 필터 체인을 타고 다음 단계로 이동
            if(token == null) {
                filterChain.doFilter(request, response);
                return;
            }

            // JWT 토큰이 유효할 경우 해당 토큰에서 사용자 ID 추출
            String userId = jwtProvider.validate(token);

            // 추출한 사용자 id를 바탕으로 SecurityContext에 인증 정보 설정
            setAuthenticationContext(request, userId);
        }catch (Exception e){

        }
    }


    /*
    setAuthenticationContext
    : SecurityContext에  인증 정보를 설정하는 메서드
     */
    private void setAuthenticationContext(HttpServletRequest request, String userId) {
        //  사용자 ID를 바탕으로 UsernamePasswordAuthenticationToken 생성
        // : 권한 정보 없음
        AbstractAuthenticationToken authenticationToken
                = new UsernamePasswordAuthenticationToken(userId, null, AuthorityUtils.NO_AUTHORITIES);

        // 요청에 대한 세부 정보를 설정
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        // 빈 SecurityContext 생성 후 , 인증 토큰을 주입
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authenticationToken);

        // SecurityContextHolder애 생성된 컨텍스트를 설정
        SecurityContextHolder.setContext(securityContext);
    }


    /*
        요청의 Authorization 헤더에서 Bearer 토큰 추출하는 메서드
        @param: request(HttpServletRequest)
        @return: JWT 토큰 문자열, 없으며 null 반환
     */
    private String parseBearerToken(HttpServletRequest request) {
        // authorization 헤더에서 값 추출
        String authorizationHeader = request.getHeader("Authorization");

        if(!StringUtils.hasText(authorizationHeader) || !authorizationHeader.startsWith("Bearer ")) {
            return null;
        }

        return authorizationHeader.substring(7);
    }
}
