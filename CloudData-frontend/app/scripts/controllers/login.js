	'use strict';

	angular.module('clouddataFrontendApp')
		.controller('SigninCtrl', function($scope, $state, principal, Restangular) {
			$scope.user = {};
			$scope.signin = function() {
				Restangular.all("user/authenticate").withHttpConfig({
					transformRequest: angular.identity
				})
					.customPOST("username=" + $scope.user.username + "&password=" + $scope.user.password, undefined, undefined, {
						'Content-Type': "application/x-www-form-urlencoded"
					}).then(function(tokenTransfer) {
						var tokenTransfer = Restangular.stripRestangular(tokenTransfer);
						principal.authenticate({
							name: tokenTransfer.name,
							roles: tokenTransfer.roles
						});
						localStorage.setItem("clouddataFrontendApp.token", tokenTransfer.token);
						if ($scope.returnToState) $state.go($scope.returnToState.name, $scope.returnToStateParams);
						else $state.go('app');
					});
			};
		});