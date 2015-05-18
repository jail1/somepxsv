(function() {
    "use strict";
    angular.module('photoX').controller('adminDashboardCtrl', adminDashboardCtrl);

    adminDashboardCtrl.$inject = ['$scope', '$rootScope', '$timeout', '$log', 'moment', '$mdDialog', '$mdSidenav', '$mdBottomSheet', 'keyboardManager'];

    function adminDashboardCtrl($scope, $rootScope, $timeout, $log, moment, $mdDialog, $mdSidenav, $mdBottomSheet, keyboardManager){
        $log.log("Admin dashboard controller");
        $log.log($rootScope.loggedInUser);
        $scope.loadUsers = function () {
            // Use timeout to simulate a 650ms request.
            $scope.users = [];
            return $timeout(function () {
                $scope.users = [
                    {id: 1, name: 'Scooby Doo'},
                    {id: 2, name: 'Shaggy Rodgers'},
                    {id: 3, name: 'Fred Jones'},
                    {id: 4, name: 'Daphne Blake'},
                    {id: 5, name: 'Velma Dinkley'},
                ];
            }, 650);
        };
        // Bind ctrl+a
        keyboardManager.bind('ctrl+a', function() {
            $log.log('Callback ctrl+a (dashboard)');
        });
        // Bind ctrl+shift+d
        keyboardManager.bind('ctrl+shift+d', function() {
            $log.log('Callback ctrl+shift+d (dashboard)');
        });

        $scope.$on('$destroy', function(){
           keyboardManager.unbindAll();
        });
    }
}).call(this);