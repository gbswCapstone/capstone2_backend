package Capstone.capstoneProject.controller.character;

import Capstone.capstoneProject.dto.character.CharacterResponse;
import Capstone.capstoneProject.global.ApiResponse;
import Capstone.capstoneProject.service.character.CharacterService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("api/characters")
@RequiredArgsConstructor
public class CharacterController {
    private final CharacterService characterService;

    @GetMapping
    @Operation(summary = "내 캐릭터(등급) 조회", description = "내 캐릭터(등급) 조회 시 사용하는 API 입니다.")
    public ResponseEntity<ApiResponse<CharacterResponse>> getMyCharacter() {
        CharacterResponse result = characterService.getMyCharacter();
        return ResponseEntity.ok(ApiResponse.ok(result, "조회되었습니다."));
    }



}
