package Capstone.capstoneProject.controller.ranking;

import Capstone.capstoneProject.service.ranking.RankingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/rankings")
@RequiredArgsConstructor
public class RankingController {
    private final RankingService rankingService;

}
