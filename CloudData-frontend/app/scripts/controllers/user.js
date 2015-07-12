'use strict';

angular.module('clouddataFrontendApp')
	.controller('UserCtrl', function($scope, $rootScope, $timeout, Restangular) {
		$scope.users = [];
		$scope.newuser = {};
		$scope.roles = [];
		$scope.myTreeData = [];
		var tree;
		$scope.myTree = tree = {};
		$scope.try_async_load = function() {
			$scope.doing_async = true;
			Restangular.all("org/listTree").getList().then(function(orgs) {
				$scope.myTreeData = Restangular.stripRestangular(orgs);
				$scope.doing_async = false;
			});
			return $timeout(function() {
				tree.expand_all();
				tree.select_first_branch();
			}, 300);
		};
		$scope.try_async_load();
		$scope.myTreeHandler = function(branch) {
			$scope.newuser.orgId = branch.id;
			$scope.newuser.orgName = branch.label;
			Restangular.all("user/filterByOrg/" + branch.id).getList().then(function(users) {
				$scope.users = Restangular.stripRestangular(users);
				angular.forEach($scope.users, function(user) {
					user['roleName']=[];
					angular.forEach($scope.roles, function(role) {
						if(user.roles.indexOf(role.id)>-1){
							user['roleName'].push(role.label);
						}
					});
					user.roleName=user.roleName.join(',');
				});
			});
		};
		Restangular.all("role").getList().then(function(roles) {
			$scope.roles = Restangular.stripRestangular(roles);
		});
		$scope.save = function() {
			delete $scope.newuser.orgName;
			Restangular.all("user").post($scope.newuser).then(function(response) {
				$(".modal-backdrop").hide();
				$state.transitionTo($state.current, $stateParams, {
					reload: true,
					inherit: false,
					notify: true
				});
			});
		}
		$scope.gridOptions = {
			data: 'users',
			plugins: [new ngGridFlexibleHeightPlugin()],
			columnDefs: [{
				field: 'name',
				displayName: '用户名'
			}, {
				field: 'roleName',
				displayName: '角色'
			}]
		};


	});