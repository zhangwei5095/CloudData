'use strict';

angular.module('clouddataFrontendApp')
	.controller('DataCtrl', function($scope, $rootScope, $state, $stateParams, Meta, $http, Restangular) {
		$scope.realData = [];
		$scope.mid = $stateParams.mid;
		$scope.vid = $stateParams.vid;
		$scope.views = Meta.getViewsByMid($scope.mid);
		if (!$scope.vid) $scope.vid = $scope.views[0].id;



		$scope.filterOptions = {
			filterText: "",
			useExternalFilter: false
		};
		$scope.totalServerItems = 100;

		$scope.pagingOptions = {
			paginationPageSizes: [10, 20, 50, 100],
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

		$scope.getPagedDataAsync = function(mid, vid, pageSize, page, searchText) {
			setTimeout(function() {
				var data;
				if (searchText) {
					var ft = searchText.toLowerCase();
					Restangular.all('data/rv').getList({
						mid: mid,
						vid: vid,
						page: page,
						pagesize: pageSize
					}).then(function(queryData) {
						$scope.realData = Restangular.stripRestangular(queryData);
						$scope.setPagingData($scope.realData, page, pageSize);
					});
				} else {
					Restangular.all('data/rv').getList({
						mid: mid,
						vid: vid,
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
		$scope.mySelections = [];
		$scope.gridOptions = {
			data: 'realData',
			columnDefs: [],
			enablePagination: true,
			paginationPageSizes: [10, 20, 50, 100],
			paginationPageSize: 10,
			totalServerItems: 10,
			paginationCurrentPage: 1,
			showSelectionCheckbox: true,
			onRegisterApi: function(gridApi) {
				$scope.gridApi = gridApi;
				gridApi.selection.on.rowSelectionChanged($scope, function(rows) {
					$scope.mySelections = gridApi.selection.getSelectedRows();
				});
			}
		};
		var displayColumn = Meta.getViewByMidVid($scope.mid, $scope.vid).displayColumn;
		var displayColumns = displayColumn.split(',');
		angular.forEach(Meta.getMFSByMid($scope.mid),
			function(mf) {
				if (displayColumns.indexOf(mf.key) > 0) {
					var columnDef = {
						field: mf.key,
						displayName: mf.label
					};
					$scope.gridOptions.columnDefs.push(columnDef);
				}
			});
		$scope.update = function() {
			$state.go('app.data', {
				mid: $scope.mid,
				vid: $scope.vid
			});
		}

		$scope.detail = function() {
			$state.go('app.detail', {
				mid: $scope.mid,
				rid: $scope.mySelections[0]._id
			});
		}

		$scope.edit = function() {
			$state.go('app.formly', {
				mid: $scope.mid,
				rid: $scope.mySelections[0]._id
			});
		}

		$scope.delete = function() {
			Restangular.one("data/" + $scope.mid + "/" + $scope.mySelections[0]._id).remove().then(function(response) {
				$state.transitionTo($state.current, $stateParams, {
					reload: true,
					inherit: false,
					notify: true
				});
			});
		}


		$scope.getPagedDataAsync($scope.mid, $scope.vid, $scope.pagingOptions.paginationPageSize, $scope.pagingOptions.paginationCurrentPage, '');
		$scope.labels = ["Download Sales", "In-Store Sales", "Mail-Order Sales"];
		$scope.data = [300, 500, 100];
	});