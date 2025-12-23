package Capstone.capstoneProject.service;

import Capstone.capstoneProject.dto.Chats.ChatMessageDTO;
import Capstone.capstoneProject.dto.Chats.UsageShareRequest;
import Capstone.capstoneProject.entity.Chats.ChatMessages;
import Capstone.capstoneProject.entity.Chats.ChatRoomUsers;
import Capstone.capstoneProject.entity.UsageHistory;
import Capstone.capstoneProject.entity.Users.Users;
import Capstone.capstoneProject.enums.ChatRoomRole;
import Capstone.capstoneProject.enums.MessageType;
import Capstone.capstoneProject.exceptions.badRequest.UsageShareMessageRequiredException;
import Capstone.capstoneProject.exceptions.forbidden.ChatMessageAccessDeniedException;
import Capstone.capstoneProject.exceptions.forbidden.ChatRoomAccessDeniedException;
import Capstone.capstoneProject.exceptions.forbidden.UsageAccessDeniedException;
import Capstone.capstoneProject.exceptions.notfound.ChatRoomMessageNotFoundException;
import Capstone.capstoneProject.repository.ChatMessagesRepository;
import Capstone.capstoneProject.repository.ChatRoomUsersRepository;
import Capstone.capstoneProject.repository.UsageHistoryRepository;
import Capstone.capstoneProject.security.AuthenticatedUserUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ChallengeUsageShareService {
    private final AuthenticatedUserUtils authenticatedUserUtils;
    private final ChatRoomUsersRepository chatRoomUsersRepository;
    private final UsageHistoryRepository usageHistoryRepository;
    private final ChallengeMessageService challengeMessageService;
    private final ChatMessagesRepository chatMessagesRepository;
    private final SimpMessagingTemplate messagingTemplate;


    public void shareChallengeChatUsage(String roomId, UsageShareRequest request) {
        Users user = authenticatedUserUtils.getCurrentUser();

        chatRoomUsersRepository.findByChatRooms_RoomIdAndUsers(roomId, user)
                .orElseThrow(() -> new ChatRoomAccessDeniedException("해당 채팅방 유저가 아닙니다."));

        List<UsageHistory> usages = usageHistoryRepository.findAllByIdInAndUsers(request.getUsageIds(), user);

        if (usages.size() != request.getUsageIds().size()) {
            throw new UsageAccessDeniedException("본인 사용내역만 공유 가능합니다.");
        }

        for (UsageHistory usage : usages) {
            challengeMessageService.saveAndSendUsageShareMessage(roomId, usage, user);
        }
    }

    public void deleteChallengeChatUsage(Long messageId) {
        Users user = authenticatedUserUtils.getCurrentUser();


        ChatMessages chatMessages = chatMessagesRepository.findById(messageId)
                .orElseThrow(() -> new ChatRoomMessageNotFoundException("채팅방의 해당 메시지를 찾을 수 없습니다."));

        if (chatMessages.getMessageType() != MessageType.USAGE_SHARE) {
            throw new UsageShareMessageRequiredException("사용내역 메시지가 아닙니다.");
        }

        ChatRoomUsers chatRoomUsers = chatRoomUsersRepository.findByChatRooms_RoomIdAndUsers(chatMessages.getChatRooms().getRoomId(), user)
                .orElseThrow(() -> new ChatRoomAccessDeniedException("해당 채팅방의 유저가 아닙니다."));

        // 작성자랑 방장만 메시지 삭제 가능
        boolean isWriter = chatMessages.getUsers().getId().equals(user.getId());
        boolean isHost = chatRoomUsers.getChatRoomRole() == ChatRoomRole.HOST;

        if (!isWriter && !isHost) {
            throw new ChatMessageAccessDeniedException("채팅방의 해당 메시지에 관한 권한이 없습니다.");
        }
        chatMessages.setIsDeleted(true);
        chatMessagesRepository.save(chatMessages);

        ChatMessageDTO dto = ChatMessageDTO.deleted(chatMessages);

        // 삭제한거 바로 반영
        messagingTemplate.convertAndSend(
                "/sub/challenges/chat/room/" + chatMessages.getChatRooms().getRoomId(),
                dto
        );
    }


}
