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
		function($scope, $rootScope, $state, principal, Meta, Restangular, $location) {
			$scope.principal = principal;
			$scope.menus = [];
			var promise = Meta.setMTS();
			promise.then(function(){
				angular.forEach(Meta.getMTS(), function(mt) {
					var menu = {
						name: mt.name,
						href: mt.id
					};
					$scope.menus.push(menu);
				});
			});
			
			$scope.signout = function() {
				principal.authenticate(null);
				$state.go('signin');
			};
		});