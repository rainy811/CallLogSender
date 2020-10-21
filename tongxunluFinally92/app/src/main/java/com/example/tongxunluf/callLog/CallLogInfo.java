package com.example.tongxunluf.callLog;

import java.io.Serializable;

public class CallLogInfo implements Serializable {

    // 销售名字
    private String salesName;

    // 通话记录的联系人
    private String name;

    // 通话记录的电话号码
    private String number;

    // 通话记录的日期
    private String date;

    // 通话时长
    private int duration;

    // 通话类型
    private int type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getSalesName() {
        return salesName;
    }

    public void setSalesName(String salesName) {
        this.salesName = salesName;
    }

    @Override
    public String toString() {
        return "CallLogInfo{" +
                "salesName='" + salesName + '\'' +
                ", name='" + name + '\'' +
                ", number='" + number + '\'' +
                ", date='" + date + '\'' +
                ", duration=" + duration +
                ", type=" + type +
                '}';
    }
}
