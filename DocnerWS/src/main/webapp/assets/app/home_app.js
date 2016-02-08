var navegate = angular.module('menu_app', [ 'ngRoute' ]);

navegate.controller('menuNavigation', function($scope) {
	var data = JSON.parse(localStorage["menuList"]);
	$scope.items = data;
});

navegate.config([ '$routeProvider', function($routeProvider) {
	var menuList = getMenuList();	
	localStorage["menuList"] = JSON.stringify(menuList);
	
	if(menuList != undefined){
		for(i=0; i<menuList.length; i++){
			if(menuList[i].subMenus != undefined){
				for(j=0; j<menuList[i].subMenus.length; j++){
					$routeProvider.when(menuList[i].subMenus[j].view, {
						templateUrl : menuList[i].subMenus[j].page,
						controller : 'MenuController'
					});
				}
			}
		}
	}
	
	$routeProvider.when('/inicio', {
		templateUrl : 'pages/private/view/inicio.html',
		controller : 'MenuController'
	});
	$routeProvider.otherwise({
		redirectTo : '/inicio'
	});
}]);

navegate.controller('MenuController', function($scope) {
	 $scope.parameters = {message: 'message'};
});

function getMenuList(){
	var menuList = [];
	$.ajax({
		type : 'POST',
		async: false,
		contentType : 'application/json',
		url : 'menu/findAllMenu',
		dataType : "json",
		success : function(data, textStatus, jqXHR) {
			menuList = data;
		},
		error : function(jqXHR, textStatus, errorThrown) {
			console.log("erro " + jqXHR.status);
		}
	});
	return menuList;
}