(function () {
    "use strict";
    angular.module('photoX').controller('userProfileController', userProfileController);

    userProfileController.$inject = ['$scope', '$rootScope', 'commons' , 'UsersService', '$state'];

    function userProfileController($scope, $rootScope, commons, UsersService, $state) {
        var profileSaved = function(serverResponse){
            commons.log(serverResponse);
            if (serverResponse.errorMessage) {
                commons.displayLocalizedToast(serverResponse.errorMessage, 'error');
            } else {

                var keys = _.keys(serverResponse);
                _.each(keys, function(key){
                    $rootScope.loggedInUser[key] = serverResponse[key];
                });
                commons.log("Profile saved", serverResponse);
                commons.displayLocalizedToast('_Success_', 'success');
                $state.go('dashboard');
            }
        };
        $scope.save = function(){
            if ($rootScope.loggedInUser.changePasswordTo) {
                if ($rootScope.loggedInUser.changePasswordTo != $scope.changePasswordToVerifier) {
                    commons.displayLocalizedToast('_PasswordsNoMatch_','error');
                    return;
                }
            }
            UsersService.updateUserProfile().then(profileSaved);
        }
        $scope.cancel = function(){
            $state.go('dashboard');
        }
    }
}).call(this);