package Capstone.capstoneProject.service;

import Capstone.capstoneProject.dto.ChatBot.ChatBotSummaryRequest;
import Capstone.capstoneProject.dto.ChatBot.ChatBotMessageResponse;
import Capstone.capstoneProject.dto.ChatBot.ChatRoomAnalysisRequest;
import Capstone.capstoneProject.dto.ChatBot.ChatRoomAnalysisResponse;
import Capstone.capstoneProject.dto.Usages.UsageSummaryDTO;
import Capstone.capstoneProject.entity.ChatBot.ChatBotMessages;
import Capstone.capstoneProject.entity.ChatBot.ChatBotRooms;
import Capstone.capstoneProject.entity.ChatBot.HomeChatBotMessages;
import Capstone.capstoneProject.entity.UsageHistory;
import Capstone.capstoneProject.entity.Users;
import Capstone.capstoneProject.exceptions.badGeteway.ChatBotHomeMessageFailedException;
import Capstone.capstoneProject.exceptions.notfound.ChatBotRoomNotFoundException;
import Capstone.capstoneProject.repository.ChatBotMessageRepository;
import Capstone.capstoneProject.repository.ChatBotRoomRepository;
import Capstone.capstoneProject.repository.HomeChatBotMessageRepository;
import Capstone.capstoneProject.repository.UsageHistoryRepository;
import Capstone.capstoneProject.security.AuthenticatedUserUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional
public class ChatBotService {

    private final AuthenticatedUserUtils authenticatedUserUtils;
    private final RestTemplate restTemplate;
    private final UsageHistoryRepository usageHistoryRepository;
    private final ChatBotMessageRepository chatBotMessageRepository;
    private final ChatBotRoomRepository chatBotRoomRepository;
    private final HomeChatBotMessageRepository homeChatBotMessageRepository;
    private final UsageService usageService;

    public ChatBotMessageResponse createChatSummary() {
        Users user = authenticatedUserUtils.getCurrentUser();
        List<UsageHistory> usageHistory = usageHistoryRepository.findTop2ByUsersOrderByProDateDesc(user);

        // 최근 챗봇 대화 기록
        List<HomeChatBotMessages> recentMessages = homeChatBotMessageRepository.findTop2ByUserOrderByCreatedAtDesc(user);
        
        ChatBotSummaryRequest request = ChatBotSummaryRequest.from(recentMessages, usageHistory);

        String url = "http://13.125.64.51:8080/summary";
        // 요청 그대로 전달
        ResponseEntity<ChatBotMessageResponse> response =
                restTemplate.postForEntity(
                        url,
                        request,
                        ChatBotMessageResponse.class
                );

        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            throw new ChatBotHomeMessageFailedException("홈 챗봇 메시지 생성에 실패했습니다.");
        }

        return response.getBody();
    }

    public ChatRoomAnalysisResponse createChatRoomAnalysis() {
        Users user = authenticatedUserUtils.getCurrentUser();

        ChatBotRooms chatBotRooms = chatBotRoomRepository.findByUser(user)
                .orElseThrow(() -> new ChatBotRoomNotFoundException("해당 챗봇 채팅방을 찾을 수 없습니다."));

        List<ChatBotMessages> chatBotMessages = chatBotMessageRepository.findByChatBotRooms(chatBotRooms);

        LocalDate startOfMonth = LocalDate.now().withDayOfMonth(1);
        LocalDate endOfMonth = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth());

        List<UsageHistory> currentMonthHistories = usageHistoryRepository.findAllByUsersForCurrentMonth(user, startOfMonth, endOfMonth);

        UsageSummaryDTO usageSummaryDTO = usageService.createUsageSummary(currentMonthHistories);


        ChatRoomAnalysisRequest request = ChatRoomAnalysisRequest.from("이번달 소비 분석 해줘", chatBotMessages, usageSummaryDTO, currentMonthHistories);

        String url = "http://13.125.64.51:8080/chat_analysis";

        // 요청 그대로 전달
        ResponseEntity<ChatRoomAnalysisResponse> response =
                restTemplate.postForEntity(
                        url,
                        request,
                        ChatRoomAnalysisResponse.class
                );

        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            throw new ChatBotHomeMessageFailedException("홈 챗봇 메시지 생성에 실패했습니다.");
        }

        return response.getBody();

    }



}
