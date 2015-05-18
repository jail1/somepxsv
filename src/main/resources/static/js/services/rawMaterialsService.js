(function () {
    "use strict";

    angular.module('photoX').service('RawMaterialsService', RawMaterialsService);

    RawMaterialsService.$inject = ['$http', '$q', 'APP_URL'];

    function RawMaterialsService($http, $q, APP_URL) {

        function getAllMaterialsGrouped() {
            var d = $q.defer();
            $http.get(APP_URL + '/getAllMaterialsGrouped').success(function (response) {
                d.resolve(response);
            });
            return d.promise;
        }

        function createOrUpdateRawMaterial(rawGroupId, rawMaterial) {
            var d = $q.defer();
            $http.post(APP_URL + '/createOrUpdateRawMaterial/' + rawGroupId, rawMaterial).success(function (response) {
                d.resolve(response);
            }).error(function (rejection) {
                d.resolve(rejection);
            });
            return d.promise;
        }

        function createOrUpdateRawMaterialGroup(group) {
            var d = $q.defer();
            $http.post(APP_URL + '/createOrUpdateRawMaterialGroup', group).success(function (response) {
                d.resolve(response);
            }).error(function (rejection) {
                d.resolve(rejection);
            });
            return d.promise;
        }

        function transferRawMaterial(materialId, toGroupId) {
            var d = $q.defer();
            $http.post(APP_URL + '/transferRawMaterial/' + materialId + "/" + toGroupId).success(function (response) {
                d.resolve(response);
            }).error(function (rejection) {
                d.resolve(rejection);
            });
            return d.promise;
        }

        function deleteRawMaterial(rawMaterialId) {
            var d = $q.defer();
            $http.delete(APP_URL + '/deleteRawMaterial', {data: rawMaterialId}).success(function (response) {
                d.resolve(response);
            }).error(function (rejection) {
                d.resolve(rejection);
            });
            return d.promise;
        }

        function deleteRawMaterialGroup(groupId) {
            var d = $q.defer();
            $http.delete(APP_URL + '/deleteRawMaterialGroup', {data: groupId}).success(function (response) {
                d.resolve(response);
            }).error(function (rejection) {
                d.resolve(rejection);
            });
            return d.promise;
        }

        var service = {
            createOrUpdateRawMaterialGroup: createOrUpdateRawMaterialGroup,
            deleteRawMaterialGroup: deleteRawMaterialGroup,
            getAllMaterialsGrouped: getAllMaterialsGrouped,
            createOrUpdateRawMaterial: createOrUpdateRawMaterial,
            transferRawMaterial: transferRawMaterial,
            deleteRawMaterial: deleteRawMaterial

        }
        return service;
    }
}).call(this);