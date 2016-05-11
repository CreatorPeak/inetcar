package com.inetcar.tools;


import java.io.Serializable;

/**
 *网络请求结果
 */
public class MyResult implements Serializable{

    private static final long serialVersionUID = 1L;
    private int status;
    private String msg;

    public MyResult(){}

    public MyResult(int status, String msg) {
        super();
        this.status = status;
        this.msg = msg;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
