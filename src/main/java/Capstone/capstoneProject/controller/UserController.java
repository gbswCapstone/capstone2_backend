package Capstone.capstoneProject.controller;

import Capstone.capstoneProject.dto.ApiResponse;
import Capstone.capstoneProject.dto.ProfilePatchDTO;
import Capstone.capstoneProject.dto.SecuritySignupRequest;
import Capstone.capstoneProject.dto.UserResponseDTO;
import Capstone.capstoneProject.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @Operation(summary = "회원가입", description = "회원가입 시 사용하는 API 입니다.")
    @PostMapping("api/auth/signup")
    public ResponseEntity<ApiResponse<Object>> signup(@RequestBody SecuritySignupRequest request) {
        userService.signup(request);
        return ResponseEntity.ok(ApiResponse.ok());
    }

    @GetMapping("/profile")
    @Operation(summary = "프로필 조회하기", description = "프로필 조회 시 사용하는 API 입니다.")
    public ResponseEntity<ApiResponse<UserResponseDTO>> myProfile() {
        UserResponseDTO result = userService.getMyProfile();
        return ResponseEntity.ok(ApiResponse.ok(result));
    }

    @PutMapping("/profile")
    @Operation(summary = "프로필 수정하기", description = "프로필 수정 시 사용하는 API 입니다.")
    public ResponseEntity<ApiResponse<ProfilePatchDTO>> myProfilePatch(
            @RequestBody ProfilePatchDTO dto
    ) {
        ProfilePatchDTO result = userService.patchMyProfile(dto);
        return ResponseEntity.ok(ApiResponse.ok(result));
    }

}
