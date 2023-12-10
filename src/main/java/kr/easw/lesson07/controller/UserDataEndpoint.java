package kr.easw.lesson07.controller;

import kr.easw.lesson07.model.dto.RemoveUserDto;
import kr.easw.lesson07.model.dto.UserDataEntity;
import kr.easw.lesson07.service.UserDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserDataEndpoint {
    private final UserDataService userDataService;

    @GetMapping("/list")
    public List<String> listUsers() {
        // Fetch all users and convert them to a list of user IDs.
        return userDataService.getAllUsers().stream()
                .map(UserDataEntity::getUserId)
                .collect(Collectors.toList());
    }

    @PostMapping("/remove")
    public ResponseEntity<String> removeUser(@RequestBody RemoveUserDto removeUserDto) {
        boolean isRemoved = userDataService.removeUser(removeUserDto.getUserId());
        if (isRemoved) {
            return ResponseEntity.ok("User removed successfully");
        } else {
            return ResponseEntity.badRequest().body("Failed to remove user");
        }
    }

}