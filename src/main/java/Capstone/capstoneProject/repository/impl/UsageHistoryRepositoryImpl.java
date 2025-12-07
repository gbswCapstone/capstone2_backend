package Capstone.capstoneProject.repository.impl;

import Capstone.capstoneProject.entity.UsageHistory;
import Capstone.capstoneProject.enums.HistoryType;
import Capstone.capstoneProject.enums.UsageSortType;
import Capstone.capstoneProject.repository.UsageHistoryCustom;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import static Capstone.capstoneProject.entity.QUsageHistory.usageHistory;

@Repository
@RequiredArgsConstructor
public class UsageHistoryRepositoryImpl implements UsageHistoryCustom {

    private final JPAQueryFactory queryFactory; // 생성자를 통해 주입

    @Override
    public List<UsageHistory> searchDynamic(Long userId, HistoryType type, LocalDate startDate, LocalDate endDate, UsageSortType sortType) {

        JPAQuery<UsageHistory> query = queryFactory
                .selectFrom(usageHistory)
                .where(
                        usageHistory.users.id.eq(userId),
                        eqHistoryType(type),
                        betweenDates(startDate, endDate)
                );
        query.orderBy(createOrderSpecifiers(sortType));
        return query.fetch();
    }

    private BooleanExpression eqHistoryType(HistoryType type) {
        return type != null ? usageHistory.historyType.eq(type) : null;
    }

    private BooleanExpression betweenDates(LocalDate startDate, LocalDate endDate) {
        if (startDate != null && endDate != null) {
            return usageHistory.proDate.between(startDate, endDate);
        }
        return null;
    }

    private OrderSpecifier<?>[] createOrderSpecifiers(UsageSortType sortType) {
        List<OrderSpecifier<?>> orderSpecifiers = new ArrayList<>();

        switch (sortType) {
            case OLDEST:
                orderSpecifiers.add(usageHistory.proDate.asc());
                break;
            case LOW_PRICE:
                orderSpecifiers.add(usageHistory.price.asc());
                break;
            case HIGH_PRICE:
                orderSpecifiers.add(usageHistory.price.desc());
                break;
            default:
                orderSpecifiers.add(usageHistory.proDate.desc());
                break;
        }

//        orderSpecifiers.add(usageHistory.id.desc());

        return orderSpecifiers.toArray(new OrderSpecifier[0]);
    }
}
