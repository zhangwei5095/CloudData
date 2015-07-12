'use strict';

angular.module('clouddataFrontendApp')
	.controller('DataCtrl', function($scope, $rootScope, $state, $stateParams, Meta, $http, Restangular) {
		// some init params
		$scope.realData = [];
		$scope.mid = $stateParams.mid;
		$scope.vid = $stateParams.vid;
		$scope.views = Meta.getViewsByMid($scope.mid);
		if (!$scope.vid) $scope.vid = $scope.views[0].id;


		// filterOptions and pagingOptions
		$scope.filterOptions = {
			filterText: "",
			useExternalFilter: false
		};

		$scope.pagingOptions = {
			pageSizes: [10, 20, 50, 100],
			pageSize: 10,
			totalServerItems: 0,
			currentPage: 1
		};
		// recalc page data
		$scope.setPagingData = function(data, page, pageSize) {
			var pagedData = data.slice((page - 1) * pageSize, page * pageSize);
			$scope.myData = pagedData;
			$scope.totalServerItems = data.length;
			if (!$scope.$$phase) {
				$scope.$apply();
			}
		};
		// getPagedDataAsync 
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


		// watch pagingOptions and filterOptions
		$scope.$watch('pagingOptions', function(newVal, oldVal) {
			if (newVal !== oldVal && newVal.currentPage !== oldVal.currentPage) {
				$scope.getPagedDataAsync($scope.gridOptions.pageSize, $scope.gridOptions.currentPage, $scope.filterOptions.filterText);
			}
		}, true);
		$scope.$watch('filterOptions', function(newVal, oldVal) {
			if (newVal !== oldVal) {
				$scope.getPagedDataAsync($scope.pagingOptions.pageSize, $scope.pagingOptions.currentPage, $scope.filterOptions.filterText);
			}
		}, true);
		$scope.mySelections = [];

		// gridOptions
		$scope.gridOptions = {
			data: 'realData',
			columnDefs: [],
			enablePaging: true,
			showFooter: true,
			pagingOptions: $scope.pagingOptions,
			filterOptions: $scope.filterOptions,
			plugins: [new ngGridFlexibleHeightPlugin()],
			showSelectionCheckbox: true,
			onRegisterApi: function(gridApi) {
				$scope.gridApi = gridApi;
				gridApi.selection.on.rowSelectionChanged($scope, function(rows) {
					$scope.mySelections = gridApi.selection.getSelectedRows();
				});
			}
		};

		// display column
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
		// operation button
		var operateRowTemplate = '<a class="glyphicon glyphicon-list" ng-click="view(row)" /><a class="glyphicon glyphicon-edit" ng-click="edit(row)" /><a class="glyphicon glyphicon-remove" ng-click="delete(row)"/>';
		$scope.gridOptions.columnDefs.push({
			field: 'operation',
			displayName: '操作',
			width: "7%",
			cellTemplate: operateRowTemplate
		});

		//  crud operation
		$scope.update = function() {
			$state.go('app.data', {
				mid: $scope.mid,
				vid: $scope.vid
			});
		}

		$scope.view = function(row) {
			$state.go('app.detail', {
				mid: $scope.mid,
				rid: row.entity._id
			});
		}

		$scope.edit = function(row) {
			$state.go('app.formly', {
				mid: $scope.mid,
				rid: row.entity._id
			});
		}

		$scope.delete = function(row) {
			Restangular.one("data/" + $scope.mid + "/" + row.entity._id).remove().then(function(response) {
				$state.transitionTo($state.current, $stateParams, {
					reload: true,
					inherit: false,
					notify: true
				});
			});
		}

		// load data at first
		$scope.getPagedDataAsync($scope.mid, $scope.vid, $scope.pagingOptions.pageSize, $scope.pagingOptions.currentPage, '');
	});