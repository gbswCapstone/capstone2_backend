package Capstone.capstoneProject.service;


import Capstone.capstoneProject.dto.Chats.NoticeRequest;
import Capstone.capstoneProject.dto.Chats.NoticeResponse;
import Capstone.capstoneProject.entity.Chats.ChatNotices;
import Capstone.capstoneProject.entity.Chats.ChatRoomUsers;
import Capstone.capstoneProject.entity.Chats.ChatRooms;
import Capstone.capstoneProject.entity.Users;
import Capstone.capstoneProject.enums.ChatRoomRole;
import Capstone.capstoneProject.exceptions.forbidden.ChatRoomAccessDeniedException;
import Capstone.capstoneProject.exceptions.forbidden.ChatRoomHostRequiredException;
import Capstone.capstoneProject.exceptions.notfound.ChatRoomNotFoundException;
import Capstone.capstoneProject.exceptions.notfound.ChatRoomNoticeNotFoundException;
import Capstone.capstoneProject.repository.ChatNoticesRepository;
import Capstone.capstoneProject.repository.ChatRoomUsersRepository;
import Capstone.capstoneProject.repository.ChatRoomsRepository;
import Capstone.capstoneProject.security.AuthenticatedUserUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class ChallengeAdminService {
    private final AuthenticatedUserUtils authenticatedUserUtils;
    private final ChatRoomUsersRepository chatRoomUsersRepository;
    private final ChatRoomsRepository chatRoomsRepository;
    private final ChatNoticesRepository chatNoticesRepository;

    public NoticeResponse createNotice(String roomId, NoticeRequest request) {
        Users user = authenticatedUserUtils.getCurrentUser();

        ChatRooms chatRooms = chatRoomsRepository.findByRoomId(roomId)
                .orElseThrow(() -> new ChatRoomNotFoundException("해당 채팅방을 찾을 수 없습니다."));

        ChatRoomUsers chatRoomUsers = chatRoomUsersRepository.findByChatRooms_RoomIdAndUsers(roomId, user)
                .orElseThrow(() -> new ChatRoomAccessDeniedException("해당 채팅방 유저가 아닙니다."));

        if(chatRoomUsers.getChatRoomRole() != ChatRoomRole.HOST) {
            throw new ChatRoomHostRequiredException("채팅방 방장만 가능한 권한입니다.");
        }

        ChatNotices chatNotices = ChatNotices.builder()
                .users(user)
                .chatRooms(chatRooms)
                .content(request.getContent())
                .build();
        chatNoticesRepository.save(chatNotices);

        return NoticeResponse.from(chatNotices, chatRoomUsers, user);
    }

    public NoticeResponse patchNotice(Long noticeId, NoticeRequest request) {
        Users user = authenticatedUserUtils.getCurrentUser();

        ChatNotices chatNotices = chatNoticesRepository.findById(noticeId)
                .orElseThrow(() -> new ChatRoomNoticeNotFoundException("채팅방 해당 공지를 찾을 수 없습니다."));

        ChatRooms chatRooms = chatRoomsRepository.findByRoomId(chatNotices.getChatRooms().getRoomId())
                .orElseThrow(() -> new ChatRoomNotFoundException("해당 채팅방을 찾을 수 없습니다."));

        ChatRoomUsers chatRoomUsers = chatRoomUsersRepository.findByChatRooms_RoomIdAndUsers(chatRooms.getRoomId(), user)
                .orElseThrow(() -> new ChatRoomAccessDeniedException("해당 채팅방 유저가 아닙니다."));

        if(chatRoomUsers.getChatRoomRole() != ChatRoomRole.HOST) {
            throw new ChatRoomHostRequiredException("채팅방 방장만 가능한 권한입니다.");
        }


        // 나중에 시간나면 마지막 수정자도 확장해두기!
        chatNotices.setContent(request.getContent());
        chatNoticesRepository.save(chatNotices);

        return NoticeResponse.from(chatNotices, chatRoomUsers, user);
    }

    public void deleteNotice(Long noticeId) {
        Users user = authenticatedUserUtils.getCurrentUser();

        ChatNotices chatNotices = chatNoticesRepository.findById(noticeId)
                .orElseThrow(() -> new ChatRoomNoticeNotFoundException("채팅방 해당 공지를 찾을 수 없습니다."));

        ChatRooms chatRooms = chatRoomsRepository.findByRoomId(chatNotices.getChatRooms().getRoomId())
                .orElseThrow(() -> new ChatRoomNotFoundException("해당 채팅방을 찾을 수 없습니다."));

        ChatRoomUsers chatRoomUsers = chatRoomUsersRepository.findByChatRooms_RoomIdAndUsers(chatRooms.getRoomId(), user)
                .orElseThrow(() -> new ChatRoomAccessDeniedException("해당 채팅방 유저가 아닙니다."));

        if(chatRoomUsers.getChatRoomRole() != ChatRoomRole.HOST) {
            throw new ChatRoomHostRequiredException("채팅방 방장만 가능한 권한입니다.");
        }



        // 공지 삭제
        chatNoticesRepository.delete(chatNotices);
    }
}
