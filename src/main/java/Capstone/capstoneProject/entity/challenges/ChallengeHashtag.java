package Capstone.capstoneProject.entity.challenges;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name="challenge_hashtag")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class ChallengeHashtag {

    @EmbeddedId
    private ChallengeHashtagId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("challengeId") // Embeddable 컬럼 매핑
    @JoinColumn(name = "challenge_id")
    private Challenges challenge;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("hashtagId")
    @JoinColumn(name = "hashtag_id")
    private Hashtag hashtag;

    // 생성자 편의 메서드
    public static ChallengeHashtag of(Challenges challenge, Hashtag hashtag) {
        ChallengeHashtagId id = new ChallengeHashtagId(challenge.getId(), hashtag.getId());
        return ChallengeHashtag.builder()
                .id(id)
                .challenge(challenge)
                .hashtag(hashtag)
                .build();
    }
}
