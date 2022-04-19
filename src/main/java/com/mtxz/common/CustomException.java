package com.mtxz.common;

/**
 * 自定义业务异常
 * Created by IntelliJ IDEA.
 * author: ylb
 * Date: 2022/4/16
 * Time: 10:32
 */
public class CustomException extends RuntimeException{

    public CustomException(String message) {
        super(message);
    }
}
