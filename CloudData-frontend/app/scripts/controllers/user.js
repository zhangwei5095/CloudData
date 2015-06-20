'use strict';

angular.module('clouddataFrontendApp')
	.controller('UserCtrl', function($scope, $rootScope, $timeout, Restangular) {
		$scope.users = [];
		$scope.newuser = {};
		$scope.roles = [];
		$scope.myTreeHandler = function(branch) {
			$scope.newuser.orgId = branch.id;
			$scope.newuser.orgName = branch.label;
			Restangular.all("user/filterByOrg/" + branch.id).getList().then(function(users) {
				$scope.users = Restangular.stripRestangular(users);
			});
		};
		Restangular.all("role").getList().then(function(roles) {
			$scope.roles = Restangular.stripRestangular(roles);
		});
		$scope.myTreeData = [];
		Restangular.all("org/listTree").getList().then(function(orgs) {
			$scope.myTreeData = Restangular.stripRestangular(orgs);
		});


		$scope.save = function() {
			delete $scope.newuser.orgName;
			Restangular.all("user").post($scope.newuser).then(function(response) {
				$location.path('user');
			});
		}
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