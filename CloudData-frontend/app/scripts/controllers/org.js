'use strict';

angular.module('clouddataFrontendApp')
	.controller('OrgCtrl', function($scope, $rootScope, $routeParams, $timeout, $location, Restangular) {
		$scope.orgs = [];
		$scope.neworg = {};
		$scope.myTreeHandler = function(branch) {
			$scope.neworg.pid = branch.id;
			$scope.neworg.pname = branch.label;
		};

		$scope.myTreeData = [];
		Restangular.all("org/listTree").getList().then(function(orgs) {
			$scope.myTreeData = Restangular.stripRestangular(orgs);

		});
		Restangular.all("org").getList().then(function(orgs) {
			$scope.orgs = Restangular.stripRestangular(orgs);
		});

		$scope.save = function() {
			delete $scope.neworg.pname;
			Restangular.all("org").post($scope.neworg).then(function(response) {
				$location.path('org');
			});
		};

		$scope.gridOptions = {
			data: 'orgs',
			columnDefs: [{
				field: 'id',
				displayName: 'ID'
			}, {
				field: 'label',
				displayName: '机构名'
			}]
		};
	});