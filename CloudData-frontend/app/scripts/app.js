'use strict';

/**
 * @ngdoc overview
 * @name clouddataFrontendApp
 * @description
 * # clouddataFrontendApp
 *
 * Main module of the application.
 */
angular
  .module('clouddataFrontendApp', [
    'ngAnimate',
    'ngCookies',
    'ngResource',
    'ngRoute',
    'ngSanitize',
    'ngTouch',
    'formly',
    'ui.bootstrap',
    'ui.select',
    'restangular',
    'ui.grid',
    'ui.grid.pagination',
    'angularBootstrapNavTree',
    'http-auth-interceptor'
  ]).config(function($routeProvider) {
    $routeProvider
      .when('/main', {
        templateUrl: 'views/main.html',
        controller: 'MainCtrl'
      })
      .when('/about', {
        templateUrl: 'views/about.html',
        controller: 'AboutCtrl'
      })
      .when('/formly/:mid', {
        templateUrl: 'views/formly.html',
        controller: 'FormlyCtrl'
      })
      .when('/schemaForm', {
        templateUrl: 'views/schemaform.html',
        controller: 'FormCtrl'
      })
      .when('/autoField', {
        templateUrl: 'views/autofield.html',
        controller: 'AutoFieldCtrl'
      })
      .when('/react', {
        templateUrl: 'views/react.html',
        controller: 'ReactCtrl'
      })
      .when('/jsx', {
        templateUrl: 'views/jsx.html',
        controller: 'JsxCtrl'
      })
      .when('/data/:mid', {
        templateUrl: 'views/grid.html',
        controller: 'DataCtrl'
      })
      .when('/user', {
        templateUrl: 'views/user.html',
        controller: 'UserCtrl'
      })
      .when('/role', {
        templateUrl: 'views/role.html',
        controller: 'RoleCtrl'
      })
      .when('/org', {
        templateUrl: 'views/org.html',
        controller: 'OrgCtrl'
      })
      .when('/mt', {
        templateUrl: 'views/mt.html',
        controller: 'MtCtrl'
      })
      .when('/login', {
        templateUrl: 'views/login.html',
        controller: 'LoginCtrl'
      })
      .otherwise({
        redirectTo: '/'
      });


  }).config(function(formlyConfigProvider) {
    var templates = '/views/fields/';
    var formly = templates + 'formly-field-';
    var fields = [
      'checkbox',
      'email',
      'hidden',
      'number',
      'password',
      'radio',
      'select',
      'text',
      'textarea',
      'multiselect',
      'date',
      'time'
    ];

    angular.forEach(fields, function(val) {
      formlyConfigProvider.setTemplateUrl(val, formly + val + '.html');
    });

  }).config(function(uiSelectConfig) {
    //uiSelectConfig.search-enabled=true;
    uiSelectConfig.theme = 'bootstrap';
    uiSelectConfig.resetSearchInput = true;
  }).config(function(timepickerConfig) {
    timepickerConfig.showSeconds = true;
  }).config(function(RestangularProvider) {
    RestangularProvider.setBaseUrl('http://localhost:8080/rest/');
  }).config(function($httpProvider) {
    /* Register error provider that shows message on failed requests or redirects to login page on
     * unauthenticated requests */
    $httpProvider.interceptors.push(function($q, $rootScope, $location) {
      return {
        'responseError': function(rejection) {
          var status = rejection.status;
          var config = rejection.config;
          var method = config.method;
          var url = config.url;

          if (rejection.status === 401) {
            $location.path("/login");
          } else {
            $rootScope.error = method + " on " + url + " failed with status " + status;
          }

          return $q.reject(rejection);
        }
      };
    });

    /* Registers auth token interceptor, auth token is either passed by header or by query parameter
     * as soon as there is an authenticated user */
    $httpProvider.interceptors.push(function($q, $rootScope, $window, $location) {
      return {
        'request': function(config) {
          var isRestCall = config.url.indexOf('rest') > 0;
          var authToken = $window.sessionStorage.getItem('token');
          if (!isRestCall && !authToken) {
            $location.path("/login");
          }
          if (isRestCall && authToken) {
            config.headers['X-Auth-Token'] = authToken;
          }
          return config || $q.when(config);
        }
      };
    });
  });