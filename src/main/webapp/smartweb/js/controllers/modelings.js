'use strict';

angular.module('SMARTApp.controllers')
.controller('ModelingsCtrl', ['$scope', '$http', '$interval', function($scope, $http, $interval) {
	
	$scope.modelingLoad = function(name) {
		$http.get($scope.server + '/modeling_load?name='+name).success(function(data) {
			$scope.modelingList();
		});
	}
	
	$scope.modelingDownload = function(name) {
		for(var i in $scope.agents) {
			if ($scope.agents[i].connected)	{
				$http.get($scope.server + '/download?name='+$scope.agents[i].name).success(function(data) {
					//$scope.agentList(true);
				});
			}
		}
		
	}
	
	$scope.modelingUnload = function(name) {
		if ($scope.getModelingByName(name).modified)
			bootbox.confirm({
				buttons: {
					cancel: {label:"No"},
					confirm:{label:"Yes"}
				},
				title:"Warning",
				message: "Do you want to save before unloading the modeling ?",
				callback: function(result) {
					if (result) {
						$scope.modelingSave(name);
					}
					$http.get($scope.server + '/modeling_unload?name=' + name).success(function (data) {
						$scope.modelingList();
					});
				}
			});
		else {
			$http.get($scope.server + '/modeling_unload?name=' + name).success(function (data) {
				$scope.modelingList();
			});
		}
	}
	
	$scope.modelingDelete = function(name) {
		$http.get($scope.server + '/modeling_delete?name='+name).success(function(data) {
			$scope.modelingList();
		});
	}
	
	$scope.modelingSave = function(name) {
		$http.get($scope.server + '/modeling_save?name='+name).success(function(data) {
			$scope.modelingList();
			$scope.$parent.addAlert('success', 'Modeling ' + name + ' successfully saved.');
		});
	}
	
	$scope.newModelingName = "";
	$scope.modelingCreate = function(name) {
		$http.get($scope.server + '/modeling_create?name='+name).success(function(data) {
			$scope.modelingList();
			console.log($scope.modelingCreateForm);
			if (data.status == 0) {
				$scope.newModelingName = "";
				$scope.modelingCreateForm.$setPristine();
			}
		});
	}
	
}]);