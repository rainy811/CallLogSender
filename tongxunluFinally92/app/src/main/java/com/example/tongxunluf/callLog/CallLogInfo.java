package com.example.tongxunluf.callLog;

import java.io.Serializable;

public class CallLogInfo implements Serializable {

    // 销售名字
    private String salesName;

    // 通话记录的电话号码
    private String number;

    // 通话记录的日期
    private String date;

    private String name;


    // 通话记录的时间
    private String time;

    // 通话时长
    private int duration;

    // 通话类型
    private String type;

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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSalesName() {
        return salesName;
    }

    public void setSalesName(String salesName) {
        this.salesName = salesName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    @Override
    public String toString() {
        return "通话日期: " + date +" "+ time + ","
                + "姓名: "  + name + ","
                + "电话号码: " + number + ","
                + "通话时长: " + duration + "s" + ","
                + "通话类型: " + type + "\r\n";
    }
}
