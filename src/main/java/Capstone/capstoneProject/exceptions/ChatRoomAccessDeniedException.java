package Capstone.capstoneProject.exceptions;

public class ChatRoomAccessDeniedException extends RuntimeException {
    public ChatRoomAccessDeniedException(String message) {
        super(message);
    }
}
