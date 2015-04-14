'use strict';

angular.module('cloudErpFrontendApp')
	.controller('RoleCtrl', function($scope, $rootScope, $routeParams, $timeout, Restangular) {
		$scope.roleResource = [];
		$scope.gridOptions = {
			data: 'roleResource',
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

		$scope.roleData = [];
		Restangular.all("role").getList().then(function(roles) {
			$scope.roleData = Restangular.stripRestangular(roles);
		});

		
	});