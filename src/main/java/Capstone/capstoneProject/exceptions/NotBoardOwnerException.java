package Capstone.capstoneProject.exceptions;

public class NotBoardOwnerException extends RuntimeException {
    public NotBoardOwnerException(String message) {
        super(message);
    }
}
