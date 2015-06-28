'use strict';

angular.module('clouddataFrontendApp')
	.controller('QueryCtrl', function($scope, $state, $rootScope, $stateParams, Meta, $http, Restangular) {
		var dc_options = '';
		var qb_filters = [];
		var rules_basic = null;
		var displayColumnArray = [];
		$scope.rule = {};
		$scope.mid = $stateParams.mid;
		$scope.vid = $stateParams.vid;
		$scope.view = Meta.getViewByMidVid($scope.mid, $scope.vid);
		if ($scope.view) {
			$scope.rule.ruleName = $scope.view.viewName;
			$scope.rule.displayColumn = $scope.view.displayColumn;
			displayColumnArray = $scope.rule.displayColumn.split(",");
			rules_basic = angular.fromJson($scope.view.rules);
		}
		angular.forEach(Meta.getMTByMid($scope.mid).mfs, function(mf) {
			var filter = {
				id: mf.key,
				label: mf.label
			};
			if (mf.type === 'number') {
				filter.type = 'integer';
			} else if (mf.type === 'select') {
				filter.input = 'select';
				filter.values = mf.options;
				filter.operators = ['equal', 'not_equal', 'in', 'not_in', 'is_null', 'is_not_null'];
			} else if (mf.type === 'date') {
				filter.type = 'date';
				filter.validation = {
					format: 'YYYY/MM/DD'
				};
				filter.plugin = 'datepicker';
				filter.plugin_config = {
					format: 'yyyy/mm/dd',
					todayBtn: 'linked',
					todayHighlight: true,
					autoclose: true
				};
			} else {
				filter.type = 'string';
			}
			qb_filters.push(filter);
			var isSelected = displayColumnArray.indexOf(mf.key) != -1;
			dc_options += '<option value="' + mf.key + '" ' + (isSelected ? 'selected="selected"' : '') + '>' + mf.label + '</option>';
		});

		$('#builder').queryBuilder({
			plugins: ['bt-tooltip-errors'],
			filters: qb_filters,
			rules: rules_basic
		});
		// reset builder
		$('.reset').on('click', function() {
			$('#builder').queryBuilder('reset');
			$('#result').empty().addClass('hide');
		});

		$('.save-rule').on('click', function() {
			var view = {
				viewName: $scope.rule.ruleName,
				rules: JSON.stringify($('#builder').queryBuilder('getRules'), null, 2),
				mongoScript: JSON.stringify($('#builder').queryBuilder('getMongo'), null, 2),
				displayColumn: $('select[name="displayColumn"]').val().join(',')
			};
			Restangular.all('mt/view').post(view, {
				mtid: $scope.mid,
				vid: $scope.vid
			}).then(function(response) {
				Restangular.stripRestangular(response);
				$scope.vid = response.id;
				view['id'] = $scope.vid;
				Meta.addView($scope.mid, view);
				$state.go('app.data', {
					mid: $scope.mid,
					vid: $scope.vid
				});
			});
		});

		$('.delete').on('click', function() {
			Restangular.one('mt/delete/mt', $scope.mid).one('view', $scope.vid).get().then(function(response) {

			});
		});


		var displayColumn = $('select[name="displayColumn"]').bootstrapDualListbox();
		displayColumn.append(dc_options);
		displayColumn.bootstrapDualListbox('refresh');
	});