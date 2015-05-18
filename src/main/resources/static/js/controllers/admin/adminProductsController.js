(function () {
    "use strict";
    angular.module('photoX').controller('adminProductsCtrl', adminProductsCtrl);

    adminProductsCtrl.$inject = ['$scope', '$rootScope', '$timeout', 'commons', '$state', 'localize', '$mdDialog', '$cookies', 'ProductsService'];

    function adminProductsCtrl($scope, $rootScope, $timeout, commons, $state, localize, $mdDialog, $cookies, ProductsService) {

        $scope.init = function () {
            $scope.loading = true;
            $scope.search = {};
            $scope.search.isListable = true;
            ProductsService.getAllProducts().then(function (response) {
                angular.forEach(response, function (product) {
                    product.extraData = angular.fromJson(product.extraData);
                });
                $scope.products = response;
                $timeout(function () {
                    $scope.loading = false;
                    if ($rootScope.isDebug) {
                        if ($cookies.productId) {
                            var product = _.findWhere(response, {id: $cookies.productId});
                            if (product.isComplex) {
                                $state.go('materials.addOrEditComposedProduct', {productId: product.id});
                            } else {
                                $state.go('materials.addOrEditSimpleProduct', {productId: product.id});
                            }
                        }
                    }
                }, 1000);
            });

            ProductsService.getProducts().then(function (response) {
                commons.log("Client's products");
                commons.log(response);
            });
        }
        //creates a product
        $scope.createProduct = function (ev) {
            ev.stopImmediatePropagation();
            var question = $mdDialog.confirm()
                .parent(angular.element(document.body))
                .title(localize.getLocalizedString('_CreateProductQuestion_'))
                .ariaLabel('Create product')
                .ok(localize.getLocalizedString('_SimpleProduct_'))
                .cancel(localize.getLocalizedString('_ComplexProduct_')).
                targetEvent(ev);
            $mdDialog.show(question).then(function () {
                $state.go('materials.addOrEditSimpleProduct');
            }, function () {
                $state.go('materials.addOrEditComposedProduct');
            });
        }
        //goes to edit product
        $scope.editProduct = function (ev, product) {
            commons.log(product);
            if (product.isComplex) {
                $state.go('materials.addOrEditComposedProduct', {productId: product.id});
            } else {
                $state.go('materials.addOrEditSimpleProduct', {productId: product.id});
            }
        }

        //deletes a product
        $scope.askQuestionThenRemoveProduct = function (ev, product) {
            ev.stopImmediatePropagation();
            var confirm = $mdDialog.confirm()
                .parent(angular.element(document.body))
                .title(localize.getLocalizedString('_DeleteQuestion_', product.name, ''))
                .ariaLabel('Delete')
                .ok(localize.getLocalizedString('_Yes_'))
                .cancel(localize.getLocalizedString('_No_')).
                targetEvent(ev);
            $mdDialog.show(confirm).then(function () {

                ProductsService.deleteProduct(product.id).then(function (response) {
                    //commons.log("product deleted" , ProductsService.products);
                    $scope.products = _.without($scope.products, product);
                });
            }, function () {
                //commons.log("NOT Removing");
            });
        }

        $scope.cloneProduct = function (event, product) {
            var dialogScope = $scope.$new();
            dialogScope.product = angular.copy(product);
            $mdDialog.show({
                controller: DialogController,
                templateUrl: 'cloneProductTemplateDialog.html',
                targetEvent: event,
                scope: dialogScope
            }).then(function (clonedProduct) {
                ProductsService.cloneProduct(clonedProduct.id, [clonedProduct.name, clonedProduct.extraData.width, clonedProduct.extraData.height]).then(function (response) {
                    $scope.init();
                });
            }, function () {
                //commons.log('You cancelled the dialog.');
            });
        }
        $scope.init();
    }

    function DialogController($scope, $mdDialog) {
        $scope.cancel = function () {
            $mdDialog.cancel();
        }
        $scope.answer = function (data) {
            $mdDialog.hide(data);
        }
    }
}).
    call(this);