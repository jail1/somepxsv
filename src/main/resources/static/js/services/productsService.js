(function () {
    "use strict";

    angular.module('photoX').service('ProductsService', ProductsService);

    ProductsService.$inject = ['$http', '$q', 'APP_URL'];

    function ProductsService($http, $q, APP_URL) {

        function getProductById(productId) {
            var d = $q.defer();
            $http.get(APP_URL + '/getProductById/' + productId).success(function (response) {
                d.resolve(response);
            });
            return d.promise;
        }

        function getAllProducts() {
            var d = $q.defer();
            $http.get(APP_URL + '/getAllProducts').success(function (response) {
                d.resolve(response);
            });
            return d.promise;
        }

        function getAllUnlistedProducts(exceptProductId) {
            var d = $q.defer();
            $http.get(APP_URL + '/getAllUnlistedProducts/' + exceptProductId).success(function (response) {
                d.resolve(response);
            });
            return d.promise;
        }

        function createOrUpdateProduct(product) {
            var d = $q.defer();
            $http.post(APP_URL + '/createOrUpdateProduct', product).success(function (response) {
                d.resolve(response);
            }).error(function (rejection) {
                d.resolve(rejection);
            });
            return d.promise;
        }

        function deleteProduct(productId) {
            var d = $q.defer();
            $http.delete(APP_URL + '/deleteProduct', {data: productId}).success(function (response) {
                d.resolve(response);
            }).error(function (rejection) {
                d.resolve(rejection);
            });
            return d.promise;
        }

        function cloneProduct(productId, data) {
            var d = $q.defer();
            $http.post(APP_URL + '/cloneProduct/' + productId, data).success(function (response) {
                d.resolve(response);
            }).error(function (rejection) {
                d.resolve(rejection);
            });
            return d.promise;
        }

        function addRawGroupToProduct(productId, toGroupId) {
            var d = $q.defer();
            $http.post(APP_URL + '/addRawGroupToProduct/' + productId + "/" + toGroupId).success(function (response) {
                d.resolve(response);
            }).error(function (rejection) {
                d.resolve(rejection);
            });
            return d.promise;
        }

        function removeRawGroupFromProduct(productId, groupId) {
            var d = $q.defer();
            $http.delete(APP_URL + '/removeRawGroupFromProduct/' + productId + "/" + groupId).success(function (response) {
                d.resolve(response);
            }).error(function (rejection) {
                d.resolve(rejection);
            });
            return d.promise;
        }

        function addChildToProduct(productId, childId) {
            var d = $q.defer();
            $http.post(APP_URL + '/addChildToProduct/' + productId + "/" + childId).success(function (response) {
                d.resolve(response);
            }).error(function (rejection) {
                d.resolve(rejection);
            });
            return d.promise;
        }

        function removeParentOfProduct(productId) {
            var d = $q.defer();
            $http.post(APP_URL + '/removeParentOfProduct/' + productId).success(function (response) {
                d.resolve(response);
            }).error(function (rejection) {
                d.resolve(rejection);
            });
            return d.promise;
        }

        function storeSellingPrice(productId, materialId, priceValue) {
            var d = $q.defer();
            $http.post(APP_URL + '/storeSellingPrice/' + productId + "/" + materialId, priceValue).success(function (response) {
                d.resolve(response);
            }).error(function (rejection) {
                d.resolve(rejection);
            });
            return d.promise;
        }

        function storeMinQuantity(productId, quantity) {
            var d = $q.defer();
            $http.post(APP_URL + '/storeMinQuantity/' + productId , quantity).success(function (response) {
                d.resolve(response);
            }).error(function (rejection) {
                d.resolve(rejection);
            });
            return d.promise;
        }
        //gets only listed products, for agencies and end-users
        function getProducts() {
            var d = $q.defer();
            $http.get(APP_URL + '/getProducts').success(function (response) {
                d.resolve(response);
            });
            return d.promise;
        }

        var service = {
            getProductById: getProductById,
            getAllProducts: getAllProducts,
            getAllUnlistedProducts: getAllUnlistedProducts,
            createOrUpdateProduct: createOrUpdateProduct,
            addRawGroupToProduct: addRawGroupToProduct,
            removeRawGroupFromProduct: removeRawGroupFromProduct,
            deleteProduct: deleteProduct,
            cloneProduct: cloneProduct,
            addChildToProduct: addChildToProduct,
            removeParentOfProduct: removeParentOfProduct,
            storeSellingPrice: storeSellingPrice,
            storeMinQuantity : storeMinQuantity,
            getProducts : getProducts
        }
        return service;
    }
}).call(this);