'use strict';

angular.module('clouddataFrontendApp')
    .controller('RoleCtrl', function($scope, $rootScope, $state, Meta, $timeout, $location, Restangular) {
        $scope.mts = Meta.getMTS();
        $scope.roleId = {};
        $scope.newrole = {};
        $scope.roleMTs = [];
        $scope.postRoleMTs = [];
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
            $scope.roleId = branch.id;
            Restangular.all("role/mt?roleId=" + branch.id).getList().then(function(roleMTs) {
                $scope.roleMTs = Restangular.stripRestangular(roleMTs);
                $scope.roleMTMapping = [];
                if ($scope.roleMTs) {
                    for (var i = 0; i < $scope.roleMTs.length; i++) {
                        $scope.roleMTMapping[$scope.roleMTs[i].mtId] = $scope.roleMTs[i];
                    };
                }
            });
        };
        Restangular.all("role").getList().then(function(roles) {
            $scope.roleData = Restangular.stripRestangular(roles);
        });

        $scope.roleMTMapping = {};

        $scope.toggleSelection = function toggleSelection(mtcrud) {
            var mtcrudarray = mtcrud.split("_");

            if ($scope.roleMTMapping[mtcrudarray[0]]) {
                if ($scope.roleMTMapping[mtcrudarray[0]][mtcrudarray[1]]) {
                    $scope.roleMTMapping[mtcrudarray[0]][mtcrudarray[1]] = false;
                } else {
                    $scope.roleMTMapping[mtcrudarray[0]][mtcrudarray[1]] = true;
                }
            } else {
                $scope.roleMTMapping[mtcrudarray[0]] = {};
                $scope.roleMTMapping[mtcrudarray[0]][mtcrudarray[1]] = true;
            }

        };
        $scope.saveRole = function() {
            Restangular.all("role").post($scope.newrole).then(function(response) {
                $(".modal-backdrop").hide();
                $state.transitionTo($state.current, $stateParams, {
                    reload: true,
                    inherit: false,
                    notify: true
                });
            });
        }
        $scope.save = function() {
            var i = 0;
            var roleMTS = {
                roleId: $scope.roleId,
                roleMTs: []
            };
            var postRoleMTs = [];
            for (var key in $scope.roleMTMapping) {
                var roleMT = $scope.roleMTMapping[key];
                postRoleMTs[i] = {};
                postRoleMTs[i].mtId = key;
                postRoleMTs[i].c = roleMT.c;
                postRoleMTs[i].r = roleMT.r;
                postRoleMTs[i].u = roleMT.u;
                postRoleMTs[i].d = roleMT.d;
                i++;
            }
            roleMTS.roleMTs = postRoleMTs;
            Restangular.all("role/mt").post(roleMTS).then(function(response) {
                $(".modal-backdrop").hide();
                $state.transitionTo($state.current, $stateParams, {
                    reload: true,
                    inherit: false,
                    notify: true
                });
            });
        }
    });