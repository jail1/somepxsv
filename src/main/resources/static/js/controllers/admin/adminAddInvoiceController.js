(function () {
    "use strict";
    angular.module('photoX').controller('adminAddInvoiceCtrl', adminAddInvoiceCtrl);

    adminAddInvoiceCtrl.$inject = ['$scope', '$rootScope', '$timeout', '$log', 'moment', '$mdDialog', '$mdSidenav', '$mdBottomSheet', 'keyboardManager'];

    function adminAddInvoiceCtrl($scope, $rootScope, $timeout, $log, moment, $mdDialog, $mdSidenav, $mdBottomSheet, keyboardManager) {

        $scope.$on('$viewContentLoaded',
            function (event) {
                $log.log("Content LOADED")
            });
        $log.log("Admin adminAddInvoiceCtrl controller");
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
        keyboardManager.bind('ctrl+a', function () {
            $log.log('Callback ctrl+a in clients');
        });
        // Bind ctrl+shift+d
        keyboardManager.bind('ctrl+shift+d', function () {
            $log.log('Callback ctrl+shift+d in clients');
        });

        $scope.$on('$destroy', function () {
            $log.log("Destroying");
            keyboardManager.unbindAll();
        });
    }
}).call(this);