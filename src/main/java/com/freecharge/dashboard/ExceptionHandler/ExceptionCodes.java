package com.freecharge.dashboard.ExceptionHandler;

public enum ExceptionCodes {

    NO_SERVICES_FOUND("SER-ERR-1005","No services found in DB"),
    NO_METHODS_FOUND("MET-ERR-1005","No method found for given particular test classes in DB"),
    NO_CLASSES_FOUND("CL-ERR-1005","No classes found for given particular services in DB"),
    NO_PASS_PERCENTAGE_FOUND("PER-ERR-1005","Test suite is not started yet");
    private String errCode;
    private String errMsg;

    private ExceptionCodes(String errCode, String errMsg) {
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

    public String errCode() {
        return this.errCode;
    }

    public String errMsg() {
        return this.errMsg;
    }
}
