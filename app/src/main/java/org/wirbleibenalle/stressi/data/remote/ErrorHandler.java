package org.wirbleibenalle.stressi.data.remote;

/**
 * Created by matahari on 16.02.18.
 */

public class ErrorHandler {
    private final ErrorHandlerInterceptor errorHandlerInterceptor;

    public ErrorHandler(ErrorHandlerInterceptor errorHandlerInterceptor) {
        this.errorHandlerInterceptor = errorHandlerInterceptor;
    }

    public void addResponseErrorListener(ResponseErrorListener responseErrorListener){
        errorHandlerInterceptor.addResponseErrorListener(responseErrorListener);
    }

    public void removeResponseErrorListener(ResponseErrorListener responseErrorListener){
        errorHandlerInterceptor.removeResponseErrorListener(responseErrorListener);
    }

}
