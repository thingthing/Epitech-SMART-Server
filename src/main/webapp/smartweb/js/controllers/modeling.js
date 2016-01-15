'use strict';

angular.module('SMARTApp.controllers')
.controller('ModelingCtrl', ['$scope', '$routeParams', '$interval', '$location', '$http', '$timeout', function($scope, $routeParams, $interval, $location, $http, $timeout) {
	
	var CONTINOUS_NBPOINTSTOLOAD = 1000;
	var CONTINOUS_DELAY = 250
	var CONTINOUS_NBVERTEX = 50000;
	var CONTINOUSMAPPING = false;

	var nbPoints = 0;
	var modelingInfoInterval;
	var getPointTimeout;
	var stop = false;

	$scope.$root.noDialog = true;

	$scope.modelingInfo = function() {
		for (var i in $scope.$parent.modelings)
			if ($scope.$parent.modelings[i].name == $routeParams.name)
				$scope.modeling = $scope.$parent.modelings[i];
		
		if (!$scope.modeling || $scope.modeling.state == "UNLOADED")
			$location.path('modelings');
	}
	
	function startIntervals() {
		modelingInfoInterval = $interval($scope.modelingInfo, 100);
	}
	
	function stopIntervals() {
		$interval.cancel(modelingInfoInterval);
		$timeout.cancel(getPointTimeout);
	}
	
	if (!Detector.webgl)
		Detector.addGetWebGLMessage();

	var canvas = document.getElementById("visualizer");
	var camera, scene, renderer, particles, geometry, material, i, color, sprite, size, controls;
	
	camera = new THREE.PerspectiveCamera( 55, window.innerWidth / window.innerHeight, 0.1, 20000 );
	camera.x = -100;
	window.camera = camera

	scene = new THREE.Scene();

	if (true) {
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

	renderer = new THREE.WebGLRenderer({ canvas: canvas });
	renderer.setPixelRatio( window.devicePixelRatio );

	var clock = new THREE.Clock();
	$scope.render = function() {
		renderer.render( scene, camera );
	}

	var stats
	/*
	stats = new Stats();
	$("#visualizer").parent().append( stats.domElement );
	*/

	var req;
	$scope.animate = function() {
		if (stop)
			cancelAnimationFrame(req);// Stop the animation
		else
			req = requestAnimationFrame( $scope.animate );

		var delta = clock.getDelta(),
			time = clock.getElapsedTime() * 5;

		if (controls)
			controls.update(delta)

		if (stats)
			stats.update()

		if (THREEx.FullScreen.activated())
			renderer.setSize( width, height );
		else if (renderer.getSize().width != 800)
			resize(800, 600);

		$scope.render();
	}

	sprite = THREE.ImageUtils.loadTexture("css/img/ball.png");
	material = new THREE.PointsMaterial({size: (25 - 2) / 1000, sizeAttenuation: true, vertexColors: THREE.VertexColors, map: sprite, alphaTest: 0.1, transparent: false});
	geometry = new THREE.Geometry();
	particles = new THREE.Points(geometry, material);

	function initGeometry() {
		for(var i = 0 ; i < CONTINOUS_NBVERTEX ; ++i) {
			geometry.colors.push(new THREE.Color(0, 0, 0));
			geometry.vertices.push(new THREE.Vector3(-42000, -42000, -42000	));
		}
	}


	if (CONTINOUSMAPPING) {
		initGeometry();
		particles = new THREE.Points(geometry, material);
		scene.add(particles);
	}

	function getPoints(from, nb) {
		var url = $scope.server + '/get_points';
		if (CONTINOUSMAPPING)
			url += '?from='+from+'&nb='+nb;
		$http.get(url, {ignoreLoadingBar: CONTINOUSMAPPING}).success(function (data) {
			if (data.data.pointcloud.points.length > 0) {

				for (var i in data.data.pointcloud.points) {
					i = parseInt(i);
					var point = data.data.pointcloud.points[i];
					if (point.x && point.y && point.z) {
						if (!CONTINOUSMAPPING) {
							geometry.colors.push(new THREE.Color("rgb(" + (point.color.red) + "," + (point.color.green) + "," + (point.color.blue) + ")"));
							geometry.vertices.push(new THREE.Vector3(point.x, -point.y, -point.z));
						} else {
							geometry.colors[(i + nbPoints) % CONTINOUS_NBVERTEX].setStyle("rgb(" + (point.color.red) + "," + (point.color.green) + "," + (point.color.blue) + ")");
							geometry.vertices[(i + nbPoints) % CONTINOUS_NBVERTEX].set(point.x, -point.y, -point.z);
						}
					}
				}
				nbPoints += data.data.pointcloud.points.length;

				//if (controls.target.equals(new THREE.Vector3(0, 0, 0)))
					//controls.target.set(data.data.pointcloud.points[nbPoints-1].x, -data.data.pointcloud.points[nbPoints-1].y, -data.data.pointcloud.points[nbPoints-1].z);

				if (CONTINOUSMAPPING) {
					geometry.verticesNeedUpdate = true;
					geometry.colorsNeedUpdate = true;
				} else {
					particles = new THREE.Points(geometry, material);
					scene.add(particles);
				}

				$scope.animate();
			}
			if (CONTINOUSMAPPING)
				getPointTimeout = $timeout(function() {
					getPoints(nbPoints, CONTINOUS_NBPOINTSTOLOAD);
				}, CONTINOUS_DELAY);
		});
	}
	getPoints(nbPoints, CONTINOUS_NBPOINTSTOLOAD);

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
	//$scope.animate();
    
    $scope.$on('$destroy', function() {
		stop = true;
    	stopIntervals();
    	cancelAnimationFrame(req);// Stop the animation
        scene = null;
        camera = null;
        controls = null;
		stats = null;
		$scope.$root.noDialog = false;
    });
}]);
