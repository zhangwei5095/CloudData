'use strict';

angular.module('clouddataFrontendApp')
	.controller('RoleCtrl', function($scope, $rootScope, $routeParams, $timeout, Restangular) {
		$scope.roleMTs = [];
		$scope.roleData = [];
		$scope.gridOptions = {
			data: 'roleResource',
			columnDefs: [{
				field: 'name',
				displayName: '用户名'
			}, {
				field: 'orgId',
				displayName: '机构号'
			}, {
				field: 'roles',
				displayName: '角色'
			}]
		};
		$scope.myTreeHandler = function(branch) {
			$scope.role.id=branch.id;
			Restangular.all("role/mt?roleId="+branch.id).getList().then(function(roleMTs) {
				$scope.roleMTs = Restangular.stripRestangular(roleMTs);
			});
		};
		Restangular.all("role").getList().then(function(roles) {
				$scope.roleData = Restangular.stripRestangular(roles);
			});
		
		$scope.roleMTMapping = {};
		$scope.checked = function(mtcrud) {
            if ($scope.roleMTs) {
                for (var i = 0; i < $scope.roleMTs.length; i++) {
                    $scope.roleMTMapping[$scope.roleMTs[i].mtId] = $scope.roleMTs[i];
                };
            }
            var mtcrudarray = mtcrud.split("_");
            if ($scope.roleMTMapping[mtcrudarray[0]]) {
                return ($scope.roleMTMapping[mtcrudarray[0]].c && "c" === mtcrudarray[1]) ||
                    ($scope.roleMTMapping[mtcrudarray[0]].r && "r" === mtcrudarray[1]) ||
                    ($scope.roleMTMapping[mtcrudarray[0]].u && "u" === mtcrudarray[1]) ||
                    ($scope.roleMTMapping[mtcrudarray[0]].d && "d" === mtcrudarray[1]);
            }
        };
		$scope.toggleSelection = function toggleSelection(mtcrud) {
            var mtcrudarray = mtcrud.split("_");

            if ($scope.roleMTMapping[mtcrudarray[0]]) {
                if ($scope.roleMTMapping[mtcrudarray[0]][mtcrudarray[1]]) {
                    $scope.roleMTMapping[mtcrudarray[0]][mtcrudarray[1]] = false;
                } else {
                    $scope.roleMTMapping[mtcrudarray[0]][mtcrudarray[1]] = true;
                }
            } else {
                $scope.roleMTMapping[mtcrudarray[0]]={};
                $scope.roleMTMapping[mtcrudarray[0]].mt={};
                $scope.roleMTMapping[mtcrudarray[0]].mt.id=mtcrudarray[0];
                $scope.roleMTMapping[mtcrudarray[0]][mtcrudarray[1]] = true;
            }

        };
	});