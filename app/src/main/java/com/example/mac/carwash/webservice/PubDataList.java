package com.example.mac.carwash.webservice;

import java.util.List;
import java.util.Map;

/**
 * Created by xk on 2017/7/5.
 */

public class PubDataList {
    private Page page;
    private String code;
    private List<CusHashMap<String, Object>> data;
    private Map<String, Object> sendMap;

    public Map<String, Object> getSendMap() {
        return sendMap;
    }

    public void setSendMap(Map<String, Object> sendMap) {
        this.sendMap = sendMap;
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

    public List<CusHashMap<String, Object>> getData() {
        return data;
    }

    public void setData(List<CusHashMap<String, Object>> data) {
        this.data = data;
    }
}
