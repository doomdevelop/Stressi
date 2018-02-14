package org.wirbleibenalle.stressi.data.remote;

/**
 * Created by and on 26.10.16.
 */

public class ResponseError {
    private int errorType;
    private int errorCode;

    public ResponseError(int errorType, int errorCode) {
        this.errorType = errorType;
        this.errorCode = errorCode;
    }

    public int getErrorType() {
        return errorType;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public static final int ERROR_UNDEFINED = -1;
    public static final int ERROR_TOKEN_EXPIRED = 1;
    public static final int ERROR_DATABASE = 2;
    public static final int ERROR_NETWORK_CONNECTION = 3;
}
