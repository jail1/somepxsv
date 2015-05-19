/**
 * Created by Badu on 2/15/2015.
 */
(function () {
    "use strict";

    angular.module('photoX').controller('schedulerOptionsController', Controller);

    angular.module('photoX').controller('ToastCtrl', function ($scope, $mdToast) {
        $scope.closeToast = function () {
            $mdToast.hide();
        };
    });

    Controller.$inject = ['$scope', '$rootScope', '$log', 'moment', 'ProductsService', '$mdDialog', '$mdSidenav', '$mdToast', '$state', '$stateParams'];

    function Controller($scope, $rootScope, $log, moment, ProductsService, $mdDialog, $mdSidenav, $mdToast, $state, $stateParams) {
        var serverResponse = function(response){
            $scope.products = response;
        }
        $scope.init = function () {
            $scope.selectedIndex = 1;
            ProductsService.getAllProducts().then(serverResponse);
        }
        $scope.showCustomToast = function () {
            $mdToast.show({
                controller: 'ToastCtrl',
                template: '<md-toast>Informatia a fost salvata! <md-icon md-svg-icon="cloud-check" style="color:white"></md-icon></md-toast>',
                hideDelay: 3000,
                position: 'bottom left right'
            });
        };
        /** Products **/
        $scope.createProduct = function (ev) {
            ev.stopImmediatePropagation();
            var question = $mdDialog.confirm()
                .parent(angular.element(document.body))
                .title(localize.getLocalizedString('_CreateProductQuestion_'))
                .ariaLabel('Create product')
                .ok(localize.getLocalizedString('_SimpleProduct_'))
                .cancel(localize.getLocalizedString('_ComplexProduct_')).
                targetEvent(event);
            $mdDialog.show(question).then(function () {
                $state.go('materials.addOrEditSimpleProduct');
            }, function () {
                $state.go('materials.addOrEditComposedProduct');
            });
        };
        $scope.productEdited = function(product){
            $log.log("productEdited", product);
        }
        $scope.productRemoved = function(product){
            $log.log("productRemoved", product);
        }
        $scope.addNewProduct = function(){
            $scope.products.unshift({id : 0 , name : 'Serviciu nou'});
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
                targetEvent(event);
            $mdDialog.show(confirm).then(function () {

                ProductsService.deleteProduct(product.id).then(function (response) {
                    //$log.log("product deleted" , ProductsService.products);
                    $scope.products = _.without($scope.products, product);
                });
            }, function () {
                //$log.log("NOT Removing");
            });
        };


        $scope.setClients = function () {
            $log.log("Clients view");
        };
        $scope.setProducts = function () {
            $log.log("Products view");
        };
        $scope.setPrices = function () {
            $log.log("Prices view");
        };
        $scope.setOthers = function () {
            $log.log("Others view");
        };
        $scope.init();
    }
}).call(this);

