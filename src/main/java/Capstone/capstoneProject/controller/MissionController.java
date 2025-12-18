package Capstone.capstoneProject.controller;

import Capstone.capstoneProject.dto.Missions.MissionResponse;
import Capstone.capstoneProject.dto.Missions.PersonalMissionCreate;
import Capstone.capstoneProject.enums.SortType;
import Capstone.capstoneProject.global.ApiResponse;
import Capstone.capstoneProject.service.MissionService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

//    @GetMapping
//    @Operation(summary = "유저 전체미션 조회", description = "유저 전체미션 조회 시 사용하는 API 입니다.")
//    public ResponseEntity<ApiResponse<List<MissionListDTO>>> getMissions(@RequestParam SortType sortType) {
//        MissionListDTO result = missionService.getMissions(sortType);
//        return ResponseEntity.ok(ApiResponse.ok(result, "조회되었습니다."));
//    }

//    @GetMapping("/{missionId}")
//    @Operation(summary = "유저 미션 상세 조회", description = "유저 미션 상세 조회 시 사용하는 API 입니다.")
//    public ResponseEntity<ApiResponse<MissionResponse>> getMission(@PathVariable Long missionId) {
//        MissionResponse result = missionService.getMission(missionId);
//        return ResponseEntity.ok(ApiResponse.ok(result, "조회되었습니다."));
//    }




}
