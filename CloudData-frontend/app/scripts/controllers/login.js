	'use strict';

	angular.module('clouddataFrontendApp')
		.controller('SigninCtrl', function($scope, $state, principal, Restangular) {
			$scope.user = {username:'admin',password:'000000'};
			$scope.newuser = {};
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
						store.set("clouddataFrontendApp.token", tokenTransfer.token);
						if ($scope.returnToState) $state.go($scope.returnToState.name, $scope.returnToStateParams);
						else $state.go('app');
					});
			};

			$scope.signup = function() {
				Restangular.all("user/signup").withHttpConfig({
					transformRequest: angular.identity
				})
					.customPOST("orgname=" + $scope.newuser.orgname + "&username=" + $scope.newuser.email + "&password=" + $scope.newuser.password, undefined, undefined, {
						'Content-Type': "application/x-www-form-urlencoded"
					}).then(function(tokenTransfer) {
						$state.go('signin');
					});
			}
		}).directive('ngUnique', ['Restangular',
			function(Restangular) {
				return {
					require: 'ngModel',
					link: function(scope, elem, attrs, ctrl) {
						elem.on('blur', function(evt) {
							scope.$apply(function() {
								Restangular.one("user/checkOrgName/" + elem.val()).get().then(function(data) {
									var unique = true;
									if (data === true)
										unique = false;
									ctrl.$setValidity('unique', unique);
								});
							});
						})
					}
				}
			}
		]);