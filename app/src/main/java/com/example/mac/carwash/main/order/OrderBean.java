package com.example.mac.carwash.main.order;
import java.util.List;

public class OrderBean {
    private String code;
    private List<Data> data;
    private Page page;
    private String msg;
    public void setCode(String code) {
        this.code = code;
    }
    public String getCode() {
        return code;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }
    public List<Data> getData() {
        return data;
    }

    public void setPage(Page page) {
        this.page = page;
    }
    public Page getPage() {
        return page;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
    public String getMsg() {
        return msg;
    }

    public static class Data {
        private String head;
        private String fee;
        private String carmark;
        private String ISSETTLEMENT;
        private String NAME;
        private String vip;
        private String TIME;
        private String id;

        public  Data(String head, String fee, String carmark, String ISSETTLEMENT, String NAME, String vip, String TIME, String id) {
            this.head = head;
            this.fee = fee;
            this.carmark = carmark;
            this.ISSETTLEMENT = ISSETTLEMENT;
            this.NAME = NAME;
            this.vip = vip;
            this.TIME = TIME;
            this.id = id;
        }

        public void setHead(String head) {
            this.head = head;
        }
        public String getHead() {
            return head;
        }

        public void setFee(String fee) {
            this.fee = fee;
        }
        public String getFee() {
            return fee;
        }

        public void setCarmark(String carmark) {
            this.carmark = carmark;
        }
        public String getCarmark() {
            return carmark;
        }

        public void setISSETTLEMENT(String ISSETTLEMENT) {
            this.ISSETTLEMENT = ISSETTLEMENT;
        }
        public String getISSETTLEMENT() {
            return ISSETTLEMENT;
        }

        public void setNAME(String NAME) {
            this.NAME = NAME;
        }
        public String getNAME() {
            return NAME;
        }

        public void setVip(String vip) {
            this.vip = vip;
        }
        public String getVip() {
            return vip;
        }

        public void setTIME(String TIME) {
            this.TIME = TIME;
        }
        public String getTIME() {
            return TIME;
        }

        public void setId(String id) {
            this.id = id;
        }
        public String getId() {
            return id;
        }

    }

    public class Page {

        private String currentPage;
        private String pageRecordCount;
        public void setCurrentPage(String currentPage) {
            this.currentPage = currentPage;
        }
        public String getCurrentPage() {
            return currentPage;
        }

        public void setPageRecordCount(String pageRecordCount) {
            this.pageRecordCount = pageRecordCount;
        }
        public String getPageRecordCount() {
            return pageRecordCount;
        }

    }
}