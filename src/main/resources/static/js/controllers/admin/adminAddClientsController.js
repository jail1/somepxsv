(function () {
    "use strict";
    angular.module('photoX').controller('adminAddClientsCtrl', adminAddClientsCtrl);

    adminAddClientsCtrl.$inject = ['$scope', '$rootScope', '$log', 'commons', '$stateParams', '$state', '$cookies', 'localize', 'UsersService'];

    function adminAddClientsCtrl($scope, $rootScope, $log, commons, $stateParams, $state, $cookies, localize, UsersService) {
        var isInUpdateMode = false;
        var postServerResponse = function (serverResponse) {
            $scope.user = serverResponse;
            isInUpdateMode = true;
        }
        if ($stateParams.userId) {
            if ($rootScope.isDebug) {
                $cookies.userId = parseInt($stateParams.userId);
            }
            //commons.log("Editing userId " + $stateParams.userId);
            UsersService.getUsersById($stateParams.userId).then(postServerResponse);
        } else {
            //fresh user
            $scope.user = {};
        }
        if ($rootScope.isDebug) {
            if ($cookies.userId) {
                //commons.log("Cookie based - Editing userId " + $cookies.userId);
                UsersService.getUsersById($cookies.userId).then(postServerResponse);
            }
        }
        $scope.loadUsers = function () {
            commons.log("loading ?")
            if ($scope.users.length <= 0) {
                commons.log("calling ?")
                UsersService.getAllUsers().then(function (response) {
                    commons.log(response);
                    var willContinue = true;
                    response.forEach(function (user) {
                        if (willContinue) {
                            if (user.username == $rootScope.loggedInUser.username) {
                                user.username += localize.getLocalizedString('_You_');
                            }
                        }
                    });
                    $scope.users = response;
                });
            }
        };

        var postSaveUser = function (serverResponse) {
            if (serverResponse.errorMessage) {
                commons.displayLocalizedToast(serverResponse.errorMessage, 'error');
            } else {
                commons.log("User created > ");
                commons.log(serverResponse);
                commons.displayLocalizedToast('_Success_','success');
            }
            $scope.loading = false;
        }

        $scope.saveUser = function () {
            $scope.loading = true;
            //DISABLED - WILL USE DRAG AND DROP TO REARRANGE ACCOUNTS
            /**
             if (!$scope.parentuser){
                commons.displayLocalizedToast($mdToast.simple().content(localize.getLocalizedString('_ParentUserError_')));
                return;
            }
             **/
            var errors = "";
            if ($scope.newUserForm.$error) {
                if ($scope.newUserForm.$error.required || $scope.newUserForm.$error.unavailable) {
                    if ($scope.newUserForm.$error.required) {
                        errors += "required;"
                    }
                    if ($scope.newUserForm.$error.unavailable) {
                        errors += "unavailable";
                    }
                    commons.displayLocalizedToast('_Errors_', 'error');
                    return;
                }
            }
            if ($scope.user.changePasswordTo) {
                if ($scope.user.changePasswordTo != $scope.changePasswordToVerifier) {
                    commons.displayLocalizedToast('_PasswordsNoMatch_','error');
                    return;
                }
            }
            if (isInUpdateMode) {
                UsersService.updateUser($scope.user).then(postSaveUser);
            } else {
                UsersService.createUserAccount($scope.user).then(postSaveUser);
            }
        };
        /**
         $scope.productEdited = function (product) {
            commons.log("productEdited", product);
        }
         $scope.productRemoved = function (product) {
            commons.log("productRemoved", product);
        }
         **/
        $scope.goBackToUsersList = function () {
            if ($rootScope.isDebug) {
                delete $cookies.userId;
            }
            $state.go("clients.list");
        };
        $scope.changePasswordToVerifier = "";

        $scope.users = [];
    }
}).call(this);