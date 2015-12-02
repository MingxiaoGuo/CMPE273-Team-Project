package com.example;

import java.util.Comparator;
import java.util.Date;

/**
 * Created by hongbotian on 11/30/15.
 */
public class Instance {
    String[] avg;
    String[] max;
    String[] count;
    String unit;
    String[] timeframe;
    String id;
    String type;
    public Instance(String id){
//        this.avg = new float[]{2,10,10,3,4,5,6,6,7};
//        this.max = new float[]{1,9,9,2,3,4,5,5,6};
//        this.unit = "";
//        this.type = "cpu";
//        this.timeframe = new String[]{"10:00","11:00","12:00","13:00","14:00","15:00","16:00"};
//        this.id = "1";
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String[] getAvg() {
        return avg;
    }

    public void setAvg(String[] avg) {
        this.avg = avg;
    }

    public String[] getMax() {
        return max;
    }

    public void setMax(String[] max) {
        this.max = max;
    }

    public String[] getCount() {
        return count;
    }

    public void setCount(String[] count) {
        this.count = count;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String[] getTimeframe() {
        return timeframe;
    }

    public void setTimeframe(String[] timeframe) {
        this.timeframe = timeframe;
    }
}
