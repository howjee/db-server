package com.rh.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class Order {
    public enum Status {
        UNDONE(0), DONE(1);
        private int value;

        // 构造方法
        private Status(int val) {
            this.value = val;
        }

        public int getValue() {
            return value;
        }
    }
    
    private int id;
    private int user_id;
    private String stock_number;
    private String stock_name;
    private float stock_price;
    private int strategy_mode;
    private float strategy_rate;
    private float margin;
    private float stop_loss_rate;
    private float stop_profit_rate;
    private float open_fee;
    private float delay_fee;
    private boolean auto_delay;
    private int current_count;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date create_time;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date update_time;
    private boolean is_valid;
    private int status;

    @Override
    public String toString() {
        return "Order{" + "id=" + id + ", user_id=" + user_id + ", stock_number='" + stock_number + ", stock_name='" + stock_name + ", stock_price=" + stock_price + ", strategy_mode=" + strategy_mode + ", strategy_rate=" + strategy_rate + ", margin=" + margin + ", stop_loss_rate=" + stop_loss_rate + ", stop_profit_rate=" + stop_profit_rate + ", open_fee=" + open_fee + ", delay_fee=" + delay_fee + ", auto_delay=" + auto_delay + ", current_count=" + current_count + ", create_time=" + create_time + ", update_time=" + update_time + ", is_valid=" + is_valid + ", status=" + status + '}';
    }

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

    public String getStock_number() {
        return stock_number;
    }

    public void setStock_number(String stock_number) {
        this.stock_number = stock_number;
    }

    public String getStock_name() {
        return stock_name;
    }

    public void setStock_name(String stock_name) {
        this.stock_name = stock_name;
    }

    public float getStock_price() {
        return stock_price;
    }

    public void setStock_price(float stock_price) {
        this.stock_price = stock_price;
    }

    public int getStrategy_mode() {
        return strategy_mode;
    }

    public void setStrategy_mode(int strategy_mode) {
        this.strategy_mode = strategy_mode;
    }

    public float getStrategy_rate() {
        return strategy_rate;
    }

    public void setStrategy_rate(float strategy_rate) {
        this.strategy_rate = strategy_rate;
    }

    public float getMargin() {
        return margin;
    }

    public void setMargin(float margin) {
        this.margin = margin;
    }

    public float getStop_loss_rate() {
        return stop_loss_rate;
    }

    public void setStop_loss_rate(float stop_loss_rate) {
        this.stop_loss_rate = stop_loss_rate;
    }

    public float getStop_profit_rate() {
        return stop_profit_rate;
    }

    public void setStop_profit_rate(float stop_profit_rate) {
        this.stop_profit_rate = stop_profit_rate;
    }

    public float getOpen_fee() {
        return open_fee;
    }

    public void setOpen_fee(float open_fee) {
        this.open_fee = open_fee;
    }

    public float getDelay_fee() {
        return delay_fee;
    }

    public void setDelay_fee(float delay_fee) {
        this.delay_fee = delay_fee;
    }

    public boolean isAuto_delay() {
        return auto_delay;
    }

    public void setAuto_delay(boolean auto_delay) {
        this.auto_delay = auto_delay;
    }

    public int getCurrent_count() {
        return current_count;
    }

    public void setCurrent_count(int current_count) {
        this.current_count = current_count;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }

    public Date getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(Date update_time) {
        this.update_time = update_time;
    }

    public boolean isIs_valid() {
        return is_valid;
    }

    public void setIs_valid(boolean is_valid) {
        this.is_valid = is_valid;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
