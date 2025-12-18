package Capstone.capstoneProject.global;

import Capstone.capstoneProject.exceptions.common.DomainException;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;



@Hidden
@RestControllerAdvice
public class ApiGlobalResponseHandler {

    // 트랜잭션 예외처리
    @ExceptionHandler(TransactionSystemException.class)
    public ResponseEntity<ApiResponse<Void>> handleTransactionException(TransactionSystemException e) {
        Throwable cause = e;
        while (cause != null) {
            if (cause instanceof DomainException de) {
                return ResponseEntity
                        .status(de.getStatus())
                        .body(ApiResponse.error(de.getMessage()));
            }
            cause = cause.getCause();
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("트랜잭션 처리 중 오류가 발생했습니다."));
    }

    // 공통 부모 예외
    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ApiResponse<Void>> handleDomainException(DomainException e) {
        return ResponseEntity
                .status(e.getStatus())
                .body(ApiResponse.error(e.getMessage()));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<Void>> handleAccessDenied(AccessDeniedException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(ApiResponse.error(e.getMessage()));
    }


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
//
//    public class UserNotFoundException extends DomainException {
//        public UserNotFoundException() {
//            super(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다.");
//        }
//    }
//
////    @ExceptionHandler(UserNotFoundException.class)
////    public ResponseEntity<ApiResponse<Void>> handleUserNotFoundException(UserNotFoundException e) {
////        return ResponseEntity.status(404)
////                .body(ApiResponse.error("사용자를 찾을 수 없습니다."));
////    }
//
//    public class NotAuthenticatedException extends DomainException {
//        public NotAuthenticatedException() { super(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다."); }
//    }
//
////    @ExceptionHandler(NotAuthenticatedException.class)
////    public ResponseEntity<ApiResponse<Void>> handleNotAuthenticatedException(NotAuthenticatedException e) {
////        return ResponseEntity.status(401)
////                .body(ApiResponse.error("로그인이 필요합니다."));
////    }
//
//    public class RefreshTokenNotFoundException extends DomainException {
//        public RefreshTokenNotFoundException() { super(HttpStatus.FORBIDDEN, "유효하지 않은 리프레시 토큰입니다."); }
//    }
//
////    @ExceptionHandler(RefreshTokenNotFoundException.class)
////    public ResponseEntity<ApiResponse<Void>> handleRefreshTokenNotFoundException(RefreshTokenNotFoundException e) {
////        return ResponseEntity.status(403)
////                .body(ApiResponse.error("유효하지 않은 리프레시 토큰입니다."));
////    }
//
//    public class ChallengeNotFoundException extends DomainException {
//        public ChallengeNotFoundException() { super(HttpStatus.NOT_FOUND, "해당 챌린지방을 찾을 수 없습니다."); }
//    }
//
////    @ExceptionHandler(ChallengeNotFoundException.class)
////    public ResponseEntity<ApiResponse<Void>> handleChallengeNotFoundException(ChallengeNotFoundException e) {
////        return ResponseEntity.status(404)
////                .body(ApiResponse.error("해당 챌린지방을 찾을 수 없습니다."));
////    }
//
//    // 이미 사용자 계정이 있을 때
//    public class UserAlreadyExistsException extends DomainException {
//        public UserAlreadyExistsException() { super(HttpStatus.CONFLICT, "이미 존재하는 계정입니다."); }
//    }
//
////
////    @ExceptionHandler(UserAlreadyExistsException.class)
////    public ResponseEntity<ApiResponse<Void>> handleUserAlreadyExistsException(UserAlreadyExistsException e) {
////        return ResponseEntity.status(409)
////                .body(ApiResponse.error("이미 존재하는 계정입니다."));
////    }
//
//    // 해당 월에 요청하느 주차가 없을 때
//    public class WeekNotInMonthException extends DomainException {
//        public WeekNotInMonthException() { super(HttpStatus.BAD_REQUEST, "해당 월에 요청하는 주차가 없습니다."); }
//    }
//
//
////
////    @ExceptionHandler(WeekNotInMonthException.class)
////    public ResponseEntity<ApiResponse<Void>> handleWeekNotInMonthException(WeekNotInMonthException e) {
////        return ResponseEntity.status(400)
////                .body(ApiResponse.error("해당 월에 요청하는 주차가 없습니다."));
////    }
//
//
//    // 검색 조건이 서로 충돌
//    public class ConflictingSearchCriteriaException extends DomainException {
//        public ConflictingSearchCriteriaException() {
//            super(HttpStatus.BAD_REQUEST, "검색 조건(Preset, 커스텀 날짜, 연/월/주)은 하나만 선택해야 합니다.");
//        }
//    }
//
////    @ExceptionHandler(ConflictingSearchCriteriaException.class)
////    public ResponseEntity<ApiResponse<Void>> handleConflictingSearchCriteriaException(ConflictingSearchCriteriaException e) {
////        return ResponseEntity.status(400)
////                .body(ApiResponse.error("검색 조건(Preset, 커스텀 날짜, 연/월/주)은 하나만 선택해야 합니다."));
////    }
//
//    // 시작날짜를 끝날짜 보다 늦은 날짜인 경우
//    public class InvalidDateRangeException extends DomainException {
//        public InvalidDateRangeException() { super(HttpStatus.BAD_REQUEST, "시작 날짜는 종료 날짜보다 늦을 수 없습니다."); }
//    }
//
////    @ExceptionHandler(InvalidDateRangeException.class)
////    public ResponseEntity<ApiResponse<Void>> handleInvalidDateRangeException(InvalidDateRangeException e) {
////        return ResponseEntity.status(400)
////                .body(ApiResponse.error("시작 날짜는 종료 날짜보다 늦을 수 없습니다."));
////    }
//
//    // 잔액이 없을 때
//    public class BalanceNotFoundException extends DomainException {
//        public BalanceNotFoundException() { super(HttpStatus.NOT_FOUND, "잔액을 찾을 수 없습니다."); }
//    }
////    @ExceptionHandler(BalanceNotFoundException.class)
////    public ResponseEntity<ApiResponse<Void>> handleBalanceNotFoundException(BalanceNotFoundException e) {
////        return ResponseEntity.status(404)
////                .body(ApiResponse.error("잔액을 찾을 수 없습니다."));
////    }
//
//
//
//
//    // 게시글 좋아요 불일치 오류
//    public class BoardLikeNotFoundException extends DomainException {
//        public BoardLikeNotFoundException() { super(HttpStatus.NOT_FOUND, "해당 게시글의 좋아요 데이터가 존재하지 않습니다."); }
//    }
////    @ExceptionHandler(BoardLikeNotFoundException.class)
////    public ResponseEntity<ApiResponse<Void>> handleBoardLikeNotFoundException(BoardLikeNotFoundException e) {
////        return ResponseEntity.status(404)
////                .body(ApiResponse.error("해당 게시글의 좋아요 데이터가 존재하지 않습니다."));
////    }
//
//    public class AlreadyJoinedException extends DomainException {
//        public AlreadyJoinedException() { super(HttpStatus.CONFLICT, "이미 참여한 챌린지입니다."); }
//    }
//
////    @ExceptionHandler(AlreadyJoinedException.class)
////    public ResponseEntity<ApiResponse<Void>> handleAlreadyJoinedException(AlreadyJoinedException e) {
////        return ResponseEntity.status(409)
////                .body(ApiResponse.error("이미 참여한 챌린지입니다."));
////    }
//
//
//    public class ChallengeFullException extends DomainException {
//        public ChallengeFullException() { super(HttpStatus.CONFLICT, "참여 인원이 가득 찼습니다."); }
//    }
//
////    @ExceptionHandler(ChallengeFullException.class)
////    public ResponseEntity<ApiResponse<Void>> handleChallengeFullException(ChallengeFullException e) {
////        return ResponseEntity.status(409)
////                .body(ApiResponse.error("참여 인원이 가득 찼습니다."));
////    }
//
//    public class PasswordMismatchException extends DomainException {
//        public PasswordMismatchException() { super(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다."); }
//    }
//
////    @ExceptionHandler(PasswordMismatchException.class)
////    public ResponseEntity<ApiResponse<Void>> handlePasswordMismatchException(PasswordMismatchException e) {
////        return ResponseEntity.status(401)
////                .body(ApiResponse.error("비밀번호가 일치하지 않습니다."));
////    }
//
//    public class BoardNotFoundException extends DomainException {
//        public BoardNotFoundException() { super(HttpStatus.NOT_FOUND, "해당 게시글을 찾을 수 없습니다."); }
//    }
//
////    @ExceptionHandler(BoardNotFoundException.class)
////    public ResponseEntity<ApiResponse<Void>> handleBoardNotFoundException(BoardNotFoundException e) {
////        return ResponseEntity.status(404)
////                .body(ApiResponse.error("해당 게시글을 찾을 수 없습니다."));
////    }
//
//    public class CommentNotFoundException extends DomainException {
//        public CommentNotFoundException() { super(HttpStatus.NOT_FOUND, "해당 댓글을 찾을 수 없습니다."); }
//    }
//
////    @ExceptionHandler(CommentNotFoundException.class)
////    public ResponseEntity<ApiResponse<Void>> handleCommentNotFoundException(CommentNotFoundException e) {
////        return ResponseEntity.status(404)
////                .body(ApiResponse.error("해당 댓글을 찾을 수 없습니다."));
////    }
//
//    public class NotChallengeOwnerException extends DomainException {
//        public NotChallengeOwnerException() { super(HttpStatus.FORBIDDEN, "해당 챌린지방에 관한 권한이 없습니다."); }
//    }
//
////    @ExceptionHandler(NotChallengeOwnerException.class)
////    public ResponseEntity<ApiResponse<Void>> handleNotChallengeOwnerException(NotChallengeOwnerException e) {
////        return ResponseEntity.status(403)
////                .body(ApiResponse.error("해당 챌린지방에 관한 권한이 없습니다."));
////    }
//
//    public class NotCommentOwnerException extends DomainException {
//        public NotCommentOwnerException() { super(HttpStatus.FORBIDDEN, "해당 댓글에 관한 권한이 없습니다."); }
//    }
////    @ExceptionHandler(NotCommentOwnerException.class)
////    public ResponseEntity<ApiResponse<Void>> handleNotCommentOwnerException(NotCommentOwnerException e) {
////        return ResponseEntity.status(403)
////                .body(ApiResponse.error("해당 댓글에 관한 권한이 없습니다."));
////    }
//
//    public class NotBoardOwnerException extends DomainException {
//        public NotBoardOwnerException() { super(HttpStatus.FORBIDDEN, "해당 게시글에 관한 권한이 없습니다."); }
//    }
//
////    @ExceptionHandler(NotBoardOwnerException.class)
////    public ResponseEntity<ApiResponse<Void>> handleNotBoardOwnerException(NotBoardOwnerException e) {
////        return ResponseEntity.status(403)
////                .body(ApiResponse.error("해당 게시글에 관한 권한이 없습니다."));
////    }
//
//    // 영수증 ai 호출 실패
//    public class ReceiptAiServerException extends DomainException {
//        public ReceiptAiServerException() { super(HttpStatus.INTERNAL_SERVER_ERROR, "영수증 AI 호출에 실패했습니다."); }
//    }
//
//
////    @ExceptionHandler(ReceiptAiServerException.class)
////    public ResponseEntity<ApiResponse<Void>> handleReceiptAiServerException(ReceiptAiServerException e) {
////        return ResponseEntity.status(500)
////                .body(ApiResponse.error("영수증 AI 호출에 실패했습니다."));
////    }
//
//    // 챌린지 방 채팅방 참가자가 아닐 때 예외처리
//    public class ChatRoomAccessDeniedException extends DomainException {
//        public ChatRoomAccessDeniedException() { super(HttpStatus.FORBIDDEN, "해당 채팅방 유저가 아닙니다."); }
//    }
//
////    @ExceptionHandler(ChatRoomAccessDeniedException.class)
////    public ResponseEntity<ApiResponse<Void>> handleChatRoomAccessDeniedException(ChatRoomAccessDeniedException e) {
////        return ResponseEntity.status(403)
////                .body(ApiResponse.error("해당 채팅방 유저가 아닙니다."));
////    }
//
//    // 챌린지방 채팅방이 없을 때 예외처리
//    public class ChatRoomNotFoundException extends DomainException {
//        public ChatRoomNotFoundException() { super(HttpStatus.NOT_FOUND, "해당 채팅방을 찾을 수 없습니다."); }
//    }
//
////    @ExceptionHandler(ChatRoomNotFoundException.class)
////    public ResponseEntity<ApiResponse<Void>> handleChatRoomNotFoundException(ChatRoomNotFoundException e) {
////        return ResponseEntity.status(404)
////                .body(ApiResponse.error("해당 채팅방을 찾을 수 없습니다."));
////    }
//
//    public class ChatImageAccessDeniedException extends DomainException {
//        public ChatImageAccessDeniedException() { super(HttpStatus.FORBIDDEN, "채팅방의 해당 이미지에 관한 권한이 없습니다."); }
//    }
//
////    @ExceptionHandler(ChatImageAccessDeniedException.class)
////    public ResponseEntity<ApiResponse<Void>> handleChatImageAccessDeniedException(ChatImageAccessDeniedException e) {
////        return ResponseEntity.status(403)
////                .body(ApiResponse.error("채팅방의 해당 이미지에 관한 권한이 없습니다."));
////    }
//
//    // 이미지 삭제에 실패 했을 때 예외처리
//    public class ImageDeleteException extends DomainException {
//        public ImageDeleteException() { super(HttpStatus.INTERNAL_SERVER_ERROR, "이미지 삭제에 실패했습니다."); }
//    }
////    @ExceptionHandler(ImageDeleteException.class)
////    public ResponseEntity<ApiResponse<Void>> handleImageDeleteException(ImageDeleteException e) {
////        return ResponseEntity.status(500)
////                .body(ApiResponse.error("이미지 삭제에 실패했습니다."));
////    }
//
//    // 챌린지 방 참가자가 아닐 때 예외처리
//    public class ChallengeAccessDeniedException extends DomainException {
//        public ChallengeAccessDeniedException() { super(HttpStatus.FORBIDDEN, "챌린지방 참가자가 아닙니다."); }
//    }
//
////    @ExceptionHandler(ChallengeAccessDeniedException.class)
////    public ResponseEntity<ApiResponse<Void>> handleChallengeAccessDeniedException(ChallengeAccessDeniedException e) {
////        return ResponseEntity.status(403)
////                .body(ApiResponse.error("챌린지방 참가자가 아닙니다."));
////    }
//
//    // 본인 사용내역만 공유가능 함
//    public class UsageAccessDeniedException extends DomainException {
//        public UsageAccessDeniedException() { super(HttpStatus.FORBIDDEN, "본인 사용내역만 공유가능합니다."); }
//    }
//
////    @ExceptionHandler(UsageAccessDeniedException.class)
////    public ResponseEntity<ApiResponse<Void>> handleUsageAccessDeniedException(UsageAccessDeniedException e) {
////        return ResponseEntity.status(403)
////                .body(ApiResponse.error("본인 사용내역만 공유가능합니다."));
////    }
//
//    // jwt 토큰이 없거나 인증 실패
//    public class InvalidJwtAuthenticationException extends DomainException {
//        public InvalidJwtAuthenticationException() { super(HttpStatus.FORBIDDEN, "jwt 토큰이 없거나 유효하지 않습니다."); }
//    }
//
//
////    @ExceptionHandler(InvalidJwtAuthenticationException.class)
////    public ResponseEntity<ApiResponse<Void>> handleInvalidJwtAuthenticationException(InvalidJwtAuthenticationException e) {
////        return ResponseEntity.status(403)
////                .body(ApiResponse.error("jwt 토큰이 없거나 유효하지 않습니다."));
////    }
//
//    // 챌린지 채팅방 방장만 가능한 권한일 때 처리
//    public class ChatRoomHostRequiredException extends DomainException {
//        public ChatRoomHostRequiredException() { super(HttpStatus.FORBIDDEN, "채팅방 방장만 가능한 권한입니다."); }
//    }
////    @ExceptionHandler(ChatRoomHostRequiredException.class)
////    public ResponseEntity<ApiResponse<Void>> handleChatRoomHostRequiredException(ChatRoomHostRequiredException e) {
////        return ResponseEntity.status(403)
////                .body(ApiResponse.error("채팅방 방장만 가능한 권한입니다."));
////    }
//
//    // 챌린지 채팅방 공지가 없을 때 예외 처리
//    public class ChatRoomNoticeNotFoundException extends DomainException {
//        public ChatRoomNoticeNotFoundException() { super(HttpStatus.NOT_FOUND, "채팅방의 해당 공지를 찾을 수 없습니다."); }
//    }
//
////    @ExceptionHandler(ChatRoomNoticeNotFoundException.class)
////    public ResponseEntity<ApiResponse<Void>> handleChatRoomNoticeNotFoundException(ChatRoomNoticeNotFoundException e) {
////        return ResponseEntity.status(404)
////                .body(ApiResponse.error("채팅방의 해당 공지를 찾을 수 없습니다."));
////    }
//
//    // 챌린지 채팅방에 해당 메시지가 없을 때 예외 처리
//    public class ChatRoomMessageNotFoundException extends DomainException {
//        public ChatRoomMessageNotFoundException() { super(HttpStatus.NOT_FOUND, "채팅방의 해당 메시지를 찾을 수 없습니다."); }
//    }
////    @ExceptionHandler(ChatRoomMessageNotFoundException.class)
////    public ResponseEntity<ApiResponse<Void>> handleChatRoomMessageNotFoundException(ChatRoomMessageNotFoundException e) {
////        return ResponseEntity.status(404)
////                .body(ApiResponse.error("채팅방의 해당 메시지를 찾을 수 없습니다."));
////    }
//
//    // 챌린지 채팅방의 메시지 해당 작성자만 수정/삭제 가능
//    public class ChatMessageAccessDeniedException extends DomainException {
//        public ChatMessageAccessDeniedException() { super(HttpStatus.FORBIDDEN, "채팅방의 해당 메시지에 관한 권한이 없습니다."); }
//    }
////    @ExceptionHandler(ChatMessageAccessDeniedException.class)
////    public ResponseEntity<ApiResponse<Void>> handleChatMessageAccessDeniedException(ChatMessageAccessDeniedException e) {
////        return ResponseEntity.status(403)
////                .body(ApiResponse.error("채팅방의 해당 메시지에 관한 권한이 없습니다."));
////    }
//
//    // 해당 사용내역이 없을 때 처리
//    public class UsageHistoryNotFoundException extends DomainException {
//        public UsageHistoryNotFoundException() { super(HttpStatus.NOT_FOUND, "해당 사용내역을 찾을 수 없습니다."); }
//    }
////    @ExceptionHandler(UsageHistoryNotFoundException.class)
////    public ResponseEntity<ApiResponse<Void>> handleUsageHistoryNotFoundException(UsageHistoryNotFoundException e) {
////        return ResponseEntity.status(404)
////                .body(ApiResponse.error("해당 사용내역을 찾을 수 없습니다."));
////    }
//
//    // 타입이 이미지가 아닌 경우에 처리
//    public class ImageMessageRequiredException extends DomainException {
//        public ImageMessageRequiredException() { super(HttpStatus.BAD_REQUEST, "이미지 메시지가 아닙니다."); }
//    }
////    @ExceptionHandler(ImageMessageRequiredException.class)
////    public ResponseEntity<ApiResponse<Void>> handleImageMessageRequiredException(ImageMessageRequiredException e) {
////        return ResponseEntity.status(400)
////                .body(ApiResponse.error("이미지 메시지가 아닙니다."));
////    }
//
//    // 타입이 텍스트가 아닌 경우에 처리
//    public class TextMessageRequiredException extends DomainException {
//        public TextMessageRequiredException() { super(HttpStatus.BAD_REQUEST, "텍스트 메시지가 아닙니다."); }
//    }
////    @ExceptionHandler(TextMessageRequiredException.class)
////    public ResponseEntity<ApiResponse<Void>> handleTextMessageRequiredException(TextMessageRequiredException e) {
////        return ResponseEntity.status(400)
////                .body(ApiResponse.error("텍스트 메시지가 아닙니다."));
////    }
//
//    // 홈에서 챗봇 메시지 생성에 실패했을 경우에 처리
//    public class ChatBotHomeMessageFailedException extends DomainException {
//        public ChatBotHomeMessageFailedException() { super(HttpStatus.BAD_GATEWAY, "홈 챗봇 메시지 생성에 실패했습니다."); }
//    }
////    @ExceptionHandler(ChatBotHomeMessageFailedException.class)
////    public ResponseEntity<ApiResponse<Void>> handleChatBotHomeMessageFailedException(ChatBotHomeMessageFailedException e) {
////        return ResponseEntity.status(502)
////                .body(ApiResponse.error("홈 챗봇 메시지 생성에 실패했습니다."));
////    }
//
//    // 챗봇방을 찾을 수 없을 때 예외처리
//    public class ChatBotRoomNotFoundException extends DomainException {
//        public ChatBotRoomNotFoundException() { super(HttpStatus.NOT_FOUND, "해당 챗봇 채팅방을 찾을 수 없습니다."); }
//    }
////    @ExceptionHandler(ChatBotRoomNotFoundException.class)
////    public ResponseEntity<ApiResponse<Void>> handleChatBotRoomNotFoundException(ChatBotRoomNotFoundException e) {
////        return ResponseEntity.status(404)
////                .body(ApiResponse.error("해당 챗봇 채팅방을 찾을 수 없습니다."));
////    }
//
//    // 유저의 캐릭터(등급)을 찾을 수 없을 때 예외 처리
//    public class UserCharacterNotFoundException extends DomainException {
//        public UserCharacterNotFoundException() { super(HttpStatus.NOT_FOUND, "해당 유저의 캐릭터(등급)을 찾을 수 없습니다."); }
//    }
//
////    @ExceptionHandler(UserCharacterNotFoundException.class)
////    public ResponseEntity<ApiResponse<Void>> handleUserCharacterNotFoundException(UserCharacterNotFoundException e) {
////        return ResponseEntity.status(404)
////                .body(ApiResponse.error("해당 유저의 캐릭터(등급)을 찾을 수 없습니다."));
////    }
//
//    // 유효하지 않은 사용내역 수량일 때 처리
//    public class InvalidQuantityException extends DomainException {
//        public InvalidQuantityException(String message) { super(HttpStatus.BAD_REQUEST, "수량은 최소 1개 이상이어야 합니다.");}
//    }















}
