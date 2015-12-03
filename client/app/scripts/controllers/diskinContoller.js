'use strict';
/**
 * @ngdoc function
 * @name sbAdminApp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of the sbAdminApp
 */
var app = angular.module('sbAdminApp');
  app.controller('DiskInCtrl', ['$scope', '$timeout', '$http', '$filter',function ($scope, $timeout, $http, $filter) {
 
    var now = new Date();
    var bef = new Date(now-24*60*60*1000);
    $scope.startdata = $filter('date')(bef, "yyyy-MM-dd");
    $scope.enddata = $filter('date')(now, "yyyy-MM-dd");
    $scope.timeinterval = 3600;
    $scope.d = $filter('date')(now, "yyyy-MM-dd");
    var urlbase = "http://10.189.97.101:8080/ec2/query/"+"start="+$scope.startdata+"&end="+$scope.enddata+"&type=DiskReadBytes&interval="+$scope.timeinterval;
 
    $scope.line1 = {
        labels: [],   
        series: ['Average','Maximun'],
        data: [],
    };
    //var url = "http://localhost:8080/api/data";
    var url = urlbase+"&instanceid=i-bcf2cc7c";
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

    var url = urlbase+"&instanceid=i-c18dc573";
    $http.get(url)
    .then(function(response) {
        $scope.datas = response.data;
        $scope.line2.data[0] = response.data.avg;
        $scope.line2.data[1] = response.data.max;
        $scope.line2.labels = response.data.timeframe;
    });

    $scope.line3 = {
        labels: [],   
        series: ['Average','Maximun'],
        data: [],
    };

     var url = urlbase+"&instanceid=i-2984cc9b";
    $http.get(url)
    .then(function(response) {
        $scope.datas = response.data;
        $scope.line3.data[0] = response.data.avg;
        $scope.line3.data[1] = response.data.max;
        $scope.line3.labels = response.data.timeframe;
    });

    $scope.line4 = {
        labels: [],   
        series: ['Average','Maximun'],
        data: [],
    };

    var url = urlbase+"&instanceid=i-2c86ce9e";
    $http.get(url)
    .then(function(response) {
        $scope.datas = response.data;
        $scope.line4.data[0] = response.data.avg;
        $scope.line4.data[1] = response.data.max;
        $scope.line4.labels = response.data.timeframe;
    });


    $scope.query = function(){

        var urlbase = "http://10.189.97.101:8080/ec2/query/"+"start="+$scope.startdata+"&end="+$scope.enddata+"&type=DiskReadBytes&interval="+$scope.timeinterval;

        $scope.line1 = {
            labels: [],   
            series: ['Average','Maximun'],
            data: [],
        };
        //var url = "http://localhost:8080/api/data";
        var url = urlbase+"&instanceid=i-bcf2cc7c";
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
        var url = urlbase+"&instanceid=i-c18dc573";
        $http.get(url)
        .then(function(response) {
            $scope.datas = response.data;
            $scope.line2.data[0] = response.data.avg;
            $scope.line2.data[1] = response.data.max;
            $scope.line2.labels = response.data.timeframe;
        });

        $scope.line3 = {
            labels: [],   
            series: ['Average','Maximun'],
            data: [],
        };
        //var url = "http://localhost:8080/api/data";
        var url = urlbase+"&instanceid=i-2984cc9b";
        $http.get(url)
        .then(function(response) {
            $scope.datas = response.data;
            $scope.line3.data[0] = response.data.avg;
            $scope.line3.data[1] = response.data.max;
            $scope.line3.labels = response.data.timeframe;
        });

        $scope.line4 = {
            labels: [],   
            series: ['Average','Maximun'],
            data: [],
        };
        //var url = "http://localhost:8080/api/data";
        var url = urlbase+"&instanceid=i-2c86ce9e";
        $http.get(url)
        .then(function(response) {
            $scope.datas = response.data;
            $scope.line4.data[0] = response.data.avg;
            $scope.line4.data[1] = response.data.max;
            $scope.line4.labels = response.data.timeframe;
        });
    }
}]);