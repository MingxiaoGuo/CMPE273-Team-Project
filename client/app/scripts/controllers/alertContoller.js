'use strict';
/**
 * @ngdoc function
 * @name sbAdminApp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of the sbAdminApp
 */
var app = angular.module('sbAdminApp');
  app.controller('AlertCtrl', function ($scope, $http) {
    $http.get("http://10.189.97.101:8080/ec2/queryAlert")
    .then(function(response) {$scope.data = response.data;});
    //$http.get("http://www.w3schools.com/angular/customers.php")
    //.then(function(response) {$scope.data = response.data.records;});
});

