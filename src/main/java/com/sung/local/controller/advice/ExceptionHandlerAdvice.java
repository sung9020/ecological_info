package com.sung.local.controller.advice;

import com.sung.local.dto.ResponseDto;
import com.sung.local.enums.ErrorFormat;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/*
 *
 * @author 123msn
 * @since 2019-08-18
 */
@RestControllerAdvice
@Slf4j
public class ExceptionHandlerAdvice {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    ResponseDto handleException(Exception ex) {
        ResponseDto result = new ResponseDto();
        result.setMsg(ErrorFormat.RUNTIME_ERROR.getMsg()); //not custom
        log.error(ex.getMessage());
        return result;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    ResponseDto handleUnauthorizedException(AccessDeniedException ex) {
        ResponseDto result = new ResponseDto();
        result.setMsg(ErrorFormat.FORBIDDEN_ERROR.getMsg()); //not custom
        log.error(ex.getMessage());

        return result;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    ResponseDto handleAuthenticationException(AuthenticationException ex) {
        ResponseDto result = new ResponseDto();
        result.setMsg(ErrorFormat.FORBIDDEN_ERROR.getMsg()); //not custom
        log.error(ex.getMessage());

        return result;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    ResponseDto handleIllegalArgumentException(IllegalArgumentException ex) {
        ResponseDto result = new ResponseDto();
        result.setMsg(ex.getMessage());
        log.error(ex.getMessage());

        return result;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    ResponseDto handleUsernameNotFoundException(UsernameNotFoundException ex) {
        ResponseDto result = new ResponseDto();
        result.setMsg(ex.getMessage());
        log.error(ex.getMessage());

        return result;
    }
}
