'use strict';

angular.module('SMARTApp.httpInterceptor', [])
.factory('httpInterceptor', function ($q, $rootScope, $log) {
	
	var nextID = {};
	var maxID = {};
	
	return {
		request: function (config) {
			var a = document.createElement('a');
			a.href = config.url;
			if (nextID[a.pathname] === undefined)
				nextID[a.pathname] = 1;
			config.id = nextID[a.pathname]++;
			return config || $q.when(config)
		},
		response: function (response) {
			var a = document.createElement('a');
			a.href = response.config.url;
			if (maxID[a.pathname] === undefined)
				maxID[a.pathname] = 0;
			maxID[a.pathname] = Math.max(maxID[a.pathname], response.config.id);
			if (response.config.id < maxID[a.pathname])
				$q.reject(response);
			if (response.data.status && response.data.status.code != 0) {
				$rootScope.$broadcast('httpError', {
					type: 'danger',
					message: response.data.status.message.capitalize()
				});
			}
			return response || $q.when(response);
			
		},
		requestError: function (response) {
			return $q.reject(response);
		},
		responseError: function (response) {
			return $q.reject(response);
		}
	};
})
.config(function ($httpProvider) {
	$httpProvider.interceptors.push('httpInterceptor');
});