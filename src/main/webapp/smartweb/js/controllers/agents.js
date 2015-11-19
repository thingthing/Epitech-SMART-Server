'use strict';

angular.module('SMARTApp.controllers')
.controller('AgentsCtrl', ['$scope', '$http', function($scope, $http) {
	$scope.x = 42;

	$scope.func = function() {
		console.log($scope);
	}
}]);