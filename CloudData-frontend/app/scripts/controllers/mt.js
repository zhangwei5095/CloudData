'use strict';

angular.module('clouddataFrontendApp')
	.controller('MtCtrl', function($scope, $rootScope, Meta, $timeout, $location, Restangular) {
		$scope.fields = [];
		$scope.mtData = [];
		$scope.newmt = {};
		$scope.newmf = {};
		angular.forEach(Meta.getMTS(), function(mt) {
			var mt = {
				id: mt.id,
				label: mt.name
			};
			$scope.mtData.push(mt);
		});
		$scope.myTreeHandler = function(branch) {
			$scope.mtid = branch.id;
			$scope.fields = Meta.getMFSByMid(branch.id);
		};
		$scope.saveMt = function() {
			Restangular.all('mt').post($scope.newmt).then(function(response) {

			});
		};
		$scope.saveMf = function() {
			//delete $scope.newmf.type;
			Restangular.all('mt/mf').post($scope.newmf, {
				mtid: $scope.mtid
			}).then(function(response) {

			});
		};
		$scope.field_types = [{
			name: '文本',
			value: 'text'
		}, {
			name: '数字',
			value: 'number'
		}, {
			name: '日期',
			value: 'date'
		}, {
			name: '时间',
			value: 'time'
		}, {
			name: '单选',
			value: 'select'
		}, {
			name: '多选',
			value: 'multiselect'
		}, {
			name: '自动编号',
			value: 'autocode'
		}, {
			name: '复选框',
			value: 'checkbox'
		}, {
			name: '关联对象',
			value: 'relation'
		}];
		$scope.gridOptions = {
			data: 'fields',
			columnDefs: [{
				field: 'key',
				displayName: 'key'
			}, {
				field: 'label',
				displayName: '名称'
			}, {
				field: 'required',
				displayName: '是否必填'
			}, {
				field: 'defaultValue',
				displayName: '默认值'
			}]
		};

	});