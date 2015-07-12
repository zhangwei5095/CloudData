angular.module('starter')

// .controller('AppCtrl', function() {})
// .controller('LoginCtrl', function() {})
// .controller('DashCtrl', function() {});
.controller('AppCtrl', function($scope, $state, $ionicPopup, AuthService, AUTH_EVENTS) {
  $scope.username = AuthService.username();

  $scope.$on(AUTH_EVENTS.notAuthorized, function(event) {
    var alertPopup = $ionicPopup.alert({
      title: 'Unauthorized!',
      template: 'You are not allowed to access this resource.'
    });
  });

  $scope.$on(AUTH_EVENTS.notAuthenticated, function(event) {
    AuthService.logout();
    $state.go('login');
    var alertPopup = $ionicPopup.alert({
      title: 'Session Lost!',
      template: 'Sorry, You have to login again.'
    });
  });

  $scope.setCurrentUsername = function(name) {
    $scope.username = name;
  };
})

.controller('LoginCtrl', function($scope, $state, $ionicPopup, AuthService) {
  $scope.data = {};

  $scope.login = function(data) {
    AuthService.login(data.username, data.password).then(function(authenticated) {
      $state.go('main.home', {}, {
        reload: true
      });
      $scope.setCurrentUsername(data.username);
    }, function(err) {
      var alertPopup = $ionicPopup.alert({
        title: 'Login failed!',
        template: 'Please check your credentials!'
      });
    });
  };
})

.controller('HomeCtrl', function($scope, $rootScope, Meta, $state, $http, $ionicPopup, AuthService, Restangular) {
  $scope.logout = function() {
    AuthService.logout();
    $state.go('login');
  };
  $rootScope.menus = [];
  Meta.setMTS().then(function() {
    angular.forEach(Meta.getMTS(), function(mt) {
      var menu = {
        name: mt.name,
        href: mt.id
      };
      $rootScope.menus.push(menu);
    });
  });
  $scope.toggleLeft = function() {
    $ionicSideMenuDelegate.toggleLeft();
  };
}).controller('ListCtrl', function($scope, $rootScope, Meta, $state, $stateParams, $http, $ionicPopup, AuthService, Restangular) {
  $scope.realData = [];
  $scope.mid = $stateParams.mid;
  $scope.vid = $stateParams.vid;
  $scope.views = Meta.getViewsByMid($scope.mid);
  if (!$scope.vid) $scope.vid = $scope.views[0].id;

  // getPagedDataAsync 
  $scope.getPagedDataAsync = function(mid, vid, pageSize, page, searchText) {
    setTimeout(function() {
      var data;
      if (searchText) {
        var ft = searchText.toLowerCase();
        Restangular.all('data/rv').getList({
          mid: mid,
          vid: vid,
          page: page,
          pagesize: pageSize
        }).then(function(queryData) {
          $scope.realData = Restangular.stripRestangular(queryData);
          //$scope.setPagingData($scope.realData, page, pageSize);
        });
      } else {
        Restangular.all('data/rv').getList({
          mid: mid,
          vid: vid,
          page: page,
          pagesize: pageSize
        }).then(function(queryData) {
          var datas= Restangular.stripRestangular(queryData);
          angular.forEach(datas,function(data){
            var rowdata={};
            angular.forEach(displayColumns,function(column){
              if(data[column] && data[column]!=null)
                rowdata[column]=data[column];
            });
            $scope.realData.push(rowdata);
          })
        });
      }
    }, 100);
  };
  // display column
  var displayColumn = Meta.getViewByMidVid($scope.mid, $scope.vid).displayColumn;
  var displayColumns = displayColumn.split(',');
  $scope.columnDefs = [];
  angular.forEach(Meta.getMFSByMid($scope.mid),
    function(mf) {
      if (displayColumns.indexOf(mf.key) > 0) {
        var columnDef = {
          field: mf.key,
          displayName: mf.label
        };
        $scope.columnDefs.push(columnDef);
      }
    });
  $scope.getPagedDataAsync($scope.mid, $scope.vid, 10, 1, '');


  // table sorting
  $scope.predicate = 'descricao';
  $scope.desc = false;


  $scope.toggleLeft = function() {
    $ionicSideMenuDelegate.toggleLeft();
  };

  $scope.sort = function(key) {
    if ($scope.predicate == key)
      $scope.desc = !$scope.desc;
    else
      $scope.predicate = key;
  }

  var adjusting = false;

  $scope.scrollMirror = function(from, to) {
    if (adjusting) {
      adjusting = false;
    } else {
      // Mirroring zoom level
      var zoomFrom = $ionicScrollDelegate.$getByHandle(from).getScrollView().getValues().zoom;
      var zoomTo = $ionicScrollDelegate.$getByHandle(to).getScrollView().getValues().zoom;

      if (zoomFrom != zoomTo) {
        $ionicScrollDelegate.$getByHandle(to).getScrollView().zoomTo(zoomFrom);
      }

      // Mirroring left position
      $ionicScrollDelegate.$getByHandle(to).scrollTo($ionicScrollDelegate.$getByHandle(from).getScrollPosition().left,
        $ionicScrollDelegate.$getByHandle(to).getScrollPosition().top);

      adjusting = true;
    }
  }
});