package com.example.mac.carwash.webservice;

/**
 * 统一返回的数据格式
 * Created by xk on 2017/7/4.
 */

public class PubData {
    private Page page;
    private String sessionId;
    private String code;
    private CusHashMap<String, Object> data;
    private String orderId;
    private String signedOrderStr;
    private String msg;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getSignedOrderStr() {
        return signedOrderStr;
    }

    public void setSignedOrderStr(String signedOrderStr) {
        this.signedOrderStr = signedOrderStr;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public CusHashMap<String, Object> getData() {
        return data;
    }

    public void setData(CusHashMap<String, Object> data) {
        this.data = data;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
}
