(function () {
    "use strict";

    angular.module('photoX').service('ResourcesService', ResourcesService);

    ResourcesService.$inject = ['$http', '$q', 'APP_URL'];

    function ResourcesService($http, $q, APP_URL) {
        function listResources() {
            var d = $q.defer();
            $http.get(APP_URL + '/listResources' ).success(function (response) {
                d.resolve(response);
            });
            return d.promise;
        }

        var service = {
            listResources : listResources
        }
        return service;
    }
}).call(this);