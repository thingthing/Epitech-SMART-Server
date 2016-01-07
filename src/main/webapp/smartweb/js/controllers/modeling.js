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
	var camera, scene, renderer, particles, geometry, material, i, color, sprite, size, controls;
	
	camera = new THREE.PerspectiveCamera( 55, window.innerWidth / window.innerHeight, 0.01, 2000 );
	camera.position.z = 1000;

	scene = new THREE.Scene();
	scene.fog = new THREE.FogExp2( 0x000000, 0.001 );
	
	controls = new THREE.TrackballControls( camera );
	controls.target.set( 0, 0, 0 );
	
	controls.rotateSpeed = 4.0;
	controls.zoomSpeed = 1.2;
	controls.panSpeed = 1.0;
	
	controls.staticMoving = false;

	renderer = new THREE.WebGLRenderer({ canvas: canvas });
	renderer.setPixelRatio( window.devicePixelRatio );
	
	var req;
	function animate() {
		req = requestAnimationFrame( animate );
		$scope.render();
		controls.update()
	}

	$scope.render = function() {
		renderer.render( scene, camera );
	}

    $http.get($scope.server + '/get_points').success(function(data) {
		geometry = new THREE.Geometry();
		var colors = [];
    	for (var i in data.data.pointcloud.points) {
    		var vertex = new THREE.Vector3();
			var point = data.data.pointcloud.points[i];
    		vertex.x = point.x;
    		vertex.y = point.y;
    		vertex.z = point.z;
			colors[i] = new THREE.Color("rgb("+(20+point.color.red)+","+(20+point.color.green+20)+","+(20+point.color.blue)+")");
    		geometry.vertices.push( vertex );
    	}
		console.log(colors);
    	geometry.colors = colors;
    	sprite = THREE.ImageUtils.loadTexture( "css/img/disc.png" );
    	material = new THREE.PointsMaterial( { size: 10, sizeAttenuation: false, vertexColors: THREE.VertexColors, map: sprite, alphaTest: 0.1, transparent: true } );

    	particles = new THREE.Points( geometry, material );
    	
    	scene.add( particles );
    	animate();
    });
    
    startIntervals();
    
    $scope.$on('$destroy', function() {
    	stopIntervals();
    	cancelAnimationFrame(req);// Stop the animation
        this.scene = null;
        this.camera = null;
        this.controls = null;
    });
}]);
