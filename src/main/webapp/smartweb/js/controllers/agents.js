'use strict';

angular.module('SMARTApp.controllers')
.controller('AgentsCtrl', ['$scope', '$http', function($scope, $http) {
	$scope.x = 42;
	$scope.y = 12;

	$scope.func = function() {
		console.log($scope.x, $scope.$root.y, $scope.z);
	}
}]);