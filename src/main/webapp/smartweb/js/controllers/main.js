'use strict';

angular.module('SMARTApp.controllers')
.controller('MainCtrl', ['$scope', '$localStorage', '$http', '$interval', 'toaster', function($scope, $localStorage, $http, $interval, $toaster) {
	$scope.server = "http://54.148.17.11:8080/smartserver";
	$scope.server = "http://"+window.location.host+"/";

	$scope.debug = false;
	$scope.$root.noDialog = false;
	$scope.$storage = $localStorage.$default({
		alerts: [],
		interval: 5000
	});
	
	$scope.addAlert = function(type, msg) {
		$scope.$storage.alerts.push({type:type, msg:msg, date:Date.now()});
		$toaster.pop((type == 'danger' ? 'error' : type), "New Alert", msg, 2500);
	}
	$scope.removeAlert = function(id) {
		$scope.$storage.alerts.splice($scope.$storage.alerts.length - 1 - id, 1);
	}
	$scope.clearAlerts = function() {
		$scope.$storage.alerts = [];
	}
	
	$scope.modelingStart = function(name) {
		$http.get($scope.server + '/modeling_start?name='+name).success(function(data) {
		});
	}
	
	$scope.modelingStop = function(name) {
		$http.get($scope.server + '/modeling_stop?name='+name).success(function(data) {
		});
	}

	$scope.getModelingByName = function(name) {
		for (var i in $scope.modelings) {
			if ($scope.modelings[i].name == name)
				return $scope.modelings[i];
		}
		return (null);
	}
	
	$scope.timeout = 10000;
	
	$scope.modelingList = function(ignoreLoadingBar) {
		$http.get($scope.server + '/modeling_list', {timeout:$scope.timeout, ignoreLoadingBar: ignoreLoadingBar}).success(function(data) {
			$scope.modelings = data.data.modelings;
			$scope.loadedModelings = [];
			$scope.runningModelings = [];
			for(var i in $scope.modelings) {
				if ($scope.modelings[i].state != "UNLOADED")
					$scope.loadedModelings.push($scope.modelings[i]);
				if ($scope.modelings[i].state == "RUNNING")
					$scope.runningModelings.push($scope.modelings[i]);
			}
			$scope.modelingDone = true;
			$scope.initDone = $scope.modelingDone & $scope.agentsDone;
		});
	}
	
	$scope.agentList = function(ignoreLoadingBar) {
		$http.get($scope.server + '/get_agents_available', {timeout:$scope.timeout, ignoreLoadingBar: ignoreLoadingBar}).success(function(data) {
			$scope.agents = data.data.agents;
			$scope.connectedAgents = [];
			$scope.nbNonOKAgents = 0;
			$scope.nbNonOKConnectedAgents = 0;
			for(var i in $scope.agents) {
				if ($scope.agents[i].connected)	{
					$scope.connectedAgents.push($scope.agents[i]);
					if ($scope.agents[i].state != "OK")
						++$scope.nbNonOKConnectedAgents;
				}
				if ($scope.agents[i].state != "OK")
					++$scope.nbNonOKAgents;
			}
			$scope.agentsDone = true;
			$scope.initDone = $scope.modelingDone & $scope.agentsDone;
		});
	}
	
	$scope.$on('httpError', function(event, data) {
		$toaster.clear();
		$scope.addAlert(data.type, data.message);
	});
	
	var modelingListInterval;
	var agentListInterval;
	
	function startIntervals() {
		modelingListInterval = $interval($scope.modelingList, $scope.$storage.interval);
		agentListInterval = $interval($scope.agentList, $scope.$storage.interval);
	}
	
	function stopIntervals() {
		$interval.cancel(modelingListInterval);
		$interval.cancel(agentListInterval);
	}
	
	$scope.$watch("$storage.interval", function(newValue, oldValue) {
		$scope.$storage.interval = (!newValue || newValue < 500 ? 500 : newValue);
		stopIntervals();
		startIntervals();
	});
	
	$scope.$on('$destroy', function() {
		stopIntervals();
    });
	
}]);