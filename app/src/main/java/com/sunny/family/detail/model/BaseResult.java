package com.sunny.family.detail.model;

/**
 * @auther:libenqi
 * @date:2019/12/10
 * @email: libenqi1@le.com
 * @description:
 */
public class BaseResult {
    private int resultStatus;
    private int status;
    private String message;
    private String errorCode;

    public int getResultStatus() {
        return resultStatus;
    }

    public void setResultStatus(int resultStatus) {
        this.resultStatus = resultStatus;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
}
