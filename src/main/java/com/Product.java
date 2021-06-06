package com;

import java.util.List;

public class Product {

    String title = "";
    String des = "";
    List<Param> paramsList ;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public List<Param> getParamsList() {
        return paramsList;
    }

    public void setParamsList(List<Param> paramsList) {
        this.paramsList = paramsList;
    }

    public Product(String title, String des, List<Param> paramsList) {
        this.title = title;
        this.des = des;
        this.paramsList = paramsList;
    }
}
