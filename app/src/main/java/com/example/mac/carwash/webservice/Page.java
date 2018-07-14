package com.example.mac.carwash.webservice;

/**
 * Created by xk on 2017/7/4.
 */

public class Page {
    private String allRecordCount;
    private String currentPage;
    private String endRecord;
    private String firstPage;
    private boolean hasNextPage;
    private boolean hasPrePage;
    private String lastPage;
    private String nextPage;
    private String pageCount;
    private String pageRecordCount;
    private String prePage;
    private boolean refreshAllRecordCount;

    public String getAllRecordCount() {
        return allRecordCount;
    }

    public void setAllRecordCount(String allRecordCount) {
        this.allRecordCount = allRecordCount;
    }

    public String getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(String currentPage) {
        this.currentPage = currentPage;
    }

    public String getEndRecord() {
        return endRecord;
    }

    public void setEndRecord(String endRecord) {
        this.endRecord = endRecord;
    }

    public String getFirstPage() {
        return firstPage;
    }

    public void setFirstPage(String firstPage) {
        this.firstPage = firstPage;
    }

    public boolean isHasNextPage() {
        return hasNextPage;
    }

    public void setHasNextPage(boolean hasNextPage) {
        this.hasNextPage = hasNextPage;
    }

    public boolean isHasPrePage() {
        return hasPrePage;
    }

    public void setHasPrePage(boolean hasPrePage) {
        this.hasPrePage = hasPrePage;
    }

    public String getLastPage() {
        return lastPage;
    }

    public void setLastPage(String lastPage) {
        this.lastPage = lastPage;
    }

    public String getNextPage() {
        return nextPage;
    }

    public void setNextPage(String nextPage) {
        this.nextPage = nextPage;
    }

    public String getPageCount() {
        return pageCount;
    }

    public void setPageCount(String pageCount) {
        this.pageCount = pageCount;
    }

    public String getPageRecordCount() {
        return pageRecordCount;
    }

    public void setPageRecordCount(String pageRecordCount) {
        this.pageRecordCount = pageRecordCount;
    }

    public String getPrePage() {
        return prePage;
    }

    public void setPrePage(String prePage) {
        this.prePage = prePage;
    }

    public boolean isRefreshAllRecordCount() {
        return refreshAllRecordCount;
    }

    public void setRefreshAllRecordCount(boolean refreshAllRecordCount) {
        this.refreshAllRecordCount = refreshAllRecordCount;
    }

    public String getStartRecord() {
        return startRecord;
    }

    public void setStartRecord(String startRecord) {
        this.startRecord = startRecord;
    }

    private String startRecord;
}
