package com.example.james_000.traveljournal.Search;

import java.util.List;


public class SearchBeanEntity {


    private String allPages;

    public List<SearchListEntity> getContentlist() {
        return contentlist;
    }

    public void setContentlist(List<SearchListEntity> contentlist) {
        this.contentlist = contentlist;
    }

    List<SearchListEntity> contentlist;
    private String currentPage;
    private String allNum;
    private String maxResult;

    public String getAllPages() {
        return allPages;
    }

    public void setAllPages(String allPages) {
        this.allPages = allPages;
    }





    public String getMaxResult() {
        return maxResult;
    }

    public void setMaxResult(String maxResult) {
        this.maxResult = maxResult;
    }

    public String getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(String currentPage) {
        this.currentPage = currentPage;
    }

    public String getAllNum() {
        return allNum;
    }

    public void setAllNum(String allNum) {
        this.allNum = allNum;
    }

}
