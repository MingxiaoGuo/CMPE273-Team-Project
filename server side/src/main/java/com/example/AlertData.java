package com.example;

import com.mongodb.BasicDBObject;

/**
 * Created by hongbotian on 12/2/15.
 */
public class AlertData {
    String timeStamp;
    String type;
    String threshold;
    String instanceId;
    int    alertType;
    public AlertData(String timeStamp,String Type,String threshold,String instanceId,int alertType){
        this.timeStamp = timeStamp;
        this.type = Type;
        this.threshold = threshold;
        this.instanceId = instanceId;
        this.alertType = alertType;
    }

    public BasicDBObject convertDB() {
        BasicDBObject dbObject = new BasicDBObject();
        dbObject.put("InstanceId", this.instanceId);
        dbObject.put("Timestamp", this.timeStamp);
        dbObject.put("Type", this.type);
        dbObject.put("Threshold", this.threshold);
        dbObject.put("AlertType", this.alertType);
        return dbObject;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public int getAlertType() {
        return alertType;
    }

    public void setAlertType(int alertType) {
        this.alertType = alertType;
    }

    public String getThreshold() {
        return threshold;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        type = type;
    }

    public void setThreshold(String threshold) {
        this.threshold = threshold;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }
}