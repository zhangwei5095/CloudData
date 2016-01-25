'use strict';
angular.module('clouddataFrontendApp')
	.factory('Meta', function($rootScope, $q, $timeout, Restangular) {
		var mts = null;
		return {
			getMTS: function() {
				if (!mts) mts = angular.fromJson(store.get("clouddataFrontendApp.metaData"));
				return mts;
			},
			setMTS: function() {
				var deferred = $q.defer(); 
				Restangular.all('mt').getList().then(function(data) {
					var mtData = Restangular.stripRestangular(data);
					mts = {};
					angular.forEach(mtData, function(mt) {
						mts[mt.id] = mt;
					})
					store.set("clouddataFrontendApp.metaData", angular.toJson(mts));
					deferred.resolve();
				})
				return deferred.promise;
			},
			getMTByMid: function(p_mid) {
				return mts[p_mid];
			},

			getMFSByMid: function(p_mid) {
				return mts[p_mid].mfs;
			},

			getViewsByMid: function(p_mid) {
				return mts[p_mid].views;
			},

			selectMid: function(p_mid) {
				mid = p_mid;
			},
			getMFS: function() {
				return this.getMFSByMid(mid);
			},

			getViews: function() {
				return this.getViewsByMid(mid);
			},

			getViewByMidVid: function(mid, vid) {
				var _view;
				angular.forEach(this.getViewsByMid(mid), function(view) {
					if (vid === view.id) {
						_view = view;
						return _view;
					}
				})
				return _view;
			},
			addView: function(p_mid, p_view) {
				var metadata = angular.fromJson(store.get("clouddataFrontendApp.metaData"));
				if (metadata) {
					metadata[p_mid].views.push(p_view);
					store.set("clouddataFrontendApp.metaData", angular.toJson(metadata));
				}

			},
			getMFByKey: function(key) {
				var mfs = getMFS();
				angular.forEach(mfs, function(mf) {
					if (key === mf.key)
						return mf;
				})
			}
		};
	});