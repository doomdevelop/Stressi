package org.wirbleibenalle.stressi.data.remote;

import java.io.IOException;

/**
 * Created by matahari on 14.02.18.
 */

public class ResponseException extends IOException {
    private final ResponseError responseError;


    public ResponseException(ResponseError responseError) {
        this.responseError = responseError;
    }


    public ResponseError getResponseError() {
        return responseError;
    }
}
