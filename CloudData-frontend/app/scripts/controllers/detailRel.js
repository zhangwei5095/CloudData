'use strict';

/**
 * @ngdoc function
 * @name clouddataFrontendApp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of the clouddataFrontendApp
 */
angular.module('clouddataFrontendApp')
	.controller('DetailRelCtrl',
		function($scope, $rootScope, $state, $stateParams, Meta, Restangular, $location) {
			$scope.mid = $stateParams.mid;
			$scope.rid = $stateParams.rid;
			$scope.rmid = $stateParams.rmid;
			$scope.roid = $stateParams.roid;
			$scope.mfs = Meta.getMFSByMid($scope.mid);

			Restangular.one("data/rr?mid=" + $scope.mid + "&rid=" + $scope.rid + "&rmid=" + $scope.rmid + "&roid=" + $scope.roid).get().then(function(data) {
				$scope.data = Restangular.stripRestangular(data);
			});

			$scope.gridOptions = {
				data: 'data',
				columnDefs: [],
				enablePagination: true,
				paginationPageSizes: [10, 20, 50, 100],
				paginationPageSize: 10,
				totalServerItems: 10,
				paginationCurrentPage: 1,
				onRegisterApi: function(gridApi) {
					$scope.gridApi = gridApi;
					gridApi.selection.on.rowSelectionChanged($scope, function(rows) {
						$scope.mySelections = gridApi.selection.getSelectedRows();
					});
				}
			};
			angular.forEach(Meta.getMFSByMid($scope.rmid), function(mf) {
				var columnDef = {
					field: mf.key,
					displayName: mf.label
				};
				$scope.gridOptions.columnDefs.push(columnDef);
			});
		});