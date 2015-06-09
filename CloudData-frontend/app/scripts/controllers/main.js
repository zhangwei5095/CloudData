'use strict';

/**
 * @ngdoc function
 * @name clouddataFrontendApp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of the clouddataFrontendApp
 */
angular.module('clouddataFrontendApp')
	.controller('MainCtrl', function($scope, $window, $rootScope, Restangular, $location) {
		$scope.menus = [];
		$rootScope.mts = [];
		if ($window.sessionStorage.getItem('token') == undefined) {
			$location.path("/login");
			return;
		} else {
			Restangular.all('mt').getList().then(function(mts) {
				$rootScope.mts = Restangular.stripRestangular(mts);
				angular.forEach($rootScope.mts, function(mt) {
					var menu = {
						name: mt.name,
						href: 'data/' + mt.id
					};
					$scope.menus.push(menu);
				});
			});
		}
		$scope.getClass = function(path) {
			if ($location.path().substr(0, path.length) == path) {
				return "active"
			} else {
				return ""
			}
		}
		/**
 	$scope.menus = [{
 		name: "Formly",
 		href: "formly"
 	}, {
 		name: "SchemaForm",
 		href: "schemaForm"
 	}, {
 		name: 'AutoField',
 		href: "autoField"
 	}, {
 		name: 'react',
 		href: 'react'
 	}, {
 		name: 'jsx',
 		href: 'jsx'
 	}, {
 		name: 'grid',
 		href: 'grid'
 	}];**/
	});