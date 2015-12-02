package com.example;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.cloudwatch.AmazonCloudWatchClient;
import com.amazonaws.services.cloudwatch.model.Datapoint;
import com.amazonaws.services.cloudwatch.model.Dimension;
import com.amazonaws.services.cloudwatch.model.GetMetricStatisticsRequest;
import com.amazonaws.services.cloudwatch.model.GetMetricStatisticsResult;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2Client;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.xml.crypto.Data;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Marshall
 */
@CrossOrigin
@Controller
@RequestMapping("/ec2")
public class TestController {

    @RequestMapping(value = "/query/{queryStr}", method = RequestMethod.GET)
    public ResponseEntity getData(@PathVariable("queryStr") String queryStr) {
        PersonalInformation firstKeySet = new PersonalInformation("AKIAJBVDUKERMGFF5CTA", "fzkX6xFBvnVh+385YXOilnYMeHECycOaz4J+7n90");
        PersonalInformation secondKeySet = new PersonalInformation("AKIAJXULY4EXX2JJTT4Q", "dNPHm9iS2Wp2nEmXZmWXksC/dr/IQRFqfXl6M/lw");

        String[] parameters = queryStr.split("&");
        // Split the query string
        final String startTime = parameters[0].substring(parameters[0].indexOf('=') + 1);
        final String endTime = parameters[1].substring(parameters[1].indexOf('=') + 1);
        final String metricType = parameters[2].substring(parameters[2].indexOf('=') + 1);
        final String interval = parameters[3].substring(parameters[3].indexOf('=') + 1);
        final String instanceId = parameters[4].substring(parameters[4].indexOf('=') + 1);

        Set<String> firstInstanceSet = new HashSet<>(); // My Instances
        Set<String> secondInstanceSet = new HashSet<>(); // Suyash's Instances

        firstInstanceSet = SetFirstInstanceMap(firstInstanceSet);
        secondInstanceSet = SetSecondInstanceMap(secondInstanceSet);

        String nameSpace = "";
        String awsAccessKey, awsSecretKey, endPoint;
        if (firstInstanceSet.contains(instanceId)) {
            awsAccessKey = firstKeySet.getAccessKey();
            awsSecretKey = firstKeySet.getSecretKey();
            if (metricType.contains("HTTP")) {
                endPoint = ""; // TODO
            } else {
                endPoint = "http://monitoring.us-west-1.amazonaws.com";
            }
        } else {
            awsAccessKey = secondKeySet.getAccessKey();
            awsSecretKey = secondKeySet.getSecretKey();
            if (metricType.contains("HTTP")) {
                endPoint = ""; // TODO
            } else {
                endPoint = "http://monitoring.us-west-2.amazonaws.com";
            }
        }

        if (metricType.contains("Memory")) {
            nameSpace = "System/Linux";
        } else {
            nameSpace = "AWS/EC2";
        }
        final AmazonCloudWatchClient client = client(awsAccessKey, awsSecretKey, endPoint);
        final GetMetricStatisticsRequest request1 = request(nameSpace, instanceId, metricType,startTime, endTime, interval);
        final GetMetricStatisticsResult result1 = result(client, request1);
        System.out.println(queryStr);
        return new ResponseEntity(Convert(result1, instanceId, metricType), HttpStatus.OK);
    }

    private Set<String> SetFirstInstanceMap(Set<String> set) {
        set.add("i-bcf2cc7c");
        set.add("i-c18dc573");
        set.add("i-2984cc9b");
        set.add("i-2c86ce9e");
        set.add("i-06bbf3b4");
        return set;
    }

    private Set<String> SetSecondInstanceMap(Set<String> set) {
        set.add("i-a97fe170");
        set.add("i-aa7fe173");
        set.add("i-a87fe171");
        set.add("i-3a8f10e3");
        return set;
    }

    /**
     * Use the right access key to fetch the amazon cloud watch client
     * @param awsAccessKey
     * @param awsSecretKey
     * @return
     */
    private AmazonCloudWatchClient client(final String awsAccessKey, final String awsSecretKey, final String endPoint) {
        final AmazonCloudWatchClient client = new AmazonCloudWatchClient(
                new BasicAWSCredentials(awsAccessKey, awsSecretKey));
        client.setEndpoint(endPoint);
        return client;
    }

    private GetMetricStatisticsRequest request(final String nameSpace, final String instanceId, final String metricName,final String startTime, final String endTime, final String interval) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date startDate = sdf.parse(startTime);
            Date endDate = sdf.parse(endTime);
            final int timeInterval = Integer.parseInt(interval);
            return new GetMetricStatisticsRequest()
                    .withStartTime(startDate)
                    .withNamespace(nameSpace)
                    .withPeriod(timeInterval)
                    .withDimensions(new Dimension().withName("InstanceId").withValue(instanceId))
                    .withMetricName(metricName)
                    .withStatistics("Average", "Maximum")
                    .withEndTime(endDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;

//        final long twentyFourHrs = 1000 * 60 * 60 * 24;
//        final int timeInterval = Integer.parseInt(interval);
//        return new GetMetricStatisticsRequest().withStartTime(new Date(new Date().getTime() - twentyFourHrs))
//                .withNamespace(nameSpace).withPeriod(timeInterval)
//                .withDimensions(new Dimension().withName("InstanceId").withValue(instanceId)).withMetricName(metricName)
//                .withStatistics("Average", "Maximum").withEndTime(new Date());
    }

    /**
     *
     * @param client
     * @param request
     * @return
     */
    private GetMetricStatisticsResult result(final AmazonCloudWatchClient client, final GetMetricStatisticsRequest request) {
        return client.getMetricStatistics(request);
    }

    /**
     *
     * @param result The result of client request
     * @param instanceID instance id from client side
     * @return Self-defined instance with all data stored
     */
    private Instance Convert(GetMetricStatisticsResult result, String instanceID, String metricType){
        Instance instance = new Instance(instanceID);
        if (result == null || result.getDatapoints() == null || result.getDatapoints().size() == 0) {
            return instance;
        }
        int lengthOfResult = result.getDatapoints().size();
        // Initialize all data structures
        String[] avg = new String[lengthOfResult];
        String[] max = new String[lengthOfResult];
        String[] count = new String[lengthOfResult];    // Some metrics doesn't have this data
        String[] time = new String[lengthOfResult];
        int index = 0;  // Keeping track of every datapoint
        List<Datapoint> sortedList = sort(result.getDatapoints());
        for (final Datapoint datapoint : sortedList) {
            avg[index] = datapoint.getAverage().toString();
            max[index] = datapoint.getMaximum().toString();
            //count[index] = datapoint.getSum().toString();
            time[index] = datapoint.getTimestamp().toString().substring(4, 16);
            index++;
        }
        String[] newTime = new String[lengthOfResult];
        // If length greater than 7, timestamps should be divided into 7 parts
        for (int i = 1; i < lengthOfResult; i++) {
            newTime[i] = "";
        }
        for (int i = 0; i < lengthOfResult; i+= lengthOfResult / 6 ) {
            newTime[i] = time[i];
        }
        //newTime[lengthOfResult -1] = time[lengthOfResult - 1];
        // Put data into instance
        instance.setTimeframe(newTime);
        instance.setAvg(avg);
        instance.setMax(max);
        instance.setCount(count);
        instance.setUnit(result.getDatapoints().get(0).getUnit());
        instance.setType(metricType);
        return instance;
    }

    private List<Datapoint> sort(List<Datapoint> list) {
        DatapointComparator comparator = new DatapointComparator();
        Collections.sort(list, comparator);
        return list;
    }

}
