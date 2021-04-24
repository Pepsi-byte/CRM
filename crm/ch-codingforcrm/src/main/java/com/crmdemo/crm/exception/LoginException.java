package com.crmdemo.crm.exception;

//自定义异常类
public class LoginException extends Exception {


    public LoginException() {
        super();
    }

    public LoginException(String message) {
        super(message);
    }
}
