package Capstone.capstoneProject.service;

import Capstone.capstoneProject.dto.ChallengeCreate;
import Capstone.capstoneProject.dto.ChallengeListDTO;
import Capstone.capstoneProject.entity.Users;
import Capstone.capstoneProject.entity.challenges.*;
import Capstone.capstoneProject.enums.UserJobs;
import Capstone.capstoneProject.exceptions.AlreadyJoinedException;
import Capstone.capstoneProject.exceptions.ChallengeFullException;
import Capstone.capstoneProject.exceptions.ChallengeNotFoundException;
import Capstone.capstoneProject.repository.*;
import Capstone.capstoneProject.security.AuthenticatedUserUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ChallengeService {
    private final ChallengeRepository challengeRepository;
    private final AuthenticatedUserUtils authenticatedUserUtils;
    private final ChallengeHashtagRepository challengeHashtagRepository;
    private final HashtagRepository hashtagRepository;
    private final ChallengeUsersRepository challengeUsersRepository;
    private final LikeRepository likeRepository;
    private final UserRepository userRepository;


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

        // 작성자를 자동 참여자로 추가
        ChallengeUsers challengeUser = ChallengeUsers.builder()
                .challenge(challenge)
                .user(user)
                .joinedAt(LocalDateTime.now())
                .build();
        challengeUsersRepository.save(challengeUser);

        // 해시태그 넣어주기
        if (dto.getHashtags() != null && !dto.getHashtags().isEmpty()) {
            List<Hashtag> hashtags = new ArrayList<>();

            for (String name : dto.getHashtags()) {
                Hashtag tag = hashtagRepository.findByName(name)
                        .orElseGet(() -> {
                            Hashtag newTag = new Hashtag();
                            newTag.setName(name);
                            return hashtagRepository.save(newTag);
                        });
                hashtags.add(tag);
            }

            List<ChallengeHashtag> challengeHashtags = hashtags.stream()
                    .map(tag -> ChallengeHashtag.of(challenge, tag))
                    .toList();
            challengeHashtagRepository.saveAll(challengeHashtags);
        }

        return challenge;
    }

    public List<ChallengeListDTO> getChallengeList(String sortType, String job) {

        UserJobs finalJob;
        if (job == null || job.isEmpty() || job.equalsIgnoreCase("NONE")) {
            finalJob = UserJobs.NONE;
        } else {
            try {
                finalJob = UserJobs.valueOf(job.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("유효하지 않은 직업입니다. job: " + job);
            }
        }

        String finalSort = (sortType == null || sortType.isEmpty()) ? "RECENT" : sortType.toUpperCase();

        List<ChallengeListDTO> challenges;
        // job 없을 때
        if (finalJob == UserJobs.NONE) {
            challenges = finalSort.equals("POPULAR")
                    ? challengeRepository.findAllActiveOrderByLikeCountDescWithCurrentPersonnel()
                    : challengeRepository.findAllActiveOrderByCreatedAtDescWithCurrentPersonnel();
        } else {  // job 있을 때
            challenges = finalSort.equals("POPULAR")
                    ? challengeRepository.findAllActiveByJobOrderByLikeCountDescWithCurrentPersonnel(finalJob)
                    : challengeRepository.findAllActiveByJobOrderByCreatedAtDescWithCurrentPersonnel(finalJob);
        }

        return challenges;
    }

    @Transactional
    public int toggleLike(Long id) {
        // user 정보 가져오기 (baarer token에서 추출)
        Users user = authenticatedUserUtils.getCurrentUser();
        Challenges challenges = challengeRepository.findById(id)
                .orElseThrow(() -> new ChallengeNotFoundException("해당 챌린지를 찾을 수 없습니다."));
        Optional<Likes> existing = likeRepository.findByUserAndChallenges(user, challenges);
        if (existing.isPresent()) {
            likeRepository.delete(existing.get()); // 좋아요 취소

            challenges.setLikeCount(challenges.getLikeCount() - 1);
            challengeRepository.save(challenges);
        } else {
            Likes like = Likes.builder().user(user).challenges(challenges).build();
            likeRepository.save(like); // 좋아요 추가

            challenges.setLikeCount(challenges.getLikeCount() + 1);
            challengeRepository.save(challenges);
        }
        return likeRepository.countByChallenges(challenges);
    }

    public Challenges findById(Long id) {
        Challenges challenges = challengeRepository.findById(id)
                .filter(c -> c.getDeletedAt() == null)
                .orElseThrow(() -> new ChallengeNotFoundException("해당 챌린지방을 찾을 수 없습니다."));
        return challenges;
    }

    public boolean isLikedByUser(Challenges challenges) {
        // user 정보 가져오기 (baarer token에서 추출)
        Users user = authenticatedUserUtils.getCurrentUser();
        return likeRepository.findByUserAndChallenges(userRepository.findById(user.getId())
                .orElseThrow(), challenges).isPresent();
    }

    @Transactional
    public Challenges update(Long id, ChallengeCreate dto) {
        Challenges challenges = findById(id);
        // user 정보 가져오기 (bearer token에서 추출)
        Users user = authenticatedUserUtils.getCurrentUser();

        // 수정하는 사람이 작성자인지 확인
        if (!challenges.getCreatedBy().getId().equals(user.getId())) {
            throw new IllegalArgumentException("작성자가 아니면 수정할 수 없습니다.");
        }

        challenges.setTitle(dto.getTitle());
        challenges.setImage(dto.getImage());
        challenges.setGoal(dto.getGoal());
        challenges.setJob(dto.getJob());
        challenges.setMaxPersonnel(dto.getMaxPersonnel());

        // 해시태그 수정
        if (dto.getHashtags() != null) {
            // 기존 연결된 해시태그 가져오기
            List<ChallengeHashtag> existingRelations =
                    challengeHashtagRepository.findByChallenge(challenges);

            // 기존 해시태그 이름 목록
            Set<String> existingNames = existingRelations.stream()
                    .map(ch -> ch.getHashtag().getName())
                    .collect(Collectors.toSet());

            // 새로 들어온 해시태그 이름 목록
            Set<String> newNames = new HashSet<>(dto.getHashtags());

            // 삭제해야 할 해시태그 관계 (기존에 있었지만 새 목록에 없는 것)
            List<ChallengeHashtag> relationsToDelete = existingRelations.stream()
                    .filter(ch -> !newNames.contains(ch.getHashtag().getName()))
                    .toList();

            if (!relationsToDelete.isEmpty()) {
                challengeHashtagRepository.deleteAll(relationsToDelete);
            }

            // 추가해야 할 해시태그 이름 목록 (새 목록에는 있지만 기존에 없었던 것)
            List<String> hashtagNamesToAdd = newNames.stream()
                    .filter(name -> !existingNames.contains(name))
                    .toList();

            List<Hashtag> hashtagsToAssociate = hashtagNamesToAdd.stream()
                    .map(name -> hashtagRepository.findByName(name)
                            .orElseGet(() -> {
                                Hashtag newTag = new Hashtag();
                                newTag.setName(name);
                                // 신규 해시태그를 먼저 db에 저장
                                return hashtagRepository.save(newTag);
                            })
                    )
                    .toList();

            List<ChallengeHashtag> newRelations = hashtagsToAssociate.stream()
                    .map(tag -> ChallengeHashtag.of(challenges, tag))
                    .toList();

            // 새로운 해시태그 저장
            if (!newRelations.isEmpty()) {
                challengeHashtagRepository.saveAll(newRelations);
            }
        }
        return challenges;
    }

    public void delete(Long id) {
        Challenges challenges = findById(id);
        // user 정보 가져오기 (bearer token에서 추출)
        Users user = authenticatedUserUtils.getCurrentUser();
        // 작성자인지 확인
        if (!challenges.getCreatedBy().getId().equals(user.getId())) {
            throw new IllegalArgumentException("작성자가 아니면 삭제할 수 없습니다.");
        }
        challenges.setDeletedAt(LocalDateTime.now());
        challengeRepository.save(challenges);
    }

    public List<Challenges> searchChallenge(String hashtag, String keyword) {
        boolean isHashtagEmpty = (hashtag == null || hashtag.isBlank());
        boolean isKeywordEmpty = (keyword == null || keyword.isBlank());

        if (!isHashtagEmpty && !isKeywordEmpty) {
            // 해시태그+제목 검색
            return challengeRepository.searchByHashtagAndKeyword(hashtag, keyword);
        } else if (!isHashtagEmpty) {
            // 해시태그만 검색
            return challengeRepository.findByHashtagNameContaining(hashtag);
        } else if (!isKeywordEmpty) {
            // 제목만 검색
            return challengeRepository.findByTitleContaining(keyword);
        } else {
            throw new IllegalArgumentException("잘못된 검색입니다.(제목, 해시태그 입력없음)");
        }
    }

    public void joinChallenge(Long id) {
        Challenges challenge = challengeRepository.findById(id)
                .orElseThrow(() -> new ChallengeNotFoundException("챌린지를 찾을 수 없습니다."));

        // user 정보 가져오기 (baarer token에서 추출)
        Users user = authenticatedUserUtils.getCurrentUser();

        // 참여했는지 확인
        boolean isAlreadyJoined = challengeUsersRepository.existsByChallengeIdAndUserId(id, user.getId());
        if (isAlreadyJoined) {
            throw new AlreadyJoinedException("이미 참여한 챌린지입니다.");
        }
        int currentParticipants = challengeUsersRepository.countByChallengeId(id);
        if (currentParticipants >= challenge.getMaxPersonnel()) {
            throw new ChallengeFullException("참여 인원이 가득 찼습니다.");
        }

        ChallengeUsers join = ChallengeUsers.builder()
                .challenge(challenge)
                .user(user)
                .joinedAt(LocalDateTime.now())
                .build();

        challengeUsersRepository.save(join);
    }
}






