package Capstone.capstoneProject.service;

import Capstone.capstoneProject.dto.ChatBot.*;
import Capstone.capstoneProject.dto.Chats.MessageSendRequest;
import Capstone.capstoneProject.dto.Usages.UsageSummaryDTO;
import Capstone.capstoneProject.entity.ChatBot.ChatBotMessages;
import Capstone.capstoneProject.entity.ChatBot.ChatBotRooms;
import Capstone.capstoneProject.entity.ChatBot.HomeChatBotMessages;
import Capstone.capstoneProject.entity.UsageHistory;
import Capstone.capstoneProject.entity.Users.Users;
import Capstone.capstoneProject.enums.ChatBotSenderType;
import Capstone.capstoneProject.exceptions.badGeteway.ChatBotMessageFailedException;
import Capstone.capstoneProject.exceptions.notfound.ChatBotRoomNotFoundException;
import Capstone.capstoneProject.exceptions.notfound.UserNotFoundException;
import Capstone.capstoneProject.exceptions.unauthorized.NotAuthenticatedException;
import Capstone.capstoneProject.repository.*;
import Capstone.capstoneProject.security.AuthenticatedUserUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.security.Principal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.UUID;


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
    private final SimpMessagingTemplate messagingTemplate;
    private final UserRepository userRepository;


    // 챗봇 채팅방 생성
    public ChatBotRooms createRoom(Users user) {
        // 있으면 반환, 없으면 생성
        return chatBotRoomRepository.findByUser(user)
                .orElseGet(() ->
                        chatBotRoomRepository.save(
                                ChatBotRooms.builder()
                                        .chatBotRoomId(UUID.randomUUID().toString()) // 랜덤 UUID
                                        .user(user)
                                        .build()
                        )
                );
    }

    private Principal resolvePrincipal(SimpMessageHeaderAccessor accessor) {
        Principal principal = accessor.getUser();
        if (principal != null) {
            return principal;
        }

        Object saved = accessor.getSessionAttributes().get("userPrincipal");
        if (saved instanceof Principal p) {
            return p;
        }
        throw new NotAuthenticatedException("WebSocket 인증 정보가 없습니다.");
    }

    public void sendMessage(MessageSendRequest request, SimpMessageHeaderAccessor accessor) {
        Principal principal = resolvePrincipal(accessor);
        String username = principal.getName();
        Users user = userRepository.findByEmailAndDeletedAtIsNull(username)
                .orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다."));

        ChatBotRooms room = chatBotRoomRepository.findByChatBotRoomId(request.getChatBotRoomId())
                .orElseThrow(() -> new ChatBotRoomNotFoundException("챗봇 채팅방이 없습니다."));

        ChatBotMessages chatBotMessages = ChatBotMessages.builder()
                .chatBotRooms(room)
                .senderType(ChatBotSenderType.USER)
                .message(request.getMessage())
                .build();
        chatBotMessageRepository.save(chatBotMessages);

        // 유저 메시지 전송
        messagingTemplate.convertAndSend(
                "/sub/chat/bot/" + request.getChatBotRoomId(),
                ChatBotMessageDTO.from(chatBotMessages)
        );

        // AI 서버 호출
        sendChatBotMessage(user, request.getMessage(), room);
    }


    public void sendChatBotMessage(Users user, String userMessage, ChatBotRooms chatBotRooms) {

        List<ChatBotMessages> chatBotMessages = chatBotMessageRepository.findTop2ByChatBotRoomsOrderByCreatedAtDesc(chatBotRooms);
        LocalDate startOfMonth = LocalDate.now().withDayOfMonth(1);
        LocalDate endOfMonth = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth());

        List<UsageHistory> currentMonthHistories = usageHistoryRepository.findAllByUsersForCurrentMonth(user, startOfMonth, endOfMonth);
        ChatBotRoomMessageRequest request = ChatBotRoomMessageRequest.from(userMessage, chatBotMessages, currentMonthHistories);

        String url = "http://13.125.64.51:8080/chat";
        // 요청 그대로 전달
        ResponseEntity<ChatBotMessageResponse> response =
                restTemplate.postForEntity(
                        url, request, ChatBotMessageResponse.class
                );
        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            throw new ChatBotMessageFailedException("챗봇 메시지 생성에 실패했습니다.");
        }
        // ai가 생성한 메시지 저장
        ChatBotMessages saveChatBotMessages =
                ChatBotMessages.builder()
                        .chatBotRooms(chatBotRooms)
                        .senderType(ChatBotSenderType.AI)
                        .message(response.getBody().getMessage())
                        .build();
        chatBotMessageRepository.save(saveChatBotMessages);
        // ai 메시지 전송
        messagingTemplate.convertAndSend(
                "/sub/chat/bot/" + chatBotRooms.getChatBotRoomId(),
                ChatBotMessageDTO.from(saveChatBotMessages));
    }

    // 챗봇 채팅방 메시지 히스토리 조회
    public List<ChatBotMessageDTO> getChatBotMessages(int page, int size) {

        Users user = authenticatedUserUtils.getCurrentUser();

        ChatBotRooms chatBotRooms = chatBotRoomRepository.findByUser(user)
                .orElseThrow(() -> new ChatBotRoomNotFoundException("해당 챗봇 채팅방이 없습니다."));

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(Sort.Direction.DESC, "createdAt") // 최신순
        );

        Page<ChatBotMessages> messages =
                chatBotMessageRepository.findByChatBotRooms(chatBotRooms, pageable);

        return messages.getContent()
                .stream()
                .map(ChatBotMessageDTO::from)
                .toList();
    }

    public ChatBotMessageResponse createChatRoomAnalysisMonth() {
        Users user = authenticatedUserUtils.getCurrentUser();

        ChatBotRooms chatBotRooms = chatBotRoomRepository.findByUser(user)
                .orElseThrow(() -> new ChatBotRoomNotFoundException("해당 챗봇 채팅방을 찾을 수 없습니다."));
        // 사용자 질문 메시지 생성 및 저장
        ChatBotMessages userMassageDTO = ChatBotMessages.builder()
                .chatBotRooms(chatBotRooms)
                .senderType(ChatBotSenderType.USER)
                .message("이번달 소비 분석 해줘")
                .build();
        chatBotMessageRepository.save(userMassageDTO);

        // 유저 메시지 전송
        messagingTemplate.convertAndSend(
                "/sub/chat/bot/" + chatBotRooms.getChatBotRoomId(),
                ChatBotMessageDTO.from(userMassageDTO)
        );

        List<ChatBotMessages> chatBotMessages = chatBotMessageRepository.findByChatBotRooms(chatBotRooms);

        LocalDate startOfMonth = LocalDate.now().withDayOfMonth(1);
        LocalDate endOfMonth = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth());

        List<UsageHistory> currentMonthHistories = usageHistoryRepository.findAllByUsersForCurrentMonth(user, startOfMonth, endOfMonth);

        UsageSummaryDTO usageSummaryDTO = usageService.createUsageSummary(currentMonthHistories);

        ChatRoomAnalysisRequest request = ChatRoomAnalysisRequest.from("이번달 소비 분석 해줘", chatBotMessages, usageSummaryDTO, currentMonthHistories);

        String url = "http://13.125.64.51:8080/chat_analysis";

        // 요청 그대로 전달
        ResponseEntity<ChatBotMessageResponse> response =
                restTemplate.postForEntity(
                        url,
                        request,
                        ChatBotMessageResponse.class
                );

        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            throw new ChatBotMessageFailedException("챗봇 메시지 생성에 실패했습니다.");
        }

        // ai가 생성한 메시지 저장
        ChatBotMessages saveChatBotMessages =
                ChatBotMessages.builder()
                        .chatBotRooms(chatBotRooms)
                        .senderType(ChatBotSenderType.ANALYSIS)
                        .message(response.getBody().getMessage())
                        .build();
        chatBotMessageRepository.save(saveChatBotMessages);

        // ai 메시지 전송
        messagingTemplate.convertAndSend(
                "/sub/chat/bot/" + chatBotRooms.getChatBotRoomId(),
                ChatBotMessageDTO.from(saveChatBotMessages)
        );
        return response.getBody();
    }


    public ChatBotMessageResponse createChatRoomAnalysisWeek() {
        Users user = authenticatedUserUtils.getCurrentUser();

        ChatBotRooms chatBotRooms = chatBotRoomRepository.findByUser(user)
                .orElseThrow(() -> new ChatBotRoomNotFoundException("해당 챗봇 채팅방을 찾을 수 없습니다."));
        // 사용자 질문 메시지 생성 및 저장
        ChatBotMessages userMassageDTO = ChatBotMessages.builder()
                .chatBotRooms(chatBotRooms)
                .senderType(ChatBotSenderType.USER)
                .message("이번주 소비 분석 해줘")
                .build();
        chatBotMessageRepository.save(userMassageDTO);

        // 유저 메시지 전송
        messagingTemplate.convertAndSend(
                "/sub/chat/bot/" + chatBotRooms.getChatBotRoomId(),
                ChatBotMessageDTO.from(userMassageDTO)
        );

        List<ChatBotMessages> chatBotMessages = chatBotMessageRepository.findByChatBotRooms(chatBotRooms);

        // 현재 날짜 기준
        LocalDate now = LocalDate.now();
        // 이번 주 월요일
        LocalDate startOfWeek = now.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        // 이번 주 일요일
        LocalDate endOfWeek = now.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
        List<UsageHistory> currentWeekHistories = usageHistoryRepository.findAllByUsersForCurrentWeek(user, startOfWeek, endOfWeek);
        UsageSummaryDTO usageSummaryDTO = usageService.createUsageSummary(currentWeekHistories);

        ChatRoomAnalysisRequest request = ChatRoomAnalysisRequest.from("이번주 소비 분석 해줘", chatBotMessages, usageSummaryDTO, currentWeekHistories);

        String url = "http://13.125.64.51:8080/chat_analysis";

        // 요청 그대로 전달
        ResponseEntity<ChatBotMessageResponse> response =
                restTemplate.postForEntity(
                        url,
                        request,
                        ChatBotMessageResponse.class
                );

        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            throw new ChatBotMessageFailedException("챗봇 메시지 생성에 실패했습니다.");
        }

        // ai가 생성한 메시지 저장
        ChatBotMessages saveChatBotMessages =
                ChatBotMessages.builder()
                        .chatBotRooms(chatBotRooms)
                        .senderType(ChatBotSenderType.ANALYSIS)
                        .message(response.getBody().getMessage())
                        .build();
        chatBotMessageRepository.save(saveChatBotMessages);

        // ai 메시지 전송
        messagingTemplate.convertAndSend(
                "/sub/chat/bot/" + chatBotRooms.getChatBotRoomId(),
                ChatBotMessageDTO.from(saveChatBotMessages)
        );
        return response.getBody();
    }




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
            throw new ChatBotMessageFailedException("챗봇 메시지 생성에 실패했습니다.");
        }

        // ai가 생성한 메시지 저장
        HomeChatBotMessages homeChatBotMessages =
                HomeChatBotMessages.builder()
                        .user(user)
                        .message(response.getBody().getMessage())
                        .build();

        homeChatBotMessageRepository.save(homeChatBotMessages);

        return response.getBody();
    }




}
