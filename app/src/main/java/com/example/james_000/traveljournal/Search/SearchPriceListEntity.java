package com.example.james_000.traveljournal.Search;

import java.util.List;


public class SearchPriceListEntity {
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<EntityListEntity> getEntityList() {
        return entityList;
    }

    public void setEntityList(List<EntityListEntity> entityList) {
        this.entityList = entityList;
    }

    private String type;
    List<EntityListEntity> entityList;
}
