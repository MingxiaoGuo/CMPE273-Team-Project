package com.example;

import com.amazonaws.services.cloudwatch.model.Datapoint;

import java.util.Comparator;

/**
 * Created by Marshall on 2015/12/1.
 */
public class DatapointComparator implements Comparator<Datapoint> {
    @Override
    public int compare(Datapoint first, Datapoint second) {
        if (first.getTimestamp().after(second.getTimestamp())){
            return 1;
        }
        return -1;
    }
}
