package com.example;

import java.util.Map;

/**
 * Created by Marshall on 2015/12/2.
 */
public class PersonalInformation {

    private String accessKey;
    private String secretKey;

    public PersonalInformation(String key, String sKey) {
        this.accessKey = key;
        this.secretKey = sKey;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }
}
