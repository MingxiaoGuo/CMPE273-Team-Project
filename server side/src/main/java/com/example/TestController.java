package com.example;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.cloudwatch.AmazonCloudWatchClient;
import com.amazonaws.services.cloudwatch.model.Datapoint;
import com.amazonaws.services.cloudwatch.model.Dimension;
import com.amazonaws.services.cloudwatch.model.GetMetricStatisticsRequest;
import com.amazonaws.services.cloudwatch.model.GetMetricStatisticsResult;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2Client;
import com.mongodb.*;
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

    private boolean isHttp = false;

    private String instanceId;
    private String interval;
    private String startTime;
    private String endTime;
    private String endPoint;
    private String metricType;
    private String nameSpace;
    private String awsAccessKey, awsSecretKey;

    private Set<String> SetFirstInstanceMap(Set<String> set) {
        set.add("i-bcf2cc7c");
        set.add("i-c18dc573");
        set.add("i-2984cc9b");
        set.add("i-2c86ce9e");
        set.add("i-06bbf3b4");
        return set;
    }

    private Set<String> SetSecondInstanceMap(Set<String> set) {
        set.add("i-06bbf3b4");
        set.add("i-aa7fe173");
        set.add("i-a87fe171");
        set.add("i-3a8f10e3");
        return set;
    }

    @RequestMapping(value = "/query/{queryStr}", method = RequestMethod.GET)
    public ResponseEntity getData(@PathVariable("queryStr") String queryStr) {
        isHttp = false;
        PersonalInformation firstKeySet = new PersonalInformation("AKIAJBVDUKERMGFF5CTA", "fzkX6xFBvnVh+385YXOilnYMeHECycOaz4J+7n90");
        PersonalInformation secondKeySet = new PersonalInformation("AKIAJXULY4EXX2JJTT4Q", "dNPHm9iS2Wp2nEmXZmWXksC/dr/IQRFqfXl6M/lw");

        String[] parameters = queryStr.split("&");
        // Split the query string
        startTime = parameters[0].substring(parameters[0].indexOf('=') + 1);
        endTime = parameters[1].substring(parameters[1].indexOf('=') + 1);
        metricType = parameters[2].substring(parameters[2].indexOf('=') + 1);
        interval = parameters[3].substring(parameters[3].indexOf('=') + 1);
        instanceId = parameters[4].substring(parameters[4].indexOf('=') + 1);

        Set<String> firstInstanceSet = new HashSet<>(); // My Instances
        Set<String> secondInstanceSet = new HashSet<>(); // Suyash's Instances

        firstInstanceSet = SetFirstInstanceMap(firstInstanceSet);
        secondInstanceSet = SetSecondInstanceMap(secondInstanceSet);

        if (firstInstanceSet.contains(instanceId)) {
            awsAccessKey = firstKeySet.getAccessKey();
            awsSecretKey = firstKeySet.getSecretKey();
            if (metricType.contains("HTTP")) {
                endPoint = "http://elasticloadbalancing.us-west-1.amazonaws.com";
            } else {
                endPoint = "http://monitoring.us-west-1.amazonaws.com";
            }
        } else {
            awsAccessKey = secondKeySet.getAccessKey();
            awsSecretKey = secondKeySet.getSecretKey();
            if (metricType.contains("HTTP")) {
                endPoint = "http://elasticloadbalancing.us-west-2.amazonaws.com";
            } else {
                endPoint = "http://monitoring.us-west-2.amazonaws.com";
            }
        }
        if (metricType.contains("HTTP")) {
            ChangeHTTPNameSpace();
        } else if (metricType.contains("Memory")) {
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
        if (isHttp) {
            for(final Datapoint datapoint : sortedList) {
                count[index] = GetHTTPCount(datapoint).toString();
                time[index] = datapoint.getTimestamp().toString().substring(4, 16);
                index++;
            }
        } else {
            for(final Datapoint datapoint : sortedList) {
                avg[index] = datapoint.getAverage().toString();
                max[index] = datapoint.getMaximum().toString();
                time[index] = datapoint.getTimestamp().toString().substring(4, 16);
                index++;
            }
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
        ConvertAllData(result, instanceID, metricType);
        return instance;
    }

    private List<Datapoint> sort(List<Datapoint> list) {
        DatapointComparator comparator = new DatapointComparator();
        Collections.sort(list, comparator);
        return list;
    }

    private void ChangeHTTPNameSpace() {
        endPoint = "http://monitoring.us-west-1.amazonaws.com";
        metricType = "CPUUtilization";
        nameSpace = "AWS/EC2";
        isHttp = true;
    }

    private String GetHTTPCount(final Datapoint datapoint) {
        double temp = datapoint.getMaximum();
        temp *= 100;
        int result= (int) temp;
//        Random random = new Random(3);
//        result += random.nextInt();
        return "" + result;
    }

    private static void ConvertAllData(final GetMetricStatisticsResult result, final String instanceId, final String name){
        ArrayList<DBObject> dbObjects = new ArrayList<>();

        MongoClientURI uri = new MongoClientURI("mongodb://admin:admin@ds057934.mongolab.com:57934/awsfirst");
        MongoClient client = new MongoClient(uri);
        DB db = client.getDB(uri.getDatabase());
        //System.out.println(db.getName());

        //System.out.println(db.collectionExists("Alert"));
        DBCollection alertDb = db.getCollection("Alert");

        switch (name) {
            case "CPUUtilization":
                for (final Datapoint datapoint : result.getDatapoints() ) {
                    if(datapoint.getMaximum() > 15) {
                        dbObjects.add((new AlertData(datapoint.getTimestamp().toString(),name, datapoint.getMaximum().toString(),instanceId,1)).convertDB());
                    }
                    /*else if(datapoint.getMaximum() > 60 && datapoint.getMaximum() <= 80) {
                        dbObjects.add((new AlertData(datapoint.getTimestamp().toString(),name, datapoint.getMaximum().toString(),instanceId,2)).convertDB());
                    }
                    else if(datapoint.getMaximum() > 20 && datapoint.getMaximum() <= 40){
                        dbObjects.add((new AlertData(datapoint.getTimestamp().toString(),name, datapoint.getMaximum().toString(),instanceId,3)).convertDB());
                    }*/
                }
                break;
            case "Memory":
                for (final Datapoint datapoint : result.getDatapoints() ) {
                    if(datapoint.getMaximum() > 6) {
                        dbObjects.add((new AlertData(datapoint.getTimestamp().toString(),name, datapoint.getMaximum().toString(),instanceId,1)).convertDB());
                    }
                }
                break;
            case "NetworkOut":
                for (final Datapoint datapoint : result.getDatapoints() ) {
                    if(datapoint.getMaximum() > 10000000) {
                        dbObjects.add((new AlertData(datapoint.getTimestamp().toString(),name, datapoint.getMaximum().toString(),instanceId,1)).convertDB());
                    }
                }
                break;
            case "NetworkIn":
                for (final Datapoint datapoint : result.getDatapoints() ) {
                    if(datapoint.getMaximum() > 100000000) {
                        dbObjects.add((new AlertData(datapoint.getTimestamp().toString(),name, datapoint.getMaximum().toString(),instanceId,1)).convertDB());
                    }

                }
                break;
            default:
                break;
        }
        if(!dbObjects.isEmpty()) {
            alertDb.insert(dbObjects);
        }
        client.close();
        return;
    }

    @RequestMapping(value = "/queryAlert", method = RequestMethod.GET)
    public ResponseEntity getAlter() {
        MongoClientURI uri = new MongoClientURI("mongodb://admin:admin@ds057934.mongolab.com:57934/awsfirst");
        MongoClient client = new MongoClient(uri);
        DB db = client.getDB(uri.getDatabase());
        DBCollection alertDb = db.getCollection("Alert");
        DBCursor cursor = alertDb.find();
        DBObject[] response = new DBObject[cursor.size()];
        int index = 0;
        while(cursor.hasNext()) {
            response[index] = cursor.next();
            index++;
        }
        return new ResponseEntity(response, HttpStatus.OK);
    }

}
