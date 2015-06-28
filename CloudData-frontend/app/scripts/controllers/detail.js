'use strict';

/**
 * @ngdoc function
 * @name clouddataFrontendApp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of the clouddataFrontendApp
 */
angular.module('clouddataFrontendApp')
	.controller('DetailCtrl',
		function($scope, $rootScope, $state, $stateParams, Meta, Restangular, $location) {
			$scope.mid = $stateParams.mid;
			$scope.rid = $stateParams.rid;
			$scope.recordData1=[];
			$scope.recordData2=[];
			$scope.mfs = Meta.getMFSByMid($scope.mid);
			if ($scope.rid) {
				Restangular.one("data/r?mid=" + $scope.mid + "&rid=" + $scope.rid).get().then(function(data) {
					$scope.recorddata = Restangular.stripRestangular(data);
					for(var i=0;i<$scope.mfs.length;i++){
						var mf=$scope.mfs[i];
						if ($scope.recorddata[mf.key])
							mf["value"] = $scope.recorddata[mf.key];
						if(i%2===0){
							$scope.recordData1.push(mf);
						}else{
							$scope.recordData2.push(mf);
						}
					}
				});
			}
			$scope.mt=Meta.getMTByMid($scope.mid);
			
		});