package org.wirbleibenalle.stressi.data.remote;

/**
 * Created by and on 26.10.16.
 */

public class ResponseError {
    private String errorMessage;
    private int errorCode;

    public ResponseError(String errorMessage, int errorCode) {
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public static final int ERROR_UNDEFINED = -1;
    public static final int ERROR_TOKEN_EXPIRED = 1;
    public static final int ERROR_DATABASE = 2;
}
