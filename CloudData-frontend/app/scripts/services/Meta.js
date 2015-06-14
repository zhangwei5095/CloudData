'use strict';
angular.module('clouddataFrontendApp')
 .factory('Meta',function(){
 	var mts={};
 	var mid={};
 	var metaService={};
 	metaService.loadMTS =function (p_mts){
 		angular.forEach(p_mts, function(mt) {
 			mts[mt.id]=mt;
 		})
 	}
 	metaService.getMTS=function(){
 		return mts;
 	}
 	metaService.getMTByMid = function (p_mid){
 		return mts[p_mid];
 	}

 	metaService.getMFSByMid = function (p_mid){
 		return mts[p_mid].mfs;
 	}

 	metaService.getViewsByMid =function(p_mid){
 		return mts[p_mid].views;
 	}

 	metaService.selectMid=function (p_mid){
 		mid=p_mid;
 	}

 	metaService.getMFS= function (){
 		return this.getMFSByMid(mid);
 	}

 	metaService.getViews = function(){
 		return this.getViewsByMid(mid);
 	}

 	function getViewByVid(vid){
 		var views=getViews();
 		angular.forEach(views, function(view) {
 			if(vid===view.id)
 				return view;
 		})	
 	}

 	function getMFByKey(key){
 		var mfs=getMFS();
 		angular.forEach(mfs, function(mf) {
 			if(key===mf.key)
 				return mf;
 		})
 	}

 	return metaService;
 });