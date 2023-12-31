package com.douzone.wehago.common.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@Getter
public enum ErrorCode {

    // Common
    INVALID_INPUT_VALUE(400, "INVALID_INPUT_VALUE", "잘못된 입력값 입니다."),
    METHOD_NOT_ALLOWED(405, "METHOD_NOT_ALLOWED", " 허용되지 않은 메서드 입니다."),
    ENTITY_NOT_FOUND(400, "ENTITY_NOT_FOUND", "개체를 찾을 수 없습니다."),
    INTERNAL_SERVER_ERROR(500, "INTERNAL_SERVER_ERROR", "내부 서버 에러"),
    INVALID_TYPE_VALUE(400, "INVALID_TYPE_VALUE", "잘못된 유형 값"),
    HANDLE_ACCESS_DENIED(403, "HANDLE_ACCESS_DENIED", "접근 권한이 없습니다."),

    // Company
    COMPANY_NOT_EXIST(404, "COMPANY_NOT_EXIST", "회사가 존재하지 않습니다."),


    // == File Upload ==//
    FILE_NOT_EXIST(404, "FILE_NOT_EXIST", "파일이 없습니다. 파일을 추가해 주세요."),
    FILE_SIZE_EXCEED(413, "FILE_SIZE_EXCEED", "업로드 할 수 있는 파일 최대 크기는 20MB 입니다."),
    INVALID_FILE_TYPE(415, "INVALID_FILE_TYPE", "업로드 할 수 있는 파일 형식은 jpg, jpeg, png 입니다."),

    // Mail
    MAIL_SEND_FAIL(500, "MAIL_SEND_FAIL", "메일 전송 실패"),


    //JWT
    JWT_NOT_PERMIT(400, "JWT_NOT_PERMIT", "존재하지 않는 Token 입니다."),
    JWT_INVALID_TOKEN(401, "JWT_INVALID_TOKEN", "토큰이 유효하지 않습니다.")
    ;



    private final int status;
    private final String code;
    private final String message;

}
