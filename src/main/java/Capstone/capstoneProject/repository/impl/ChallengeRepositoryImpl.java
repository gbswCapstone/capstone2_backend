package Capstone.capstoneProject.repository.impl;

import Capstone.capstoneProject.entity.challenges.*;
import Capstone.capstoneProject.enums.SortType;
import Capstone.capstoneProject.enums.UserJobs;
import Capstone.capstoneProject.repository.ChallengeRepositoryCustom;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ChallengeRepositoryImpl implements ChallengeRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final QChallenges challenges = QChallenges.challenges;
    private final QChallengeHashtag challengeHashtag = QChallengeHashtag.challengeHashtag;
    private final QHashtag hashtagEntity = QHashtag.hashtag;

    @Override
    public List<Challenges> searchDynamic(
            String hashtag,
            String keyword,
            SortType sortType,
            UserJobs userJobs
    ) {

        BooleanExpression condition = buildConditions(keyword, userJobs);

        JPQLQuery<Challenges> query = queryFactory
                .selectFrom(challenges)
                .where(condition);

        // 해시태그 포함
        if (hashtag != null && !hashtag.isBlank()) {
            query.join(challenges.challengeHashtags, challengeHashtag)
                    .join(challengeHashtag.hashtag, hashtagEntity)
                    .where(hashtagEntity.name.containsIgnoreCase(hashtag))
                    .distinct();
        }


        OrderSpecifier<?>[] orderSpecifiers = getOrderSpecifiers(sortType);

        return query.orderBy(orderSpecifiers).fetch();
    }

    private BooleanExpression buildConditions(String keyword, UserJobs userJobs) {

        BooleanExpression condition = challenges.deletedAt.isNull();

        if (keyword != null && !keyword.isBlank()) {
            condition = condition.and(challenges.title.containsIgnoreCase(keyword));
        }

        if (userJobs != null) {
            condition = condition.and(challenges.job.eq(userJobs));
        }

        return condition;
    }

    // 복수 정렬
    private OrderSpecifier<?>[] getOrderSpecifiers(SortType sortType) {

        if (sortType == null) {
            return new OrderSpecifier<?>[]{
                    challenges.createdAt.desc()
            };
        }

        return switch (sortType) {
            case POPULAR -> new OrderSpecifier<?>[]{
                    challenges.likeCount.desc().nullsLast(),
                    challenges.createdAt.desc()
            };
            case OLDEST -> new OrderSpecifier<?>[]{
                    challenges.createdAt.asc()
            };
            default -> new OrderSpecifier<?>[]{
                    challenges.createdAt.desc()
            };
        };
    }
}