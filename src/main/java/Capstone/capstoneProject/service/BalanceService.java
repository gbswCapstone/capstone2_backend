package Capstone.capstoneProject.service;

import Capstone.capstoneProject.dto.BalanceDTO;
import Capstone.capstoneProject.entity.UsageHistory;
import Capstone.capstoneProject.entity.Users.UserAccounts;
import Capstone.capstoneProject.entity.Users.Users;
import Capstone.capstoneProject.enums.HistoryType;
import Capstone.capstoneProject.exceptions.conflict.AlreadyJoinedException;
import Capstone.capstoneProject.exceptions.conflict.BalanceAlreadyExistsException;
import Capstone.capstoneProject.exceptions.notfound.BalanceNotFoundException;
import Capstone.capstoneProject.repository.UserAccountRepository;
import Capstone.capstoneProject.security.AuthenticatedUserUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.YearMonth;

@Service
@RequiredArgsConstructor
public class BalanceService {
    private final AuthenticatedUserUtils authenticatedUserUtils;
    private final UserAccountRepository userAccountRepository;

    public BalanceDTO createBalance(BalanceDTO request) {
        Users user = authenticatedUserUtils.getCurrentUser();

        boolean isBalanced = userAccountRepository.existsByUser(user);

        if (isBalanced) {
            throw new BalanceAlreadyExistsException("잔액 정보가 이미 존재합니다.");
        }

        UserAccounts userAccounts = UserAccounts.builder()
                .user(user)
                .balance(request.getBalance())
                .thisIncome(BigDecimal.valueOf(0))
                .thisOutlay(BigDecimal.valueOf(0))
                .thisMonth(String.valueOf(YearMonth.now()))
                .build();
        userAccountRepository.save(userAccounts);
        return new BalanceDTO(request.getBalance());
    }

    public BalanceDTO patchBalance(BalanceDTO request) {
        Users user = authenticatedUserUtils.getCurrentUser();

        UserAccounts userAccounts = userAccountRepository.findByUser(user)
                .orElseThrow(() -> new BalanceNotFoundException("잔액을 찾을 수 없습니다."));

        userAccounts.setBalance(request.getBalance());
        userAccountRepository.save(userAccounts);
        return new BalanceDTO(request.getBalance());
    }

    public BalanceDTO getBalance() {
        Users user = authenticatedUserUtils.getCurrentUser();
        UserAccounts userAccounts = userAccountRepository.findByUser(user)
                .orElseThrow(() -> new BalanceNotFoundException("잔액을 찾을 수 없습니다."));
        return new BalanceDTO(userAccounts.getBalance());
    }

    // 잔액 갱신
    public void applyUsage(Users user, UsageHistory usageHistory) {

        UserAccounts account = userAccountRepository
                .findByUser(user)
                .orElseGet(() -> UserAccounts.builder()
                        .user(user)
                        .balance(BigDecimal.ZERO)
                        .thisIncome(BigDecimal.valueOf(0))
                        .thisOutlay(BigDecimal.valueOf(0))
                        .thisMonth(String.valueOf(YearMonth.now()))
                        .build()
                );

        BigDecimal amount = usageHistory.getPrice();

        if (usageHistory.getHistoryType() == HistoryType.INCOME) {
            account.setBalance(account.getBalance().add(amount));
        } else {
            account.setBalance(account.getBalance().subtract(amount));
        }

        userAccountRepository.save(account);
    }
}
