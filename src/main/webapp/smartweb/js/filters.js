'use strict';

angular.module('SMARTApp')
.filter('reverse', function() {
	return function(items) {
		return items.slice().reverse();
	};
});