package Capstone.capstoneProject.controller;

import Capstone.capstoneProject.dto.ApiResponse;
import Capstone.capstoneProject.dto.ChatRoom;
import Capstone.capstoneProject.service.ChatService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;

    // 챗 목록 조회
    @GetMapping("/chatList")
    @Operation(summary = "채팅방 목록 전체조회", description = "현재 생성된 채팅방 목록 전체 조회")
    public String chatList(Model model) {
        List<ChatRoom> roomList = chatService.findAllRoom();
        model.addAttribute("roomList", roomList);
        return "chat/chatList";
    }

    // 챌린지 그룹채팅방 입장
    @GetMapping("/chat/{id}")
    @Operation(summary = "챌린지방 그룹채팅방 입장", description = "챌린지방에 가입되어있어야 가능")
    public ResponseEntity<ApiResponse<ChatRoom>> enterChatRoom(@PathVariable Long id) {
        ChatRoom room = chatService.enterChallengeChatRoom(id);
        return ResponseEntity.ok(ApiResponse.ok(room));
    }

    @PostMapping("/chat/createRoom") // 그냥 채팅방만 만들어짐
    public String createRoom(Model model, @RequestParam String name, String username) {
        ChatRoom room = chatService.createRoom(name);
        model.addAttribute("room",room);
        model.addAttribute("username",username);
        return "chat/chatRoom";
    }

    @GetMapping("/chat/chatRoom")
    public String chatRoom(Model model, @RequestParam String roomId){
        ChatRoom room = chatService.findRoomById(roomId);
        model.addAttribute("room",room);   //현재 방에 들어오기위해서 필요한데...... 접속자 수 등등은 실시간으로 보여줘야 돼서 여기서는 못함
        return "chat/chatRoom";
    }


}
