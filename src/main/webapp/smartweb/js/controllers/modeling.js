'use strict';

angular.module('SMARTApp.controllers')
.controller('ModelingCtrl', ['$scope', '$routeParams', '$interval', '$location', '$http', function($scope, $routeParams, $interval, $location, $http) {

	$scope.$root.noDialog = true;

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
	
	camera = new THREE.PerspectiveCamera( 55, window.innerWidth / window.innerHeight, 0.1, 20000 );
	//camera.position.z = 1000;
	window.camera = camera

	scene = new THREE.Scene();

	if (false) {
		controls = new THREE.FirstPersonControls(camera, canvas);

		controls.movementSpeed = 1;
		controls.lookSpeed = 0.15;
		controls.noFly = false;
		controls.lookVertical = true;
	} else {

		controls = new THREE.TrackballControls(camera, canvas);

		controls.rotateSpeed = 4.0;
		controls.zoomSpeed = 1.2;
		controls.panSpeed = 1.0;

		controls.staticMoving = false;
		controls.dynamicDampingFactor = 0.25
	}

	controls.target.set(0, 0, 0);
	window.controls = controls;

	renderer = new THREE.WebGLRenderer({ canvas: canvas });
	renderer.setPixelRatio( window.devicePixelRatio );

	var clock = new THREE.Clock();
	$scope.render = function() {
		renderer.render( scene, camera );
	}

	var req;
	$scope.animate = function() {
		req = requestAnimationFrame( $scope.animate );
		var delta = clock.getDelta(),
			time = clock.getElapsedTime() * 5;
		//console.log(delta);
		controls.update(delta)
		if (THREEx.FullScreen.activated())
			renderer.setSize( width, height );
		else if (renderer.getSize().width != 800)
			resize(800, 600);
		$scope.render();
	}

	var helper = new THREE.GridHelper( 500, 10 );
	helper.color1.setHex( 0x444444 );
	helper.color2.setHex( 0x444444 );
	helper.position.y = 0.1;
	scene.add( helper );

    $http.get($scope.server + '/get_points').success(function(data) {
		geometry = new THREE.Geometry();
		var colors = [];
    	for (var i in data.data.pointcloud.points) {
    		var vertex = new THREE.Vector3();
			var point = data.data.pointcloud.points[i];
    		vertex.x = point.x;
    		vertex.y = -point.y;
    		vertex.z = -point.z;
			if (vertex.x && vertex.y && vertex.z) {
				colors.push(new THREE.Color("rgb(" + (point.color.red) + "," + (point.color.green + 20) + "," + (point.color.blue) + ")"));
				geometry.vertices.push(vertex);
			}
    	}
		controls.target.set( data.data.pointcloud.points[i].x, -data.data.pointcloud.points[i].y, -data.data.pointcloud.points[i].z );
    	geometry.colors = colors;
    	sprite = THREE.ImageUtils.loadTexture( "css/img/ball.png" );
    	material = new THREE.PointsMaterial( { size: (25 - 2) / 1000, sizeAttenuation: true, vertexColors: THREE.VertexColors, map: sprite, alphaTest: 0.1, transparent: false } );
		window.material = material

    	particles = new THREE.Points( geometry, material );
    	
    	scene.add( particles );
    	$scope.animate();
    });
	window.renderer = renderer;

	$("#fullscreen").click(function() {
		THREEx.FullScreen.request(canvas);
		resize(window.innerWidth, window.innerHeight);
	});

	function resize(width, height) {
		camera.aspect = width / height;
		camera.updateProjectionMatrix();
		renderer.setSize( width, height );
		controls.handleResize();
	}

    startIntervals();
    
    $scope.$on('$destroy', function() {
    	stopIntervals();
    	cancelAnimationFrame(req);// Stop the animation
        scene = null;
        camera = null;
        controls = null;
		$scope.$root.noDialog = false;
    });
}]);
