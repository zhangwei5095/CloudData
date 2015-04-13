'use strict';

angular.module('cloudErpFrontendApp')
	.controller('DataCtrl', function($scope, $rootScope, $routeParams, $http, Restangular) {
		$scope.realData = [];
		$scope.mid = $routeParams.mid;
		Restangular.all('data/r').getList({
			mid: $routeParams.mid,
			page: 1,
			pagesize: 10
		}).then(function(queryData) {
			$scope.realData = Restangular.stripRestangular(queryData);
		});
		angular.forEach($rootScope.mts, function(mt) {
			if (mt.id === $routeParams.mid)
				$scope.mfs = mt.mfs;
		});

		$scope.filterOptions = {
			filterText: "",
			useExternalFilter: true
		};
		$scope.totalServerItems = 100;

		$scope.pagingOptions = {
			paginationPageSizes: [250, 500, 1000],
			paginationPageSize: 250,
			totalServerItems: 0,
			paginationCurrentPage: 1
		};

		$scope.setPagingData = function(data, page, pageSize) {
			var pagedData = data.slice((page - 1) * pageSize, page * pageSize);
			$scope.myData = pagedData;
			$scope.totalServerItems = data.length;
			if (!$scope.$$phase) {
				$scope.$apply();
			}
		};

		$scope.getPagedDataAsync = function(pageSize, page, searchText) {
			setTimeout(function() {
				var data;
				if (searchText) {
					var ft = searchText.toLowerCase();
					Restangular.all('data/r').getList({
						mid: $routeParams.mid,
						page: 1,
						pagesize: 10
					}).then(function(queryData) {
						$scope.realData = Restangular.stripRestangular(queryData);
						$scope.setPagingData($scope.realData, page, pageSize);
					});
				} else {
					Restangular.all('data/r').getList({
						mid: $routeParams.mid,
						page: 1,
						pagesize: 10
					}).then(function(queryData) {
						$scope.realData = Restangular.stripRestangular(queryData);
						$scope.setPagingData($scope.realData, page, pageSize);
					});
				}
			}, 100);
		};

		$scope.getPagedDataAsync($scope.pagingOptions.pageSize, $scope.pagingOptions.currentPage);

		$scope.$watch('pagingOptions', function(newVal, oldVal) {
			if (newVal !== oldVal && newVal.currentPage !== oldVal.currentPage) {
				$scope.getPagedDataAsync($scope.pagingOptions.pageSize, $scope.pagingOptions.currentPage, $scope.filterOptions.filterText);
			}
		}, true);
		$scope.$watch('filterOptions', function(newVal, oldVal) {
			if (newVal !== oldVal) {
				$scope.getPagedDataAsync($scope.pagingOptions.pageSize, $scope.pagingOptions.currentPage, $scope.filterOptions.filterText);
			}
		}, true);

		$scope.gridOptions = {
			data: 'realData',
			columnDefs: [],
			enablePagination: true
		};
		angular.forEach($scope.mfs, function(mf) {
			var columnDef = {
				field: mf.key,
				displayName: mf.label
			};
			$scope.gridOptions.columnDefs.push(columnDef);
		});
	});