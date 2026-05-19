package Capstone.capstoneProject.service.experience;

import Capstone.capstoneProject.entity.experience.ExperienceHistory;
import Capstone.capstoneProject.entity.experience.UserExperience;
import Capstone.capstoneProject.entity.user.Users;
import Capstone.capstoneProject.enums.SourceType;
import Capstone.capstoneProject.exceptions.notfound.UserNotFoundException;
import Capstone.capstoneProject.policy.LevelPolicy;
import Capstone.capstoneProject.repository.experience.ExperienceHistoryRepository;
import Capstone.capstoneProject.repository.experience.UserExperienceRepository;
import Capstone.capstoneProject.repository.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Transactional
public class ExperienceService {

    private final UserExperienceRepository userExperienceRepository;
    private final ExperienceHistoryRepository experienceHistoryRepository;
    private final UserRepository userRepository;

    public void giveExperience(
            Long userId,
            int experience,
            SourceType sourceType,
            Long sourceId
    ) {
        Users user = userRepository.findByIdAndDeletedAtIsNull(userId)
                .orElseThrow(() -> new UserNotFoundException("해당 유저를 찾을 수 없습니다."));

        // 유저 경험치 조회 (없으면 생성)
        UserExperience userExp = userExperienceRepository
                .findByUser(user)
                .orElseGet(() -> createInitialExperience(user));

        int beforeExp = userExp.getTotalExperience();
        int afterExp = beforeExp + experience;

        userExp.setTotalExperience(afterExp);

        // 레벨 계산 (Policy 사용)
        int newLevel = LevelPolicy.calculateLevel(afterExp);
        if (newLevel > userExp.getLevel()) {
            userExp.setLevel(newLevel);
        }

        userExperienceRepository.save(userExp);

        // 경험치 히스토리 저장
        ExperienceHistory history = ExperienceHistory.builder()
                .user(user)
                .experience(experience)
                .sourceType(sourceType)
                .sourceId(sourceId)
                .description(sourceType.getDescription())
                .build();

        experienceHistoryRepository.save(history);
    }

    private UserExperience createInitialExperience(Users user) {
        UserExperience exp = UserExperience.builder()
                .user(user)
                .totalExperience(0)
                .level(1)
                .build();

        return userExperienceRepository.save(exp);
    }
}
