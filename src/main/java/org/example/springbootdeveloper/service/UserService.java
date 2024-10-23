package org.example.springbootdeveloper.service;

import org.example.springbootdeveloper.dto.request.UserLoginRequestDto;
import org.example.springbootdeveloper.dto.request.UserRequestDto;
import org.example.springbootdeveloper.dto.response.UserLoginResponseDto;
import org.example.springbootdeveloper.entity.User;
import org.example.springbootdeveloper.provider.JwtProvider;
import org.example.springbootdeveloper.repository.UserRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserService {

    // 비즈니스 로직
    // Controller의 요청을 받아 필요한 데이터를 Repository를 통해 얻고
    // , 기능 구현 후 응답을 Controller에 전달

    // +) 기능 구현에 있어 필요한 보안을 설정
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;

    public UserService(UserRepository userRepository, @Lazy AuthenticationManager authenticationManager
            , BCryptPasswordEncoder bCryptPasswordEncoder, JwtProvider jwtProvider) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.jwtProvider = jwtProvider;

    }


    public String signup(UserRequestDto dto) {
        try{
            // 중복되는 이메일 검증
            if(userRepository.existsByEmail(dto.getEmail())) {
                throw new RuntimeException("Email already exists");
            }
            // 패스워드 암호화
            String encodedPassword = bCryptPasswordEncoder.encode(dto.getPassword());

            User user = User.builder()
                    .email(dto.getEmail())
                    .password(encodedPassword)
                    .createdAt(LocalDateTime.now())
                    .build();
            userRepository.save(user);
            return "회원가입 완료";
        } catch (Exception e) {
            return "회원가입 실패" + e.getMessage();
        }
    }

    public UserLoginResponseDto login(UserLoginRequestDto dto) {
        try{
            // 해당 이메일의 유저가 있는지 검색하고 있을 경우 해당 데이터 반환
            User user = userRepository.findByEmail(dto.getEmail())
                    .orElseThrow(() -> new RuntimeException("유효하지 않은 이메일입니다."));

            // .matches(펑문 비밀번호, 암호화된 비밀번호)
            // : 암호화된 비밀번호와 평문 비밀번호를 비교하여 일치 여부를 반환
            // : 일치할 경우 true 아닐경우 false
            if(!bCryptPasswordEncoder.matches(dto.getPassword(), user.getPassword())) {
                // 일차하지 않은 경우 (!false)
                throw new RuntimeException("비밀번호가 일치하지 않습니다.");
            }

//            // 사용자 인증을 처리(인증 실패 시 예외 발생)
//            Authentication authentication = authenticationManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(
//                            dto.getEmail(),
//                            dto.getPassword()
//                    )
//            );

            // 인증 성공 후 JWT 토큰 생성
            String token = jwtProvider.generateJwtToken(dto.getEmail());
            return new UserLoginResponseDto(token);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("로그인 실패" + e.getMessage());
        }

    }
}
