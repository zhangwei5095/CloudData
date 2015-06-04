'use strict';

angular.module('clouddataFrontendApp')
	.controller('DataCtrl', function($scope, $rootScope, $routeParams, $http, $window, Restangular, DTOptionsBuilder, DTColumnBuilder) {
		$scope.vm = {};
		$scope.mid = $routeParams.mid;


		var authToken = $window.sessionStorage.getItem('token');
		$scope.vm.dtOptions = DTOptionsBuilder.newOptions()
			.withOption('ajax', {
				url: 'http://localhost:8080/rest/data/r?mid=' + $scope.mid,
				"headers": {
					'X-Auth-Token': authToken
				}
			})
			.withBootstrap()
			.withBootstrapOptions({
				TableTools: {
					classes: {
						container: 'btn-group',
						buttons: {
							normal: 'btn btn-danger'
						}
					}
				},
				ColVis: {
					classes: {
						masterButton: 'btn btn-primary'
					}
				},
				pagination: {
					classes: {
						ul: 'pagination pagination-sm'
					}
				}
			})
			.withDataProp('data')
			.withOption('processing', true)
			.withPaginationType('full_numbers');
		angular.forEach($rootScope.mts, function(mt) {
			if (mt.id === $routeParams.mid)
				$scope.mfs = mt.mfs;
		});
		$scope.vm.dtColumns = [];
		angular.forEach($scope.mfs, function(mf) {
			var column = DTColumnBuilder.newColumn(mf.key).withTitle(mf.label);
			$scope.vm.dtColumns.push(column);
		});
	});