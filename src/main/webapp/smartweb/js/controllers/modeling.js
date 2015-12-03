'use strict';

angular.module('SMARTApp.controllers')
.controller('ModelingCtrl', ['$scope', '$routeParams', '$interval', '$location', '$http', function($scope, $routeParams, $interval, $location, $http) {
	
	$scope.modelingInfo = function() {
		for (var i in $scope.$parent.modelings)
		if ($scope.$parent.modelings[i].name == $routeParams.name)
		$scope.modeling = $scope.$parent.modelings[i];
		
		if (!$scope.modeling || $scope.modeling.state == "UNLOADED")
		$location.path('modelings');
	}
	
	var modelingInfoInterval;
	
	function startIntervals() {
		modelingInfoInterval = $interval($scope.modelingInfo, 100);
	}
	
	function stopIntervals() {
		$interval.cancel(modelingInfoInterval);
	}
	
	if (!Detector.webgl)
		Detector.addGetWebGLMessage();

	var canvas = document.getElementById("visualizer");
	var camera, scene, renderer, particles, geometry, material, i, h, color, sprite, size, controls;
	
	camera = new THREE.PerspectiveCamera( 55, window.innerWidth / window.innerHeight, 2, 2000 );
	camera.position.z = 1000;

	scene = new THREE.Scene();
	//scene.fog = new THREE.FogExp2( 0x000000, 0.001 );
	
	controls = new THREE.TrackballControls( camera );
	controls.target.set( 0, 0, 0 )
	
	geometry = new THREE.Geometry();
	
	$http.get($scope.server + '/get_points').success(function(data) {
    	for (var i in data.data.pointcloud.points) {
    		var vertex = new THREE.Vector3();
    		vertex.x = data.data.pointcloud.points[i].x;
    		vertex.y = data.data.pointcloud.points[i].y;
    		vertex.z = data.data.pointcloud.points[i].z;
    		geometry.vertices.push( vertex );
    	}
    	
    	sprite = THREE.ImageUtils.loadTexture( "css/img/disc.png" );
    	material = new THREE.PointsMaterial( { size: 10, sizeAttenuation: false, map: sprite, alphaTest: 0.1, transparent: true } );
    	material.color.setHSL( 1.0, 0.3, 0.7 );

    	particles = new THREE.Points( geometry, material );
    	
    	scene.add( particles );
    	animate();
    });
	

	renderer = new THREE.WebGLRenderer({ canvas: canvas });
	renderer.setPixelRatio( window.devicePixelRatio );
	
	var req;
	function animate() {
		req = requestAnimationFrame( animate );
		$scope.render();
		controls.update()
	}

	$scope.render = function() {
		material.color.setHSL( h, 0.5, 0.5 );
		renderer.render( scene, camera );
		
	}
    
    startIntervals();
    
    $scope.$on('$destroy', function() {
    	stopIntervals();
    	cancelAnimationFrame(req);// Stop the animation
        this.scene = null;
        this.camera = null;
        this.controls = null;
    });
}]);
