package com.example.mac.carwash.jsonBean;
import java.util.List;

public class OrderInfoBean {
    private String code;
    private List<Data> data;
    private String page;
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

    public void setPage(String page) {
        this.page = page;
    }
    public String getPage() {
        return page;
    }

    public class Data {

        private String head;
        private String fee;
        private String carmark;
        private int ISSETTLEMENT;
        private String NAME;
        private String vip;
        private String TIME;
        private int id;
        private int rownum_;
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

        public void setISSETTLEMENT(int ISSETTLEMENT) {
            this.ISSETTLEMENT = ISSETTLEMENT;
        }
        public int getISSETTLEMENT() {
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

        public void setId(int id) {
            this.id = id;
        }
        public int getId() {
            return id;
        }

        public void setRownum_(int rownum_) {
            this.rownum_ = rownum_;
        }
        public int getRownum_() {
            return rownum_;
        }

    }
}