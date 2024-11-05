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
    - request의 header에서 토큰을 추출하여 검증
    - security의 context에 접근 제어자 정보 등록

    - OncePerRequestFilter
    : 모든 요청마다 한번식 필터가 실행 되도록 보장
 */
@Component // 스프링에서 해당 클래스를 관리하도록 지정, 애플리케이션에서 자동으로 사용될 수 있도록 설정
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    // JWT 토큰을 처리하는 JwtProvider 의존성 주입
    // JWT 인증에 사용
    private final JwtProvider jwtProvider;


    /*
        doFilterInternal
        : 요청의 헤더에서 JWT 토큰을 추출
        : JwtProvider 에서 만든 removeBearer()을 호출하여 토큰을 파싱
        : JwtProvider를 사용하여 토큰 검증 및 사용자 ID 추출
        : 추출한 사용자 ID를 바탕으로 SecurityContext에 인증 정보를 설정하는 메서드 호출
     */
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            // 요청 헤더에서 JWT 토큰 추출
            String authorizationHeader = request.getHeader("Authorization");

            // 헤더에서 토큰을 파싱하여 가져옴 ("Bearer "을 제거)
            String token = (authorizationHeader != null && authorizationHeader.startsWith("Bearer "))
                    ? jwtProvider.removeBearer(authorizationHeader)
                    : null;

            // 토큰이 없거나 유효하지 않으면 필터 체인을 타고 다음 단계로 이동
            if(token == null || !jwtProvider.isValidToken(token)) {
                // 토큰이 유효하지 않은 경우
                // : 시큐리티 설정 없이 로직이 실행
                filterChain.doFilter(request, response);
                // 이후의 필터를 거치지 않고 해당 메서드가 종료
                return;
            }

            // JWT 토큰이 유효할 경우 해당 토큰에서 ID 추출
            String userId = jwtProvider.getUserIdFromJwt(token);

            // 추출한 사용자 id를 바탕으로 SecurityContext에 인증 정보 설정
            // : setAuthenticationContext()는 요청에서 userId 값을 SecurityContext에 인증 정보로 설정
            // > UsernamePasswordAuthenticationToken을 생성하고, 해당 토큰에 userId 값을 넣어 인증 정보로 등록
            // > Spring Security는 SecurityContextHolder 에 있는 인증 정보를 자동으로
            //  , 컨트롤러의 메서드에서 주입시킬 수 있음(@AuthenticationPrincipal)

            setAuthenticationContext(request, userId);
        }catch (Exception e){
            e.printStackTrace();
        }

        filterChain.doFilter(request, response);
    }


    /*
    setAuthenticationContext
    : SecurityContext에  인증 정보를 설정하는 메서드
     */
    private void setAuthenticationContext(HttpServletRequest request, String userId) {
        //  사용자 ID를 바탕으로 UsernamePasswordAuthenticationToken (인증 토큰) 생성
        //  기본설정 - 권한 없음
        AbstractAuthenticationToken authenticationToken
                = new UsernamePasswordAuthenticationToken(userId, null, AuthorityUtils.NO_AUTHORITIES);

        // 요청에 대한 세부 정보를 설정
        // : 생성된 인증 토큰에 요청의 세부 사항을 설정
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        // 빈 SecurityContext 생성 후 , 인증 토큰을 주입
        // : 사용자가 인증되었다는 정보를 담음
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authenticationToken);

        // SecurityContextHolder애 생성된 컨텍스트를 설정
        SecurityContextHolder.setContext(securityContext);
    }
}

/*
    1. doFilterInternal
        Authorization 헤더에서 JWT 토큰을 추출하여 검증
        유효하지 않은 토큰은 필터 체인 다음 단꼐로 넘감
        유효한 토큰일 경우 userID를 추출하여 인증 설정 메서드로 전달

    2. setAuthenticationContext (인증 설정 메서드)
        useId를 포함한 인증 토큰을 생성
        요청에 대한 세부 정보를 인증 토큰에 설정
        securityContextHolder에 사용자 인증 정볼르 설정
        , 이후의 요청에서 인증된 사용자로 인식되도록 함
 */
