'use strict';

angular.module('cloudErpFrontendApp')
	.controller('OrgCtrl', function($scope, $rootScope, $routeParams, $timeout, Restangular) {
		$scope.users = [];
		$scope.myTreeHandler = function(branch) {
			$scope.output = 'You selected: ' + branch.id;
			Restangular.all("user/filterByOrg/"+ branch.id).getList().then(function(users) {
				$scope.users = Restangular.stripRestangular(users);
			});
		};

		$scope.myTreeData = [];
		Restangular.all("org").getList().then(function(orgs) {
			$scope.myTreeData = Restangular.stripRestangular(orgs);
		});

		$scope.gridOptions = {
			data: 'users',
			columnDefs: [{
				field: 'name',
				displayName: '用户名'
			}, {
				field: 'roles',
				displayName: '角色'
			}]
		};
	});