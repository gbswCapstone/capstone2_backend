package Capstone.capstoneProject.controller;

import Capstone.capstoneProject.dto.Missions.MissionResponse;
import Capstone.capstoneProject.dto.Missions.PersonalMissionCreate;
import Capstone.capstoneProject.global.ApiResponse;
import Capstone.capstoneProject.service.MissionService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/missions")
@RequiredArgsConstructor
public class MissionController {

    private final MissionService missionService;

    @PostMapping("/personal")
    @Operation(summary = "개인 미션 생성", description = "미션 생성 시 사용하는 API 입니다.")
    public ResponseEntity<ApiResponse<MissionResponse>> createMission
            (@RequestBody PersonalMissionCreate request) {
        MissionResponse result = missionService.createPersonalMission(request);
        return ResponseEntity.ok(ApiResponse.ok(result, "생성되었습니다."));
    }
}
