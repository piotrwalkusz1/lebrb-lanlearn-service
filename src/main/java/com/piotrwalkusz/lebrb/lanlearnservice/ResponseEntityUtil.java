package com.piotrwalkusz.lebrb.lanlearnservice;

import com.piotrwalkusz.lebrb.lanlearnservice.model.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseEntityUtil {

    public static ResponseEntity<ErrorMessage> error(HttpStatus status, String message) {
        return ResponseEntity.status(status).body(new ErrorMessage(message));
    }

    public static ResponseEntity<ErrorMessage> error(HttpStatus status, String message, Object... params) {
        return error(status, String.format(message, (Object[]) params));
    }

    public static ResponseEntityBuilder badRequest() {
        return new ResponseEntityBuilder(HttpStatus.BAD_REQUEST);
    }

    public static ResponseEntityBuilder unauthorized() {
        return new ResponseEntityBuilder(HttpStatus.UNAUTHORIZED);
    }

    public static ResponseEntityBuilder internalServerError() {
        return new ResponseEntityBuilder(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public static class ResponseEntityBuilder {
        private HttpStatus status;

        public ResponseEntityBuilder(HttpStatus status) {
            this.status = status;
        }

        public ResponseEntity<ErrorMessage> message(String message) {
            return error(status, message);
        }

        public ResponseEntity<ErrorMessage> message(String message, Object... params) {
            return error(status, message, ((Object[]) params));
        }
    }
}
