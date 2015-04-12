'use strict';

angular.module('cloudErpFrontendApp')
  .controller('DataCtrl', function($scope,$rootScope,$routeParams,Restangular) {
  	$scope.realData = [];
  	$scope.colDef =[];
  	Restangular.all('data/r').getList({mid: $routeParams.mid,page:1,pagesize:10}).then(function(queryData){
		$scope.realData = Restangular.stripRestangular(queryData);
		angular.forEach($rootScope.mts,function(mt){
			if(mt.id===$routeParams.mid)
				$scope.mfs=mt.mfs;
		});
		
		angular.forEach($scope.mfs,function(mf){
			 var columnDef={
			 	field:mf.key,
			 	displayName:mf.label
			 };
				
			$scope.colDef.push(columnDef);
    	});
    	
  	});

    $scope.gridOptions = { data: 'realData', columnDefs: 'colDef'} ;
  	

  });
