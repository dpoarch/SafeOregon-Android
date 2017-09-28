package com.etech.bean;

import java.io.Serializable;

/**
 * Created by etech8 on 1/1/16.
 */
public class DbMyTips implements Serializable {

    public String tips_id;
    public String tips_number;
    public String tips_date;

    public String getTips_id() {
        return tips_id;
    }

    public void setTips_id(String tips_id) {
        this.tips_id = tips_id;
    }

    public String getTips_date() {
        return tips_date;
    }

    public void setTips_date(String tips_date) {
        this.tips_date = tips_date;
    }

    public String getTips_number() {
        return tips_number;
    }

    public void setTips_number(String tips_number) {
        this.tips_number = tips_number;
    }
}
