'use strict';

/**
 * @ngdoc function
 * @name clouddataFrontendApp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of the clouddataFrontendApp
 */
angular.module('clouddataFrontendApp')
.controller('MainCtrl',
    function($scope,$rootScope, $state, principal,Meta, Restangular, $location) {
      $scope.menus = [];
		$rootScope.mts = [];
		
			Restangular.all('mt').getList().then(function(mts) {
				Meta.loadMTS(Restangular.stripRestangular(mts));
				angular.forEach(Meta.getMTS(), function(mt) {
					var menu = {
						name: mt.name,
						href: mt.id
					};
					$scope.menus.push(menu);
				});
			});
		
		$scope.getClass = function(path) {
			if ($location.path().substr(0, path.length) == path) {
				return "active"
			} else {
				return ""
			}
		}
      $scope.signout = function() {
        principal.authenticate(null);
        $state.go('signin');
      };
    });