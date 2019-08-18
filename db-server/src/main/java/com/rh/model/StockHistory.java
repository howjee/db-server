package com.rh.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class StockHistory {
    private int id;
    private int user_id;
    private int stock_id;
    private int operate_type;
    private float buy_price;
    private float sell_price;
    private int sell_count;
    private int remain_count;
    private int sell_mode;
    private float profit_rate;
    private float profit;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date buy_time;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date sell_time;

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

    public int getStock_id() {
        return stock_id;
    }

    public void setStock_id(int stock_id) {
        this.stock_id = stock_id;
    }

    public int getOperate_type() {
        return operate_type;
    }

    public void setOperate_type(int operate_type) {
        this.operate_type = operate_type;
    }

    public float getBuy_price() {
        return buy_price;
    }

    public void setBuy_price(float buy_price) {
        this.buy_price = buy_price;
    }

    public float getSell_price() {
        return sell_price;
    }

    public void setSell_price(float sell_price) {
        this.sell_price = sell_price;
    }

    public int getSell_count() {
        return sell_count;
    }

    public void setSell_count(int sell_count) {
        this.sell_count = sell_count;
    }

    public int getRemain_count() {
        return remain_count;
    }

    public void setRemain_count(int remain_count) {
        this.remain_count = remain_count;
    }

    public int getSell_mode() {
        return sell_mode;
    }

    public void setSell_mode(int sell_mode) {
        this.sell_mode = sell_mode;
    }

    public float getProfit_rate() {
        return profit_rate;
    }

    public void setProfit_rate(float profit_rate) {
        this.profit_rate = profit_rate;
    }

    public float getProfit() {
        return profit;
    }

    public void setProfit(float profit) {
        this.profit = profit;
    }

    public Date getBuy_time() {
        return buy_time;
    }

    public void setBuy_time(Date buy_time) {
        this.buy_time = buy_time;
    }

    public Date getSell_time() {
        return sell_time;
    }

    public void setSell_time(Date sell_time) {
        this.sell_time = sell_time;
    }
}
