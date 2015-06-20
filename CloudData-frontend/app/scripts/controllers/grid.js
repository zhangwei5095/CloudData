'use strict';

angular.module('clouddataFrontendApp')
	.controller('DataCtrl', function($scope, $rootScope, $stateParams,Meta, $http, Restangular) {
		$scope.realData = [];
		$scope.mid = $stateParams.mid;
		$rootScope.mid = $stateParams.mid;

		Meta.selectMid($stateParams.mid);
		$rootScope.mfs=Meta.getMFS();
		$rootScope.views=Meta.getViews();
		

		$scope.filterOptions = {
			filterText: "",
			useExternalFilter: false
		};
		$scope.totalServerItems = 100;

		$scope.pagingOptions = {
			paginationPageSizes: [10, 20, 50,100],
			paginationPageSize: 10,
			totalServerItems: 10,
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

		$scope.getPagedDataAsync = function(mid,vid,pageSize, page, searchText) {
			setTimeout(function() {
				var data;
				if (searchText) {
					var ft = searchText.toLowerCase();
					Restangular.all('data/rv').getList({
						mid: mid,
						vid:vid,
						page: page,
						pagesize: pageSize
					}).then(function(queryData) {
						$scope.realData = Restangular.stripRestangular(queryData);
						$scope.setPagingData($scope.realData, page, pageSize);
					});
				} else {
					Restangular.all('data/rv').getList({
						mid: mid,
						vid:vid,
						page: page,
						pagesize: pageSize
					}).then(function(queryData) {
						$scope.realData = Restangular.stripRestangular(queryData);
						$scope.setPagingData($scope.realData, page, pageSize);
					});
				}
			}, 100);
		};

		//$scope.getPagedDataAsync($scope.mid,"all",$scope.pagingOptions.paginationPageSize, $scope.pagingOptions.paginationCurrentPage,'');

		$scope.$watch('pagingOptions', function(newVal, oldVal) {
			if (newVal !== oldVal && newVal.currentPage !== oldVal.currentPage) {
				$scope.getPagedDataAsync($scope.gridOptions.paginationPageSize, $scope.gridOptions.paginationCurrentPage, $scope.filterOptions.filterText);
			}
		}, true);
		$scope.$watch('filterOptions', function(newVal, oldVal) {
			if (newVal !== oldVal) {
				$scope.getPagedDataAsync($scope.pagingOptions.paginationPageSize, $scope.pagingOptions.paginationCurrentPage, $scope.filterOptions.filterText);
			}
		}, true);

		$scope.gridOptions = {
			data: 'realData',
			columnDefs: [],
			enablePagination: true,
			paginationPageSizes: [10, 20, 50,100],
			paginationPageSize: 10,
			totalServerItems: 10,
			paginationCurrentPage: 1
		};
		angular.forEach(Meta.getMFS(), function(mf) {
			var columnDef = {
				field: mf.key,
				displayName: mf.label
			};
			$scope.gridOptions.columnDefs.push(columnDef);
		});

		$scope.update=function(){
   			$scope.getPagedDataAsync($scope.mid,$scope.selectedItem.id,$scope.pagingOptions.paginationPageSize, $scope.pagingOptions.paginationCurrentPage,'');
		}
	});