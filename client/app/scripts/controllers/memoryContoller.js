'use strict';
/**
 * @ngdoc function
 * @name sbAdminApp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of the sbAdminApp
 */
var app = angular.module('sbAdminApp');
  app.controller('MemoryCtrl', ['$scope', '$timeout', '$http', '$filter',function ($scope, $timeout, $http, $filter) {
 
    var now = new Date();
    var bef = new Date(now-24*60*60*1000);
    $scope.startdata = $filter('date')(bef, "yyyy-MM-dd");
    $scope.enddata = $filter('date')(now, "yyyy-MM-dd");
    $scope.timeinterval = 60;
    $scope.d = $filter('date')(now, "yyyy-MM-dd");
    var urlbase = "http://10.189.97.101:8080/ec2/query/"+"start="+$scope.startdata+"&end="+$scope.enddata+"&type=MemoryUtilization&interval=60";
 
    $scope.line1 = {
        labels: [],   
        series: ['Average','Maximun'],
        data: [],
    };
    //var url = "http://localhost:8080/api/data";
    var url = urlbase+"&instanceid=i-06bbf3b4";
    $scope.p = url;
    $http.get(url)
    .then(function(response) {
        $scope.datas = response.data;
        $scope.line1.data[0] = response.data.avg;
        $scope.line1.data[1] = response.data.max;
        $scope.line1.labels = response.data.timeframe;
    });


    $scope.line2 = {
        labels: [],   
        series: ['Average','Maximun'],
        data: [],
    };

    var url = urlbase+"&instanceid=i-aa7fe173";
    $http.get(url)
    .then(function(response) {
        $scope.datas = response.data;
        $scope.line2.data[0] = [9.2, 9.4, 10.2, 10.0, 10.1, 9.8, 9.5];
        $scope.line2.data[1] = [9.2, 9.4, 10.2, 10.0, 10.1, 9.8, 9.5];
        $scope.line2.labels = ["Dec 01 15:23", "Dec 01 16:23", "Dec 01 17:23", "Dec 01 18:23", "Dec 01 19:23", "Dec 01 20:23", "Dec 01 21:23"];
    });

    $scope.line3 = {
        labels: [],   
        series: ['Average','Maximun'],
        data: [],
    };

    var url = urlbase+"&instanceid=i-a87fe171";
    $http.get(url)
    .then(function(response) {
        $scope.datas = response.data;
        $scope.line3.data[0] = [6.2, 6.8, 6.2, 7.6, 6.7, 6.5, 6.5];
        $scope.line3.data[1] = [6.2, 6.8, 6.2, 7.6, 6.7, 6.5, 6.5];
        $scope.line3.labels = ["Dec 01 10:11", "Dec 01 11:11", "Dec 01 12:11", "Dec 01 13:11", "Dec 01 14:11", "Dec 01 15:11", "Dec 01 16:11"];
    });

    $scope.line4 = {
        labels: [],   
        series: ['Average','Maximun'],
        data: [],
    };

    var url = urlbase+"&instanceid=i-3a8f10e3";
    $http.get(url)
    .then(function(response) {
        $scope.datas = response.data;
        $scope.line4.data[0] = response.data.avg;
        $scope.line4.data[1] = response.data.max;
        $scope.line4.labels = response.data.timeframe;
    });


    $scope.query = function(){

        var urlbase = "http://10.189.97.101:8080/ec2/query/"+"start="+$scope.startdata+"&end="+$scope.enddata+"&type=MemoryUtilization&interval=60";

        $scope.line1 = {
            labels: [],   
            series: ['Average','Maximun'],
            data: [],
        };
        //var url = "http://localhost:8080/api/data";
        var url = urlbase+"&instanceid=i-06bbf3b4";
        $scope.p = url;
        $http.get(url)
        .then(function(response) {
            $scope.datas = response.data;
            $scope.line1.data[0] = response.data.avg;
            $scope.line1.data[1] = response.data.max;
            $scope.line1.labels = response.data.timeframe;
        });

        $scope.line2 = {
            labels: [],   
            series: ['Average','Maximun'],
            data: [],
        };
        //var url = "http://localhost:8080/api/data";
        var url = urlbase+"&instanceid=i-aa7fe173";
        $http.get(url)
        .then(function(response) {
            $scope.datas = response.data;
            $scope.line2.data[0] = [9.2, 9.4, 10.2, 10.0, 10.1, 9.8, 9.5];
            $scope.line2.data[1] = [9.2, 9.4, 10.2, 10.0, 10.1, 9.8, 9.5];
            $scope.line2.labels = ["Dec 01 15:23", "Dec 01 16:23", "Dec 01 17:23", "Dec 01 18:23", "Dec 01 19:23", "Dec 01 20:23", "Dec 01 21:23"];
        });

        $scope.line3 = {
            labels: [],   
            series: ['Average','Maximun'],
            data: [],
        };
        //var url = "http://localhost:8080/api/data";
        var url = urlbase+"&instanceid=i-a87fe171";
        $http.get(url)
        .then(function(response) {
            $scope.datas = response.data;
            $scope.line3.data[0] = [6.2, 6.8, 6.2, 7.6, 6.7, 6.5, 6.5];
            $scope.line3.data[1] = [6.2, 6.8, 6.2, 7.6, 6.7, 6.5, 6.5];
            $scope.line3.labels = ["Dec 01 10:11", "Dec 01 11:11", "Dec 01 12:11", "Dec 01 13:11", "Dec 01 14:11", "Dec 01 15:11", "Dec 01 16:11"];
        });

        $scope.line4 = {
            labels: [],   
            series: ['Average','Maximun'],
            data: [],
        };
        //var url = "http://localhost:8080/api/data";
        var url = urlbase+"&instanceid=i-3a8f10e3";
        $http.get(url)
        .then(function(response) {
            $scope.datas = response.data;
            $scope.line4.data[0] = response.data.avg;
            $scope.line4.data[1] = response.data.max;
            $scope.line4.labels = response.data.timeframe;
        });
    }
}]);