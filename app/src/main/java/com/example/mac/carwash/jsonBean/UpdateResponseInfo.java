package com.example.mac.carwash.jsonBean;

import java.util.List;

/**
 * Created by mac on 2018/7/14.
 */

public class UpdateResponseInfo {
        private String code;
        private Data data;
        private String page;
        public void setCode(String code) {
            this.code = code;
        }
        public String getCode() {
            return code;
        }

        public void setData(Data data) {
            this.data = data;
        }
        public Data getData() {
            return data;
        }

        public void setPage(String page) {
            this.page = page;
        }
        public String getPage() {
            return page;
        }

    public class Resultset1 {

        private String QCODE;
        private String QVERSIONDESC;
        private String QFILEPATH;
        public void setQCODE(String QCODE) {
            this.QCODE = QCODE;
        }
        public String getQCODE() {
            return QCODE;
        }

        public void setQVERSIONDESC(String QVERSIONDESC) {
            this.QVERSIONDESC = QVERSIONDESC;
        }
        public String getQVERSIONDESC() {
            return QVERSIONDESC;
        }

        public void setQFILEPATH(String QFILEPATH) {
            this.QFILEPATH = QFILEPATH;
        }
        public String getQFILEPATH() {
            return QFILEPATH;
        }

    }

    public class Data {

        private List<Resultset1> resultset1;
        private int updatecount1;
        public void setResultset1(List<Resultset1> resultset1) {
            this.resultset1 = resultset1;
        }
        public List<Resultset1> getResultset1() {
            return resultset1;
        }

        public void setUpdatecount1(int updatecount1) {
            this.updatecount1 = updatecount1;
        }
        public int getUpdatecount1() {
            return updatecount1;
        }

        @Override
        public String toString() {
            return "Data{" +
                    "resultset1=" + resultset1 +
                    ", updatecount1=" + updatecount1 +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "UpdateResponseInfo{" +
                "code='" + code + '\'' +
                ", data=" + data +
                ", page='" + page + '\'' +
                '}';
    }
}
