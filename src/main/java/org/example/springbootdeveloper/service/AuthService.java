package org.example.springbootdeveloper.service;

import lombok.RequiredArgsConstructor;
import org.example.springbootdeveloper.common.constant.ResponseMessage;
import org.example.springbootdeveloper.dto.request.UserSignInRequestDto;
import org.example.springbootdeveloper.dto.request.UserSignUpRequestDto;
import org.example.springbootdeveloper.dto.response.ResponseDto;
import org.example.springbootdeveloper.dto.response.UserSignInResponseDto;
import org.example.springbootdeveloper.dto.response.UserSignUpResponseDto;
import org.example.springbootdeveloper.entity.User;
import org.example.springbootdeveloper.provider.JwtProvider;
import org.example.springbootdeveloper.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtProvider jwtProvider;

    // 회원 가입
    public ResponseDto<UserSignUpResponseDto> signup(UserSignUpRequestDto dto) {
        String email = dto.getEmail();
        String password = dto.getPassword();

        UserSignUpResponseDto data = null;
        User user = null;

        try {
            // 중복되는 이메일 검증
            if (userRepository.existsByEmail(email))
                return ResponseDto.setFailed(ResponseMessage.EXIST_DATA);

            // 패스워드 암호화
            String encodedPassword = bCryptPasswordEncoder.encode(password);

            user = User.builder()
                    .email(email)
                    .password(encodedPassword)
                    .createdAt(LocalDateTime.now())
                    .build();

            userRepository.save(user);

        } catch (Exception e) {
            e.printStackTrace();
            ResponseDto.setFailed(ResponseMessage.DATABASE_ERROR);
        }

        data = new UserSignUpResponseDto(user);
        return ResponseDto.setSuccess(ResponseMessage.SUCCESS, data);
    }

    // 로그인 구현
    public ResponseDto<UserSignInResponseDto> signIn(UserSignInRequestDto dto) {
        String email = dto.getEmail();
        String password = dto.getPassword();

        UserSignInResponseDto data = null;
        User user = null;

        try {
            // 해당 이메일의 유저가 있는지 검색하고 있을 경우 해당 데이터를 반환

            if (userRepository.findByEmail(email).isPresent()) {
                user = userRepository.findByEmail(email).get();
            } else {
                ResponseDto.setFailed(ResponseMessage.NOT_EXIST_USER);
            }

            // .matches(평문 비밀번호, 암호화된 비밀번호)
            // : 평문 비밀번호와 암호화된 비밀번호를 비교하여 일치 여부를 반환
            // : 일치할 경우 true
            // : 일치하지 않을 경우 false
            if (!bCryptPasswordEncoder.matches(password, user.getPassword())) {
                // 일치하지 않은 경우(!false)
                ResponseDto.setFailed(ResponseMessage.NOT_MATCH_PASSWORD);
            }

            // 인증 성공 후 JWT 토큰 생성
            String token = jwtProvider.generateJwtToken(email);

            data = new UserSignInResponseDto(token, user);
            return ResponseDto.setSuccess(ResponseMessage.SUCCESS, data);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.setFailed(ResponseMessage.DATABASE_ERROR);
        }
    }
}