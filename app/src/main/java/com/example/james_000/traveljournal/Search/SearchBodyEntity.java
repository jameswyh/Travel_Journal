package com.example.james_000.traveljournal.Search;


public class SearchBodyEntity {
    private String ret_code;

    public String getRet_code() {
        return ret_code;
    }

    public void setRet_code(String ret_code) {
        this.ret_code = ret_code;
    }


    public SearchBeanEntity getPagebean() {
        return pagebean;
    }

    public void setPagebean(SearchBeanEntity pagebean) {
        this.pagebean = pagebean;
    }

    SearchBeanEntity pagebean;
}
