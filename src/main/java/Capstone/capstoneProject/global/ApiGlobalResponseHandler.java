package Capstone.capstoneProject.global;

import Capstone.capstoneProject.exceptions.*;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
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
        return ResponseEntity.status(403)
                .body(ApiResponse.error("유효하지 않은 리프레시 토큰입니다."));
    }

    @ExceptionHandler(ChallengeNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleChallengeNotFoundException(ChallengeNotFoundException e) {
        return ResponseEntity.status(404)
                .body(ApiResponse.error("해당 챌린지방을 찾을 수 없습니다."));
    }

    // 이미 사용자 계정이 있을 때
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<Void>> handleUserAlreadyExistsException(UserAlreadyExistsException e) {
        return ResponseEntity.status(409)
                .body(ApiResponse.error("이미 존재하는 계정입니다."));
    }

    // 해당 월에 요청하느 주차가 없을 때
    @ExceptionHandler(WeekNotInMonthException.class)
    public ResponseEntity<ApiResponse<Void>> handleWeekNotInMonthException(WeekNotInMonthException e) {
        return ResponseEntity.status(400)
                .body(ApiResponse.error("해당 월에 요청하는 주차가 없습니다."));
    }

    // 검색 조건이 서로 충돌
    @ExceptionHandler(ConflictingSearchCriteriaException.class)
    public ResponseEntity<ApiResponse<Void>> handleConflictingSearchCriteriaException(ConflictingSearchCriteriaException e) {
        return ResponseEntity.status(400)
                .body(ApiResponse.error("검색 조건(Preset, 커스텀 날짜, 연/월/주)은 하나만 선택해야 합니다."));
    }

    // 시작날짜를 끝날짜 보다 늦은 날짜인 경우
    @ExceptionHandler(InvalidDateRangeException.class)
    public ResponseEntity<ApiResponse<Void>> handleInvalidDateRangeException(InvalidDateRangeException e) {
        return ResponseEntity.status(400)
                .body(ApiResponse.error("시작 날짜는 종료 날짜보다 늦을 수 없습니다."));
    }

    // @Min, @Max 예외처리
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .findFirst()
                .orElse("입력 값이 올바르지 않습니다.");

        return ResponseEntity.badRequest().body(ApiResponse.error(errorMessage));
    }

    // 게시글 좋아요 불일치 오류
    @ExceptionHandler(BoardLikeNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleBoardLikeNotFoundException(BoardLikeNotFoundException e) {
        return ResponseEntity.status(404)
                .body(ApiResponse.error("해당 게시글의 좋아요 데이터가 존재하지 않습니다."));
    }



    @ExceptionHandler(AlreadyJoinedException.class)
    public ResponseEntity<ApiResponse<Void>> handleAlreadyJoinedException(AlreadyJoinedException e) {
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

    @ExceptionHandler(BoardNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleBoardNotFoundException(BoardNotFoundException e) {
        return ResponseEntity.status(404)
                .body(ApiResponse.error("해당 게시글을 찾을 수 없습니다."));
    }

    @ExceptionHandler(CommentNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleCommentNotFoundException(CommentNotFoundException e) {
        return ResponseEntity.status(404)
                .body(ApiResponse.error("해당 댓글을 찾을 수 없습니다."));
    }

    @ExceptionHandler(NotChallengeOwnerException.class)
    public ResponseEntity<ApiResponse<Void>> handleNotChallengeOwnerException(NotChallengeOwnerException e) {
        return ResponseEntity.status(403)
                .body(ApiResponse.error("해당 챌린지방에 관한 권한이 없습니다."));
    }

    @ExceptionHandler(NotCommentOwnerException.class)
    public ResponseEntity<ApiResponse<Void>> handleNotCommentOwnerException(NotCommentOwnerException e) {
        return ResponseEntity.status(403)
                .body(ApiResponse.error("해당 댓글에 관한 권한이 없습니다."));
    }

    @ExceptionHandler(NotBoardOwnerException.class)
    public ResponseEntity<ApiResponse<Void>> handleNotBoardOwnerException(NotBoardOwnerException e) {
        return ResponseEntity.status(403)
                .body(ApiResponse.error("해당 게시글에 관한 권한이 없습니다."));
    }

    // 영수증 ai 호출 실패
    @ExceptionHandler(ReceiptAiServerException.class)
    public ResponseEntity<ApiResponse<Void>> handleReceiptAiServerException(ReceiptAiServerException e) {
        return ResponseEntity.status(500)
                .body(ApiResponse.error("영수증 AI 호출에 실패했습니다."));
    }

    // 챌린지 방 채팅방 참가자가 아닐 때 예외처리
    @ExceptionHandler(ChatRoomAccessDeniedException.class)
    public ResponseEntity<ApiResponse<Void>> handleChatRoomAccessDeniedException(ChatRoomAccessDeniedException e) {
        return ResponseEntity.status(403)
                .body(ApiResponse.error("해당 채팅방에 관한 권한이 없습니다."));
    }

    // 챌린지방 채팅방이 없을 때 예외처리
    @ExceptionHandler(ChatRoomNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleChatRoomNotFoundException(ChatRoomNotFoundException e) {
        return ResponseEntity.status(404)
                .body(ApiResponse.error("해당 채팅방을 찾을 수 없습니다."));
    }


    // 챌린지 방 참가자가 아닐 때 예외처리
    @ExceptionHandler(ChallengeAccessDeniedException.class)
    public ResponseEntity<ApiResponse<Void>> handleChallengeAccessDeniedException(ChallengeAccessDeniedException e) {
        return ResponseEntity.status(403)
                .body(ApiResponse.error("챌린지방 참가자가 아닙니다."));
    }



    // jwt 토큰이 없거나 인증 실패
    @ExceptionHandler(InvalidJwtAuthenticationException.class)
    public ResponseEntity<ApiResponse<Void>> handleInvalidJwtAuthenticationException(InvalidJwtAuthenticationException e) {
        return ResponseEntity.status(403)
                .body(ApiResponse.error("jwt 토큰이 없거나 유효하지 않습니다."));
    }


}
