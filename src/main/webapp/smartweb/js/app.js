'use strict';

angular.module('SMARTApp', [
	'ngRoute',
	'ngStorage',
	'angularMoment',
	'ui.bootstrap',
	'angular-loading-bar',
	'ngAnimate',
	'toaster',
	'SMARTApp.httpInterceptor',
	'SMARTApp.controllers'
])
.config(function($routeProvider, $locationProvider) {
	$routeProvider
	.when('/', {
		templateUrl: 'views/home.html',
	})
	.when('/agents', {
		templateUrl: 'views/agents.html',
		controller: 'AgentsCtrl'
	})
	.when('/modelings', {
		templateUrl: 'views/modelings.html',
		controller: 'ModelingsCtrl'
	})
	.when('/modelings', {
		templateUrl: 'views/modelings.html',
		controller: 'ModelingsCtrl'
	})
	.when('/modeling/:name', {
		templateUrl: 'views/modeling.html',
		controller: 'ModelingCtrl'
	})
	.when('/sessions', {
		templateUrl: 'views/sessions.html',
		controller: 'SessionsCtrl'
	})
	.otherwise({
		redirectTo: '/'
	});
	$locationProvider.html5Mode(true);
});

angular.module('SMARTApp.controllers', []);