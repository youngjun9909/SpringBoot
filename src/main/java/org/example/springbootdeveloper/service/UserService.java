package org.example.springbootdeveloper.service;

import lombok.RequiredArgsConstructor;
import org.example.springbootdeveloper.dto.request.UserLoginRequestDto;
import org.example.springbootdeveloper.dto.request.UserRequestDto;
import org.example.springbootdeveloper.entity.User;
import org.example.springbootdeveloper.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    // 비즈니스 로직
    // Controller의 요청을 받아 필요한 데이터를 Repository를 통해 얻고
    // , 기능 구현 후 응답을 Controller에 전달

    // +) 기능 구현에 있어 필요한 보안을 설정
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;


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

    public String login(UserLoginRequestDto dto) {
        return null;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }
}
