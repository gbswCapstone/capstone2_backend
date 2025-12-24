package Capstone.capstoneProject.repository.impl;


import Capstone.capstoneProject.entity.Missions.QMissions;
import Capstone.capstoneProject.entity.Missions.QUserMissions;
import Capstone.capstoneProject.entity.Missions.UserMissions;
import Capstone.capstoneProject.entity.Users.Users;
import Capstone.capstoneProject.enums.DateSortType;
import Capstone.capstoneProject.enums.MissionCategory;
import Capstone.capstoneProject.enums.MissionType;
import Capstone.capstoneProject.repository.UserMissionRepositoryCustom;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserMissionRepositoryImpl implements UserMissionRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<UserMissions> findAllByUsersWithMissions(Users user, MissionCategory category, DateSortType sortType) {
        QUserMissions userMissions = QUserMissions.userMissions;
        QMissions missions = QMissions.missions;

        return queryFactory
                .selectFrom(userMissions)
                .join(userMissions.missions, missions).fetchJoin()
                .where(
                        userMissions.users.eq(user),
                        categoryEq(category) // 카테고리 필터링
                )
                .orderBy(dateSort(sortType)) // 정렬 조건
                .fetch();
    }

    // 카테고리 동적 조건
    private BooleanExpression categoryEq(MissionCategory category) {
        if (category == null || category == MissionCategory.TOTAL) {
            return null;
        }

        QUserMissions userMissions = QUserMissions.userMissions;
        QMissions missions = QUserMissions.userMissions.missions;

        if (category == MissionCategory.CHALLENGE) {
            return missions.challenges.isNotNull();
        }

        if (category == MissionCategory.BASIC) {
            return missions.missionType.in(
                    MissionType.MONTHLY_OUTLAY_GOAL,
                    MissionType.ATTENDANCE_CHECK
            );
        }

        if (category == MissionCategory.PERSONAL) {
            return missions.challenges.isNull()
                    .and(missions.missionType.notIn(
                            MissionType.MONTHLY_OUTLAY_GOAL,
                            MissionType.ATTENDANCE_CHECK
                    ))
                    .and(missions.category.isNotNull());
        }
        return null;
    }

    // 정렬 동적 조건
    private OrderSpecifier<?> dateSort(DateSortType sortType) {
        if (sortType == DateSortType.OLDEST) {
            return QUserMissions.userMissions.createdAt.asc();
        }
        // 기본값은 최신순(RECENT)
        return QUserMissions.userMissions.createdAt.desc();
    }
}

