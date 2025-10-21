package Capstone.capstoneProject.service;

import Capstone.capstoneProject.dto.ChallengeCreate;
import Capstone.capstoneProject.entity.Users;
import Capstone.capstoneProject.entity.challenges.ChallengeHashtag;
import Capstone.capstoneProject.entity.challenges.Challenges;
import Capstone.capstoneProject.entity.challenges.Hashtag;
import Capstone.capstoneProject.repository.ChallengeHashtagRepository;
import Capstone.capstoneProject.repository.ChallengeRepository;
import Capstone.capstoneProject.repository.HashtagRepository;
import Capstone.capstoneProject.repository.UserRepository;
import Capstone.capstoneProject.security.AuthenticatedUserUtils;
import Capstone.capstoneProject.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChallengeService {
    private final ChallengeRepository challengeRepository;
    private final AuthenticatedUserUtils authenticatedUserUtils;
    private final ChallengeHashtagRepository challengeHashtagRepository;
    private final HashtagRepository hashtagRepository;

    public Challenges create(ChallengeCreate dto) {
        // user 정보 가져오기 (baarer token에서 추출)
        Users user = authenticatedUserUtils.getCurrentUser();

        Challenges challenge = Challenges.builder()
                .createdBy(user)
                .title(dto.getTitle())
                .maxPersonnel(dto.getMaxPersonnel())
                .job(dto.getJob())
                .goal(dto.getGoal())
                .image(dto.getImage())
                .build();

        challengeRepository.save(challenge);

        // 해시태그 넣어주기
        if (dto.getHashtags() != null && !dto.getHashtags().isEmpty()) {
            List<Hashtag> hashtags = hashtagRepository.findByNameIn(dto.getHashtags());
            List<ChallengeHashtag> challengeHashtags = hashtags.stream()
                    .map(tag -> ChallengeHashtag.of(challenge, tag))
                    .toList();
            challengeHashtagRepository.saveAll(challengeHashtags);
        }

        return challenge;
    }
}
