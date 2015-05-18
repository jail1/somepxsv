(function () {
    "use strict";
    angular.module('photoX').controller('adminRawProductsController', adminRawProductsController);

    adminRawProductsController.$inject = ['$scope', 'RawMaterialsService', '$timeout', '$mdDialog', 'localize', '_', 'commons'];

    function adminRawProductsController($scope, RawMaterialsService, $timeout, $mdDialog, localize, _, commons) {
        $scope.currencyLbl = "EUR";
        //just to make it nice... we're letting the progress be displayed for a second
        $scope.init = function () {
            $scope.loading = true;
            RawMaterialsService.getAllMaterialsGrouped().then(function (response) {
                $scope.groups = response;
                $timeout(function () {
                    $scope.loading = false;
                }, 1000);
            });
        }
        //creates a group of materials
        $scope.createGroup = function (ev) {
            var dialogScope = $scope.$new();
            dialogScope.group = {name: '', isRequiredInEveryProduct: false, isSelector: false};
            $mdDialog.show({
                controller: DialogController,
                templateUrl: 'createGroupTemplateDialog.html',
                targetEvent: ev,
                scope: dialogScope
            }).then(function (group) {
                //TODO : validate
                RawMaterialsService.createOrUpdateRawMaterialGroup(group).then(function (response) {
                    //commons.log("Saved to server", response);
                    $scope.groups.push(response);
                    $scope.$applyAsync(function () {
                        commons.displayLocalizedToast('_Success_', 'success');
                        $scope.selectedTabIndex = $scope.groups.length - 1;
                    });
                })
            }, function () {
                //commons.log('You cancelled the dialog.');
            });
        }
        //deletes a group (including the materials inside
        $scope.deleteGroup = function (ev, group) {
            event.stopImmediatePropagation();
            var confirm = $mdDialog.confirm()
                .parent(angular.element(document.body))
                .title(localize.getLocalizedString('_DeleteQuestion_', group.name, ''))
                .ariaLabel('Delete')
                .ok(localize.getLocalizedString('_Yes_'))
                .cancel(localize.getLocalizedString('_No_')).
                targetEvent(event);
            $mdDialog.show(confirm).then(function () {
                RawMaterialsService.deleteRawMaterialGroup(group.id).then(function (response) {
                    //commons.log("Group deleted");
                    $scope.groups = _.without($scope.groups, group);
                })
            }, function () {
                //commons.log("NOT Removing");
            });
        }
        var saveMaterial = function (forGroup, rawMaterial) {
            //TODO : validate
            RawMaterialsService.createOrUpdateRawMaterial(forGroup.id, rawMaterial).then(function (response) {
                //commons.log("Saved to server", response);
                if (rawMaterial.id && rawMaterial.id > 0) {
                    _.replaceInCollection(forGroup.rawMaterials, rawMaterial, response);
                    //commons.log(rawMaterial);
                }
                else {
                    if (!forGroup.rawMaterials) {
                        forGroup.rawMaterials = [];
                    }
                    forGroup.rawMaterials.push(response);
                }
                commons.displayLocalizedToast('_Success_', 'success');
            })
        }
        //create fresh material
        $scope.createRawMaterialInGroup = function (ev, group) {
            var dialogScope = $scope.$new();
            dialogScope.rawMaterial = {
                name: "",
                description: "",
                aquisitionPrice: parseFloat(0.0),
                aquisitionUnits: parseFloat(0.0),
                pricePerProductionUnit: parseFloat(0.0),
                unitsPerProductionUnit: parseFloat(0.0)
            };
            $mdDialog.show({
                controller: DialogController,
                templateUrl: 'createRawMaterialTemplateDialog.html',
                targetEvent: ev,
                scope: dialogScope
            }).then(function (rawMaterial) {
                saveMaterial(group, rawMaterial);
            }, function () {
                //commons.log('You cancelled the dialog.');
            });
        }
        //deletes a material
        $scope.askQuestionThenRemoveMaterial = function (event, rawMaterial, group) {
            event.stopImmediatePropagation();
            var confirm = $mdDialog.confirm()
                .parent(angular.element(document.body))
                .title(localize.getLocalizedString('_DeleteQuestion_', rawMaterial.name, ''))
                .ariaLabel('Delete')
                .ok(localize.getLocalizedString('_Yes_'))
                .cancel(localize.getLocalizedString('_No_')).
                targetEvent(event);
            $mdDialog.show(confirm).then(function () {
                RawMaterialsService.deleteRawMaterial(rawMaterial.id).then(function (response) {
                    commons.log("Removed!");
                    //might need findWhere
                    group.rawMaterials = _.without(group.rawMaterials, rawMaterial);
                })
            }, function () {
                //commons.log("NOT Removing");
            });
        };
        //edits a material
        $scope.editMaterial = function (ev, rawMaterial, group) {
            ev.stopImmediatePropagation();
            var dialogScope = $scope.$new();
            dialogScope.rawMaterial = angular.copy(rawMaterial);
            $mdDialog.show({
                controller: DialogController,
                templateUrl: 'createRawMaterialTemplateDialog.html',
                targetEvent: ev,
                scope: dialogScope
            }).then(function (rawMaterial) {
                saveMaterial(group, rawMaterial);
            }, function () {
            });
        }
        $scope.init();
    }

    function DialogController($scope, $mdDialog, commons, $rootScope) {
        $scope.cancel = function () {
            $mdDialog.cancel();
        }
        $scope.answer = function (data) {
            if ($scope.rawMaterialForm) {
                if ($scope.rawMaterialForm.$error.required || $scope.rawMaterialForm.$error.number) {
                    //commons.log('You have errors');
                    commons.displayLocalizedToast('_Errors_', 'error');
                    return;
                }
            }
            $mdDialog.hide(data);
        }
        //automatically calculates price for 1mp when something changes
        $scope.calculatePrice = function () {
            if (
                $scope.rawMaterial.aquisitionPrice <= 0 ||
                $scope.rawMaterial.aquisitionUnits <= 0 ||
                $scope.rawMaterial.unitsPerProductionUnit <= 0) {
                //commons.log("Can't calculate");
                return;
            }
            try {
                var calculatedPrice = parseFloat(($scope.rawMaterial.aquisitionPrice * $scope.rawMaterial.unitsPerProductionUnit / $scope.rawMaterial.aquisitionUnits).toFixed($rootScope.serverSettings.applicationDefaultDecimals));
                if (!isNaN(calculatedPrice)) {
                    //to avoid showing NaN
                    $scope.rawMaterial.pricePerProductionUnit = calculatedPrice;
                }
            } catch (ex) {
                commons.displayToast(ex, 'error');
            }
        }
        //calculate price immediately after loading
        if ($scope.rawMaterial) {
            $scope.calculatePrice();
        }
    }

}).call(this);