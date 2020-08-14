package com.example.james_000.traveljournal.Search;


public class SearchEntity {
    private String showapi_res_code;
    private String showapi_res_error;

    public String getShowapi_res_code() {
        return showapi_res_code;
    }

    public void setShowapi_res_code(String showapi_res_code) {
        this.showapi_res_code = showapi_res_code;
    }

    public String getShowapi_res_error() {
        return showapi_res_error;
    }

    public void setShowapi_res_error(String showapi_res_error) {
        this.showapi_res_error = showapi_res_error;
    }

    public SearchBodyEntity getShowapi_res_body() {
        return showapi_res_body;
    }

    public void setShowapi_res_body(SearchBodyEntity showapi_res_body) {
        this.showapi_res_body = showapi_res_body;
    }

    SearchBodyEntity showapi_res_body;
}
