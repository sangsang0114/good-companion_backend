package org.sku.zero.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    //400
    UNAUTHORIZED_CLIENT(HttpStatus.BAD_REQUEST, "접근 토큰이 없습니다."),
    PASSWORD_INCORRECT(HttpStatus.BAD_REQUEST, "비밀번호가 일차히지 않습니다."),
    INVALID_ADDRESS(HttpStatus.BAD_REQUEST, "유효하지 않은 주소입니다."),
    INVALID_ARGUMENTS(HttpStatus.BAD_REQUEST, "요청인자가 잘못되었습니다"),

    //401
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "만료된 토큰입니다."),
    JWT_DECODE_FAIL(HttpStatus.UNAUTHORIZED, "올바른 토큰이 필요합니다."),
    JWT_SIGNATURE_FAIL(HttpStatus.UNAUTHORIZED, "올바른 토큰이 필요합니다."),
    LOGIN_FAIL(HttpStatus.UNAUTHORIZED, "로그인에 실패하였습니다.\n이메일과 패스워드를 확인해주세요."),

    //403
    FORBIDDEN_CLIENT(HttpStatus.FORBIDDEN, "접근 권한이 없습니다."),
    FORBIDDEN_FILE(HttpStatus.FORBIDDEN, "업로드할 수 없는 파일 유형입니다"),

    //404
    SHOP_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 가게 정보입니다"),
    ATTACHMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 첨부파일입니다"),
    NOTICE_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 공지사항입니다."),
    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 상품입니다"),
    REVIEW_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 리뷰입니다"),
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 회원입니다"),
    PROPOSAL_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 요청입니다"),
    SECTOR_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 업종입니다"),

    //500
    SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류입니다\n잠시 후 다시 시도해주세요");

    private HttpStatus httpStatus;
    private String message;
}