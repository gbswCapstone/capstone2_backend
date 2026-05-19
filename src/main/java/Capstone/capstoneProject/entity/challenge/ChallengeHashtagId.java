package Capstone.capstoneProject.entity.challenge;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

// ChallengeHashtag 엔티티의 복합 PK용 클래스
@Embeddable
public class ChallengeHashtagId implements Serializable {

    private Long challengeId;
    private Long hashtagId;

    public ChallengeHashtagId() {}

    public ChallengeHashtagId(Long challengeId, Long hashtagId) {
        this.challengeId = challengeId;
        this.hashtagId = hashtagId;
    }

    // equals()와 hashCode() 반드시 구현
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChallengeHashtagId)) return false;
        ChallengeHashtagId that = (ChallengeHashtagId) o;
        return Objects.equals(challengeId, that.challengeId) &&
                Objects.equals(hashtagId, that.hashtagId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(challengeId, hashtagId);
    }

    // Getter / Setter
    public Long getChallengeId() { return challengeId; }
    public void setChallengeId(Long challengeId) { this.challengeId = challengeId; }
    public Long getHashtagId() { return hashtagId; }
    public void setHashtagId(Long hashtagId) { this.hashtagId = hashtagId; }
}
