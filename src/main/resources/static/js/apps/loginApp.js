/**
 * Created by Badu on 4/21/2015.
 */
(function () {

    'use strict';

    angular.module('loginApp', ['ngMaterial']).controller('LoginController', LoginController);

    LoginController.$inject = ['$rootScope', '$scope', '$timeout'];

    function LoginController($rootScope, $scope, $timeout) {
        $scope.loginSubmitted = false;
        $scope.loginSubmit = function() {
            $scope.loginSubmitted = true;
            $timeout(function(){
                var forms = angular.element(document.getElementsByName("loginForm"));
                forms[0].onsubmit="return true";
                if (typeof forms[0].submit === "object") {
                    forms[0].submit.remove();
                }
                forms[0].submit();
            }, 500);
        };
    }
})();