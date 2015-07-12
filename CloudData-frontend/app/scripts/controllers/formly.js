'use strict';

/**
 * @ngdoc function
 * @name clouddataFrontendApp.controller:FormlyCtrl
 * @description
 * # FormlyCtrl
 * Controller of the clouddataFrontendApp
 */
angular.module('clouddataFrontendApp')
  .controller('FormlyCtrl', function($scope, $rootScope, $q, $timeout, $state, $stateParams, Meta, $location, Restangular) {
    $scope.mid = $stateParams.mid;
    $scope.rid = $stateParams.rid;
    $scope.formData = {};
    if ($scope.rid) {
      Restangular.one("data/r?mid=" + $scope.mid + "&rid=" + $scope.rid).get().then(function(data) {
        $scope.formData = Restangular.stripRestangular(data);
      });
    }
    $scope.formFields = [];
    $scope.fields = Meta.getMFSByMid($scope.mid);
    angular.forEach($scope.fields, function(mf) {
      var field = {
        key: mf.key,
        type: null,
        templateOptions: {
          label: mf.label,
          required:mf.required
        }
      };
      if(mf.placeholder)
        field.templateOptions.placeholder=mf.placeholder;
      switch (mf.type) {
        case "text":
          field.type = 'input';
          break;
        case "textarea":
          field.type = 'textarea';
          field.templateOptions["rows"] = 4;
          field.templateOptions["cols"] = 15;
          break;
        case "checkbox":
          field.type = 'checkbox';
          break;
        case "multiCheckbox":
          field.type = 'multiCheckbox';
          field.templateOptions["options"] = [];
          for (var i = 0; i < mf.options.length; i++) {
            field.templateOptions["options"].push({
              name: mf.options[i],
              value: mf.options[i]
            });
          }
          field.templateOptions["valueProp"] = 'value';
          field.templateOptions["labelProp"] = 'name';
          break;
        case "radio":
          field.type = 'radio';
          field.templateOptions["options"] = mf.options;
          break;
        case "select":
          field.type = 'select';
          field.templateOptions["options"] = [];
          for (var i = 0; i < mf.options.length; i++) {
            field.templateOptions["options"].push({
              name: mf.options[i],
              value: mf.options[i]
            });
          }
          break;
        case "date":
          field.type = 'datepicker';
          field.templateOptions['type'] = 'text';
          field.templateOptions['datepickerPopup'] = 'yyyy-MM-dd';
          break;
        case "time":
          field.type = 'timepicker';
          break;
        default:
          field.type = 'input';
          break;
      }
      if (mf['isunique'] && mf['isunique'] === true) {
        field = {
          key: mf.key,
          type: 'input-loader',
          templateOptions: {
            label: mf.label,
            placeholder: mf.placeholder,
            required: true,
            onKeydown: function(value, options) {
              options.validation.show = false;
            },
            onBlur: function(value, options) {
              options.validation.show = null;
            }
          },
          validators: {
            unique: {
              expression: function($viewValue, $modelValue, scope) {
                var deferred = $q.defer();
                scope.options.templateOptions.loading = true;
                $timeout(function() {
                  if ($scope.existingUsers.indexOf($viewValue) === -1)
                    deferred.resolve();
                  else
                    deferred.reject();
                  scope.options.templateOptions.loading = false;
                }, 1000);
                return deferred.promise;
              },
              message: '"' + mf.label + ' 已存在."'
            }
          },
          modelOptions: {
            updateOn: 'blur'
          }
        };
      };
      $scope.formFields.push(field);
    });
    $scope.existingUsers = [
      'john',
      'tyrion',
      'arya'
    ];
    $scope.onSubmit = function() {
      $scope.submittedData = $scope.formData;
      console.log($scope.submittedData);
      Restangular.all('data/c').post($scope.submittedData, {
        mid: $scope.mid,
        rid: $scope.rid
      }).then(function(response) {
        $state.go('app.data', {
          mid: $stateParams.mid
        });
      });
    }
  }).directive('myMessages', function() {
    return {
      templateUrl: 'custom-messages.html',
      scope: {
        options: '=myMessages'
      }
    };
  });