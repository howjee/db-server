package com.rh.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class MoneyHistory {
    public enum Type{
        RECHARGE(0), WITHDRAW(1), MARGIN(2), DELAY_FEE(3), OPEN_FEE(4), PROFIT(5), PRESENT(6);
        private int value;

        // 构造方法
        private Type(int val) {
            this.value = val;
        }

        public int getValue() {
            return value;
        }
    };

    private int id;
    private int user_id;
    private int type;
    private double value;
    private double remain;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date create_time;
    private String detail;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public double getRemain() {
        return remain;
    }

    public void setRemain(double remain) {
        this.remain = remain;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
