package kr.easw.lesson07.controller;

import kr.easw.lesson07.model.dto.ExceptionalResultDto;
import kr.easw.lesson07.model.dto.UserDataEntity;
import kr.easw.lesson07.service.UserDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class UserAuthEndpoint {
    private final UserDataService userDataService;


    // JWT 인증을 위해 사용되는 엔드포인트입니다.
    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody UserDataEntity entity) {
        try {
            // 로그인을 시도합니다.
            return ResponseEntity.ok(userDataService.createTokenWith(entity));
        } catch (Exception ex) {
            // 만약 로그인에 실패했다면, 400 Bad Request를 반환합니다.
            return ResponseEntity.badRequest().body(new ExceptionalResultDto(ex.getMessage()));
        }
    }

    @PostMapping("/register")
    public void register(@RequestBody UserDataEntity entity) {
        // 사용자가 이미 존재하는지 확인합니다.
        if (userDataService.isUserExists(entity.getUserId())) {
            // 이미 존재하는 경우 예외를 던집니다.
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User already exists");
        }

        // BCryptPasswordEncoder를 사용하여 비밀번호를 암호화합니다.
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode(entity.getPassword());

        // 새 사용자를 생성합니다.
        userDataService.createUser(new UserDataEntity(0, entity.getUserId(), encodedPassword, false));
        // 성공적으로 생성되면 메서드는 정상적으로 완료됩니다. 반환 값은 없습니다.
    }


}