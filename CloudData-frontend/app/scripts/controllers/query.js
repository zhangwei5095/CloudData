'use strict';

angular.module('clouddataFrontendApp')
	.controller('QueryCtrl', function($scope, $rootScope, $routeParams, $http, Restangular) {
		var dc_options = '';
		var qb_filters = [];
		angular.forEach($rootScope.mfs, function(mf) {
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
			dc_options += '<option value="' + mf.key + '">' + mf.label + '</option>';
		});
		var rules_basic = null;

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
			var view={
				viewName:$scope.rule.ruleName,
				rules:JSON.stringify($('#builder').queryBuilder('getRules'), null, 2),
				mongoScript:JSON.stringify($('#builder').queryBuilder('getMongo'), null, 2),
				displayColumn:$('select[name="displayColumn"]').val().join(',')
			};
			Restangular.all('mt/view').post(view, {
				mtid: $rootScope.mid
			}).then(function(response) {

			});
		});


		var displayColumn = $('select[name="displayColumn"]').bootstrapDualListbox();
		displayColumn.append(dc_options);
		displayColumn.bootstrapDualListbox('refresh');
	});