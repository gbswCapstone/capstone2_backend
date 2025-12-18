package Capstone.capstoneProject.service;

import Capstone.capstoneProject.dto.Chats.NoticeDetailResponse;
import Capstone.capstoneProject.dto.Chats.NoticeResponse;
import Capstone.capstoneProject.dto.Chats.NoticeSummaryResponse;
import Capstone.capstoneProject.entity.Chats.ChatNotices;
import Capstone.capstoneProject.entity.Chats.ChatRoomUsers;
import Capstone.capstoneProject.entity.Chats.ChatRooms;
import Capstone.capstoneProject.entity.Users;
import Capstone.capstoneProject.enums.DateSortType;
import Capstone.capstoneProject.exceptions.forbidden.ChatRoomAccessDeniedException;
import Capstone.capstoneProject.exceptions.notfound.ChatRoomNotFoundException;
import Capstone.capstoneProject.exceptions.notfound.ChatRoomNoticeNotFoundException;
import Capstone.capstoneProject.repository.ChatNoticesRepository;
import Capstone.capstoneProject.repository.ChatRoomUsersRepository;
import Capstone.capstoneProject.repository.ChatRoomsRepository;
import Capstone.capstoneProject.security.AuthenticatedUserUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ChallengeNoticeService {
    private final AuthenticatedUserUtils authenticatedUserUtils;
    private final ChatRoomsRepository chatRoomsRepository;
    private final ChatRoomUsersRepository chatRoomUsersRepository;
    private final ChatNoticesRepository chatNoticesRepository;

    public NoticeSummaryResponse getRecentNotice(String roomId) {
        Users user = authenticatedUserUtils.getCurrentUser();

        ChatRooms chatRooms = chatRoomsRepository.findByRoomId(roomId)
                .orElseThrow(() -> new ChatRoomNotFoundException("해당 채팅방을 찾을 수 없습니다."));

        ChatRoomUsers chatRoomUsers = chatRoomUsersRepository.findByChatRooms_RoomIdAndUsers(roomId, user)
                .orElseThrow(() -> new ChatRoomAccessDeniedException("해당 채팅방 유저가 아닙니다."));

        ChatNotices notice = chatNoticesRepository
                .findTopByChatRoomsOrderByCreatedAtDesc(chatRooms)
                .orElse(null);

        return NoticeSummaryResponse.from(notice, user);
    }

    public NoticeDetailResponse getNotice(String roomId, Long noticeId) {
        Users user = authenticatedUserUtils.getCurrentUser();

        ChatRooms chatRooms = chatRoomsRepository.findByRoomId(roomId)
                .orElseThrow(() -> new ChatRoomNotFoundException("해당 채팅방을 찾을 수 없습니다."));

        ChatRoomUsers chatRoomUsers = chatRoomUsersRepository.findByChatRooms_RoomIdAndUsers(roomId, user)
                .orElseThrow(() -> new ChatRoomAccessDeniedException("해당 채팅방 유저가 아닙니다."));

        ChatNotices notice = chatNoticesRepository.findById(noticeId)
                .orElseThrow(() -> new ChatRoomNoticeNotFoundException("채팅방의 해당 공지를 찾을 수 없습니다."));

        return NoticeDetailResponse.from(notice, chatRoomUsers, user);
    }

    public List<NoticeResponse> getNotices(String roomId, DateSortType dateSortType) {
        Users user = authenticatedUserUtils.getCurrentUser();

        ChatRooms chatRooms = chatRoomsRepository.findByRoomId(roomId)
                .orElseThrow(() -> new ChatRoomNotFoundException("해당 채팅방을 찾을 수 없습니다."));

        ChatRoomUsers chatRoomUsers = chatRoomUsersRepository.findByChatRooms_RoomIdAndUsers(roomId, user)
                .orElseThrow(() -> new ChatRoomAccessDeniedException("해당 채팅방 유저가 아닙니다."));
        List<ChatNotices> chatNotices;

        List<ChatNotices> notices =
                dateSortType == DateSortType.OLDEST
                        ? chatNoticesRepository.findAllByChatRoomsOrderByCreatedAtAsc(chatRooms)
                        : chatNoticesRepository.findAllByChatRoomsOrderByCreatedAtDesc(chatRooms);

        return notices.stream()
                .map(notice -> NoticeResponse.from(notice, chatRoomUsers, user))
                .toList();
    }
}
