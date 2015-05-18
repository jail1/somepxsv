(function() {
    "use strict";

    angular.module('photoX').service('UsersService', usersService);

    usersService.$inject = ['$http', '$q', 'APP_URL', '$rootScope'];//, 'PleaseWaitService'];

    function usersService($http, $q, APP_URL, $rootScope){//, pws) {

        function getUsersPagedAndOrdered(paginationData) {
            //pws.show();
            var d = $q.defer();
            $http.post(APP_URL + '/getUsersPagedAndOrdered', paginationData).success(function (response, status, headers, config) {
                //pws.hide();
                d.resolve(response);
            }).error(function(){
                //pws.hide();
            });
            return d.promise;
        }
        
        function createAccount(emailAddress){
            var d = $q.defer();
            //pws.show();
            $http.post(APP_URL + '/inviteAccount', emailAddress).success(function(response){
                //pws.hide();
                d.resolve(response);
            });
            return d.promise;
        }

        function createUserAccount(user){
            var d = $q.defer();
            $http.post(APP_URL + '/createAccount', user).success(function(response){
                d.resolve(response);
            });
            return d.promise;
        }
        
        function getUsersById(userId) {
            var d = $q.defer();
            $http.get(APP_URL + '/getUserById/' + userId).success(function (response) {
                d.resolve(response);
            });
            return d.promise;
        }
        
        function getAllUsers(){
            var d = $q.defer();
            $http.get(APP_URL + '/getAllUsers').success(function(response){
                d.resolve(response);
            });
            return d.promise;
        }

        function getRefereeList(){
            var d = $q.defer();
            $http.get(APP_URL + '/getRefereeList').success(function(response){
                d.resolve(response);
            });
            return d.promise;
        }
        
        function igniteAdmin(){
            var d = $q.defer();
            $http.get(APP_URL + '/igniteAdmin').success(function(response){
                d.resolve(response);
            });
            return d.promise;
        }
        
        function search(term){
            var d = $q.defer();
            $http.get(APP_URL + '/searchUsers',{params:{fragment:term}}).success(function(response){
                d.resolve(response);
            });
            return d.promise;
        }
        
        function deleteUser(user){
            var d = $q.defer();
            $http.delete(APP_URL + '/deleteUser',{data:user}).success(function(response){
                d.resolve(response);
            }).error(function(rejection){
                d.resolve(rejection);
            });
            return d.promise;
        }
        
        function setRoleForUser(userAndRole){
            var d = $q.defer();
            $http.post(APP_URL + '/setRoleForUser', userAndRole).success(function(response){
                d.resolve(response);
            }).error(function(rejection){
                d.resolve(rejection);
            });
            return d.promise;
        }
        
        function setLanguageForLoggedInUser(language){
            var d = $q.defer();
            $http.post(APP_URL + '/setLanguageForLoggedInUser', language).success(function(response){
                d.resolve(response);
            }).error(function(rejection){
                d.resolve(rejection);
            });
            return d.promise;
        }
        
        function updateUser(user){
            var d = $q.defer();
            $http.post(APP_URL + '/updateUser', user).success(function(response){
                d.resolve(response);
            }).error(function(rejection){
                d.resolve(rejection);
            });
            return d.promise;
        }

        function updateUserProfile(){
            var d = $q.defer();
            $http.post(APP_URL + '/updateUserProfile', $rootScope.loggedInUser).success(function(response){
                d.resolve(response);
            }).error(function(rejection){
                d.resolve(rejection);
            });
            return d.promise;
        }
        //TODO : remove access to this in production
        function setParentOfUser(childId, parentId){
            var d = $q.defer();
            $http.post(APP_URL + '/setParentOfUser/'+childId+"/"+parentId).success(function(response){
                d.resolve(response);
            }).error(function(rejection){
                d.resolve(rejection);
            });
            return d.promise;
        }

        function logout(){
            $http.post("/logout");
        }

        var service = {
            getRefereeList : getRefereeList,
            setParentOfUser : setParentOfUser,
            getUsersPagedAndOrdered: getUsersPagedAndOrdered,
            createAccount : createAccount,
            createUserAccount : createUserAccount,
            getUsersById : getUsersById,
            getAllUsers : getAllUsers,
            igniteAdmin : igniteAdmin,
            search : search,
            deleteUser : deleteUser,
            setRoleForUser : setRoleForUser,
            setLanguageForLoggedInUser : setLanguageForLoggedInUser,
            updateUser : updateUser,
            updateUserProfile : updateUserProfile,
            logout : logout
        };

        return service;
    }
}).call(this);