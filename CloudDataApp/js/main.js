(function($, owner) {
	owner.getMTS = function() {
		return JSON.parse(localStorage.getItem('$clouddataFrontendApp.metaData') || '[]');
	};
	
}(mui, window.main = {}));