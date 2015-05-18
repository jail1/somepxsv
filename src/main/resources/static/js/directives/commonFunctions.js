( function () {

    'use strict';

    angular.module('commons', []).factory('commons', commons);

    commons.$inject = ['$log', '$timeout', '$mdToast', 'localize', '$rootScope'];

    function commons($log, $timeout, $mdToast, localize, $rootScope) {
        var commonsService = {};
        //commons.displayLocalizedToast
        commonsService.displayLocalizedToast = function(localizeKey, toastType, parent) {
            var aToast = {
                template: '<md-toast class="md-toast ' + toastType + '">' + localize.getLocalizedString(localizeKey) + '</md-toast>',
                hideDelay: 6000,
                position: 'bottom left right'
            };
            if (parent){
                aToast.parent = parent;
            }
            $mdToast.show(aToast);
        };
        //commons.displayToast
        commonsService.displayToast = function(message, toastType) {
            $mdToast.show({
                template: '<md-toast class="md-toast ' + toastType + '">' + message + '</md-toast>',
                hideDelay: 6000,
                position: 'bottom left right'
            });
        };
        //commons.log
        commonsService.log = function(message){
            if ($rootScope.isDebug){
                $log.log(message);
            }
        }

        // empty function to be copied around
        commonsService.test = function () {
        };
        //
        return commonsService;
    }
}());