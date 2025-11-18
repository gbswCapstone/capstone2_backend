package Capstone.capstoneProject.global;

import Capstone.capstoneProject.exceptions.*;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.rmi.AlreadyBoundException;

@Hidden
@RestControllerAdvice(annotations = RestController.class)
public class ApiGlobalResponseHandler {

    // 404 에러 핸들링
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleNotFound(NoHandlerFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(e.getMessage()));
    }

    // 400 에러 핸들링
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Void>> handleBadRequest(IllegalArgumentException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(e.getMessage()));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleUserNotFoundException(UserNotFoundException e) {
        return ResponseEntity.status(404)
                .body(ApiResponse.error("사용자를 찾을 수 없습니다."));
    }

    @ExceptionHandler(NotAuthenticatedException.class)
    public ResponseEntity<ApiResponse<Void>> handleNotAuthenticatedException(NotAuthenticatedException e) {
        return ResponseEntity.status(401)
                .body(ApiResponse.error("로그인이 필요합니다."));
    }

    @ExceptionHandler(RefreshTokenNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleRefreshTokenNotFoundException(RefreshTokenNotFoundException e) {
        return ResponseEntity.status(404)
                .body(ApiResponse.error("유효하지 않은 리프레시 토큰입니다."));
    }

    @ExceptionHandler(ChallengeNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleChallengeNotFoundException(ChallengeNotFoundException e) {
        return ResponseEntity.status(404)
                .body(ApiResponse.error("해당 챌린지방을 찾을 수 없습니다."));
    }

    @ExceptionHandler(AlreadyBoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleAlreadyBoundException(AlreadyBoundException e) {
        return ResponseEntity.status(409)
                .body(ApiResponse.error("이미 참여한 챌린지입니다."));
    }

    @ExceptionHandler(ChallengeFullException.class)
    public ResponseEntity<ApiResponse<Void>> handleChallengeFullException(ChallengeFullException e) {
        return ResponseEntity.status(409)
                .body(ApiResponse.error("참여 인원이 가득 찼습니다."));
    }

    @ExceptionHandler(PasswordMismatchException.class)
    public ResponseEntity<ApiResponse<Void>> handlePasswordMismatchException(PasswordMismatchException e) {
        return ResponseEntity.status(401)
                .body(ApiResponse.error("비밀번호가 일치하지 않습니다."));
    }



}
