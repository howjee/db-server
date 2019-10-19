package com.rh.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class User {
    public enum Permission {
        USER(0), MANAGER(1), ROOT(2);
        private int value;

        // 构造方法
        private Permission(int val) {
            this.value = val;
        }

        public int getValue() {
            return value;
        }
    }

    private int id;
    private String nickname;
    private String avatar;
    private double money;
    private int permission;

    @Override
    public String toString() {
        return "User{" + "id=" + id + ", nickname='" + nickname + '\'' + ", avatar='" + avatar + '\'' + ", create_time=" + create_time + '}';
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date create_time;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }

    public int getPermission() {
        return permission;
    }

    public void setPermission(int permission) {
        this.permission = permission;
    }
}
