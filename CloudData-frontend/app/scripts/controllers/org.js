'use strict';

angular.module('clouddataFrontendApp')
	.controller('OrgCtrl', function($scope, $rootScope, $state, $stateParams, $timeout, $location, Restangular) {
		$scope.orgs = [];
		$scope.neworg = {};
		$scope.myTreeHandler = function(branch) {
			$scope.neworg.pid = branch.id;
			$scope.neworg.pname = branch.label;
		};
		var tree;
		$scope.myTree = tree = {};
		$scope.try_async_load = function() {
			$scope.doing_async = true;
			Restangular.all("org/listTree").getList().then(function(orgs) {
				$scope.myTreeData = Restangular.stripRestangular(orgs);

			});
			return $timeout(function() {
				tree.expand_all();
				tree.select_first_branch();
			}, 300);
		};
		$scope.try_async_load();

		$scope.myTreeData = [];


		Restangular.all("org").getList().then(function(orgs) {
			$scope.orgs = Restangular.stripRestangular(orgs);
		});

		$scope.save = function() {
			delete $scope.neworg.pname;
			Restangular.all("org").post($scope.neworg).then(function(response) {
				$(".modal-backdrop").hide();
				$state.transitionTo($state.current, $stateParams, {
					reload: true,
					inherit: false,
					notify: true
				});
			});
		};

		$scope.gridOptions = {
			data: 'orgs',
			plugins: [new ngGridFlexibleHeightPlugin()],
			columnDefs: [{
				field: 'id',
				displayName: 'ID'
			}, {
				field: 'label',
				displayName: '机构名'
			}]

		};
	});