package Capstone.capstoneProject.service;

import Capstone.capstoneProject.dto.BalanceDTO;
import Capstone.capstoneProject.entity.Users.UserAccounts;
import Capstone.capstoneProject.entity.Users.Users;
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
}
