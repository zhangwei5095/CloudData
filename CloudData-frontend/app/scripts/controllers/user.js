'use strict';

angular.module('cloudErpFrontendApp')
	.controller('UserCtrl', function($scope, $rootScope, $routeParams, $timeout, Restangular) {
		$scope.users = [];
		$scope.gridOptions = {
			data: 'userData',
			columnDefs: [{
				field: 'name',
				displayName: '用户名'
			}, {
				field: 'orgId',
				displayName: '机构号'
			}, {
				field: 'roles',
				displayName: '角色'
			}]
		};

		$scope.userData = [];
		Restangular.all("user/all").getList().then(function(users) {
			$scope.userData = Restangular.stripRestangular(users);
		});

		
	});