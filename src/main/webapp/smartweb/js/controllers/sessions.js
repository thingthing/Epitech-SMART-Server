'use strict';

angular.module('SMARTApp.controllers')
.controller('SessionsCtrl', ['$scope', '$http', '$interval', function($scope, $http, $interval) {
	
	$scope.sessionList = function(ignoreLoadingBar) {
		$http.get($scope.server + '/get_sessions', {timeout:$scope.timeout, ignoreLoadingBar: ignoreLoadingBar}).success(function(data) {
			//console.log(data);
			if (data == "")
				$scope.sessions = [];
			else
				$scope.sessions = data.data.sessions;
			//console.log($scope.sessions);
		});
	}
	
	var sessionListInterval;
	
	$scope.$parent.$watch("$storage.interval", function(newValue, oldValue) {
		stopIntervals();
		startIntervals();
	});
	
	function startIntervals() {
		sessionListInterval = $interval($scope.sessionList, $scope.$storage.interval);
	}
	
	function stopIntervals() {
		$interval.cancel(sessionListInterval);
	}
	
	$scope.$on('$destroy', function() {
		stopIntervals();
	});
}]);