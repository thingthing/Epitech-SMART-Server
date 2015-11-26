'use strict';

angular.module('SMARTApp.controllers')
.controller('ModelingCtrl', ['$scope', '$routeParams', '$interval', '$location', function($scope, $routeParams, $interval, $location) {
	
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
	
	// Get the canvas element from our HTML above
	var canvas = document.getElementById("visualizer");
	// Load the BABYLON 3D engine
	var engine = new BABYLON.Engine(canvas, true);
	
	// This begins the creation of a function that we will 'call' just after it's built
	var createScene = function () {
		
		// Now create a basic Babylon Scene object 
		var scene = new BABYLON.Scene(engine);
		
		// Change the scene background color to green.
		scene.clearColor = new BABYLON.Color3(1, 1, 1);
		
		// This creates and positions a free camera
		var camera = new BABYLON.FreeCamera("camera1", new BABYLON.Vector3(0, 5, -10), scene);
		
		// This targets the camera to scene origin
		camera.setTarget(BABYLON.Vector3.Zero());
		
		// This attaches the camera to the canvas
		camera.attachControl(canvas, false);
		
		// This creates a light, aiming 0,1,0 - to the sky.
		var light = new BABYLON.HemisphericLight("light1", new BABYLON.Vector3(0, 1, 0), scene);
		
		// Dim the light a small amount
		light.intensity = .5;
		
	    $.getJSON("http://54.148.17.11:8080/smartserver/get_points", function( data ) {        	
		console.log(data);
		var offset = 0.05;
		if (data.status.code == 0)
		{
		    for (var i in data.data.pointcloud.points) {
			
			
			/*       TEST segments
				 var list = new Array;
				 list.push(new BABYLON.Vector3(data.data.pointcloud.points[i].x - offset, data.data.pointcloud.points[i].y - offset, data.data.pointcloud.points[i].z - offset));
				 list.push(new BABYLON.Vector3(data.data.pointcloud.points[i].x + offset, data.data.pointcloud.points[i].y + offset, data.data.pointcloud.points[i].z + offset));
				 var lines = BABYLON.Mesh.CreateLines("Lines", list, scene);
			*/		

			/*      TEST cubes   */
			var cube = BABYLON.Mesh.CreateBox("cube", 0.5, scene);
			cube.position.x = data.data.pointcloud.points[i].x;
			cube.position.y = data.data.pointcloud.points[i].y;
			cube.position.z = data.data.pointcloud.points[i].z;
		    }
		    
		}
	    });
	    
	    return scene;
	    
	};
    
    var scene = createScene();
    
    engine.runRenderLoop(function () {
	scene.render();
    });
    
    
    // Watch for browser/canvas resize events
    window.addEventListener("resize", function () {
	engine.resize();
    });
    
    startIntervals();
    
    $scope.$on('$destroy', function() {
	stopIntervals();
    });
}]);
