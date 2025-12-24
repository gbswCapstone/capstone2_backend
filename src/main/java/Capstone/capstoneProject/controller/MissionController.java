package Capstone.capstoneProject.controller;

import Capstone.capstoneProject.dto.Missions.MissionListDTO;
import Capstone.capstoneProject.dto.Missions.MissionResponse;
import Capstone.capstoneProject.dto.Missions.MissionCreate;
import Capstone.capstoneProject.enums.DateSortType;
import Capstone.capstoneProject.enums.MissionCategory;
import Capstone.capstoneProject.enums.SortType;
import Capstone.capstoneProject.global.ApiResponse;
import Capstone.capstoneProject.service.MissionService;
import com.google.protobuf.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/missions")
@RequiredArgsConstructor
public class MissionController {

    private final MissionService missionService;

    @PostMapping("/personal")
    @Operation(summary = "개인 미션 생성", description = "미션 생성 시 사용하는 API 입니다.")
    public ResponseEntity<ApiResponse<MissionResponse>> createMission
            (@RequestBody MissionCreate request) {
        MissionResponse result = missionService.createPersonalMission(request);
        return ResponseEntity.ok(ApiResponse.ok(result, "생성되었습니다."));
    }

    @PostMapping("/month-goal")
    @Operation(summary = "이번 달 목표 미션 생성",
            description = "이번 달 목표 미션 생성 시 사용하는 API 입니다.\n"
            + "이번달 최대 지출 금액을 입력해주면 됩니다.")
    public ResponseEntity<ApiResponse<MissionResponse>> createMonthGoal
            (@RequestBody BigDecimal price) {
        MissionResponse result = missionService.createMonthGoalMission(price);
        return ResponseEntity.ok(ApiResponse.ok(result, "생성되었습니다."));
    }

//    @GetMapping("/month-goal")
//    @Operation(summary = "이번달 목표 미션 조회", description = "이번 달 목표 미션 조회 시 사용하는 API 입니다.")
//    public ResponseEntity<ApiResponse<>>


    @GetMapping
    @Operation(summary = "유저 전체미션 조회", description = "유저 전체미션 조회 시 사용하는 API 입니다.")
    public ResponseEntity<ApiResponse<List<MissionListDTO>>> getMissions
            (@RequestParam(required = false) DateSortType sortType,
             @RequestParam(required = false) MissionCategory category
             ) {
        List<MissionListDTO> result = missionService.getMissions(sortType, category);
        return ResponseEntity.ok(ApiResponse.ok(result, "조회되었습니다."));
    }


//    @PostMapping("/complete/{missionId}")
//    @Operation(summary = "미션 완료하기", description = "미션 완료 시 사용하는 API 입니다.")
//    public ResponseEntity<ApiResponse<Void>> completeMission(@PathVariable Long missionId) {
//        missionService.completeMission(missionId);
//        return ResponseEntity.ok(ApiResponse.ok("완료되었습니다."));
//    }




//
//    @GetMapping("/{missionId}")
//    @Operation(summary = "유저 미션 상세 조회", description = "유저 미션 상세 조회 시 사용하는 API 입니다.")
//    public ResponseEntity<ApiResponse<MissionResponse>> getMission(@PathVariable Long missionId) {
//        MissionResponse result = missionService.getMission(missionId);
//        return ResponseEntity.ok(ApiResponse.ok(result, "조회되었습니다."));
//    }




}
