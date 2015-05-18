(function () {
    "use strict";
    angular.module('photoX').controller('adminAddOrEditSimpleProductCtrl', adminAddOrEditSimpleProductCtrl);

    adminAddOrEditSimpleProductCtrl.$inject = ['$scope', '$rootScope', '$cookies', '$state', '$stateParams', 'localize', '$mdDialog', 'commons', 'ProductsService', 'RawMaterialsService'];

    function adminAddOrEditSimpleProductCtrl($scope, $rootScope, $cookies, $state, $stateParams, localize, $mdDialog, commons, ProductsService, RawMaterialsService) {

        var pricesCurrencyWatcher;
        var rejectedRawMaterialGroups = [];

        //post server action after raw material group was dropped
        function postRawMaterialAdded(nodeData) {
            //commons.log("nodeData.isSelector = " + nodeData.isSelector);
            if (!$scope.productHasSelector && nodeData.isSelector) {
                //commons.log("Rejecting other selectors");
                $scope.productHasSelector = true;
                $scope.mainSelectorGroup = nodeData;
                //rejects all selectors from the raw material groups tree
                $scope.rawMaterialsGroups = _.reject($scope.rawMaterialsGroups, function (rawGroup) {
                    if (rawGroup.isSelector) {
                        //commons.log("Rejecting " + rawGroup.name);
                        rejectedRawMaterialGroups.push(rawGroup);
                    }
                    return rawGroup.isSelector == true;
                });
            }
            calculateFixedTotalPrice();
            commons.displayLocalizedToast('_Success_', 'success');
        }

        //handler - raw material dropped onto our product tree
        function rawMaterialGroupDropped(event) {
            try {
                var source = event.source.nodeScope;
                var nodeData = source.$modelValue;
                var parent = event.dest.nodesScope;
                //commons.log("raw material group was dropped");
                //commons.log(parent.$treeScope.$element[0].id);
                if (parent.$treeScope.$element[0].id == "product-tree-root") {
                    ProductsService.addRawGroupToProduct($scope.product.id, nodeData.id).then(function (response) {
                        postRawMaterialAdded(nodeData);
                    });
                }
            } catch (ex) {
                commons.displayLocalizedToast(ex, 'error');
            }
        }

        //handler before dragging nodes (even if selectors are removed once one is added)
        function beforeDraggingRawMaterial(source) {
            if (!$scope.product.id || $scope.product.id == 0) {
                commons.displayLocalizedToast('_SaveProductError_', 'error');
                return;
            }
            try {
                //commons.log(source);
                source.collapse();
                //check and mark productHasSelector and return false if it has
                if ($scope.productHasSelector && source.$modelValue.isSelector) {
                    commons.displayLocalizedToast('_SelectorError_', 'error');
                    return false;
                }
                return true;
            }
            catch (ex) {
                commons.displayLocalizedToast(ex, 'error');
                return false;
            }
        }

        //post server operation - after a raw material group was deleted
        function postRawMaterialRemoval(targetNode, scope) {
            if (targetNode.isSelector) {
                //selector was removed - alter flag $scope.productHasSelector
                $scope.productHasSelector = false;
                //add rejected rawMaterials back to it's tree
                angular.forEach(rejectedRawMaterialGroups, function (group) {
                    //commons.log('Adding ' + group.name);
                    $scope.rawMaterialsGroups.push(group);
                });
                rejectedRawMaterialGroups = [];
                delete $scope.mainSelectorGroup;
            } else {
                $scope.rawMaterialsGroups.push(targetNode);
            }
            scope.remove();
            calculateFixedTotalPrice();
            commons.displayLocalizedToast('_Success_', 'success');
        }

        //calculates a grand total for all fixed costs and expose it into $scope
        function calculateFixedTotalPrice() {
            var sum = 0;
            angular.forEach($scope.productData, function (rawMaterialGroup) {
                if (!rawMaterialGroup.isSelector) {
                    angular.forEach(rawMaterialGroup.rawMaterials, function (rawMaterial) {
                        sum += parseFloat(rawMaterial.pricePerProductionUnit * $scope.currencyMultiplier * ($scope.product.ratio + $scope.product.effectiveLostRatio));
                    });
                }
            });
            $scope.totalFixedPrice = sum;
        }

        //init
        $scope.init = function () {
            $scope.decimals = $rootScope.serverSettings.applicationDefaultDecimals;
            $scope.loading = true;
            $scope.pricesCurrency = "EUR";
            $scope.currencyMultiplier = 1;
            pricesCurrencyWatcher = $scope.$watch('pricesCurrency', function (newValue) {
                if (newValue == "EUR") {
                    $scope.currencyMultiplier = 1;
                } else if (newValue == "RON") {
                    $scope.currencyMultiplier = $rootScope.serverSettings.applicationEuro;
                }
                if ($scope.product) {
                    calculateFixedTotalPrice();
                }
            });
            $scope.productHasSelector = false;
            $scope.productData = [];
            if ($rootScope.isDebug && $cookies.productId) {
                //commons.log('Forcing product id '+$cookies.productId+" in debug mode");
                ProductsService.getProductById(parseInt($cookies.productId)).then(function (response) {
                    $scope.loadProductComplete(response);
                });
            } else {
                if (!$stateParams.productId) {
                    //load the raw materials
                    $scope.getAllMaterials(false);
                } else {
                    //it's an edit operation
                    if ($rootScope.isDebug) {
                        $cookies.productId = parseInt($stateParams.productId);
                    }
                    ProductsService.getProductById(parseInt($stateParams.productId)).then(function (response) {
                        $scope.loadProductComplete(response);
                    });
                }
            }
        };
        //goes back to products list
        $scope.goToProductList = function () {
            $state.go('materials.products');
            if ($rootScope.isDebug) {
                delete $cookies.productId;
            }
        };
        //saves only product fields (children and selectors saved elsewhere
        $scope.saveProduct = function () {
            var serverProduct = {
                id: $scope.product.id,
                isListable: $scope.product.isListable,
                name: $scope.product.name,
                description: $scope.product.description,
                extraData: angular.toJson($scope.product.extraData),
                ratio: parseFloat($scope.product.extraData.width * $scope.product.extraData.height / 1000000),
                lostRatio: parseFloat($scope.product.lostRatio),
                minQuantity: parseInt($scope.product.minQuantity)
            };

            ProductsService.createOrUpdateProduct(serverProduct).then(function (response) {
                commons.displayLocalizedToast('_Success_', 'success');
                $scope.product.id = response.id;
                $scope.product.ratio = parseFloat($scope.product.extraData.width * $scope.product.extraData.height / 1000000);
                $scope.product.effectiveLostRatio = parseFloat($scope.product.lostRatio * $scope.product.extraData.width * $scope.product.extraData.height / 100000000);
                //$scope.goToProductList();
            })

        };
        //called when editing a product
        $scope.loadProductComplete = function (product) {
            $scope.product = product;
            $scope.product.extraData = angular.fromJson(product.extraData);
            if ($scope.product.ratio <= 0) {
                $scope.product.ratio = parseFloat($scope.product.extraData.width * $scope.product.extraData.height / 1000000);
            } else {
                //commons.log("Ratio : " + $scope.product.ratio);
            }
            $scope.product.effectiveLostRatio = parseFloat($scope.product.lostRatio * $scope.product.extraData.width * $scope.product.extraData.height / 100000000);
            //setting default selling prices
            angular.forEach($scope.product.selectors, function (rawMaterialGroup) {
                if (rawMaterialGroup.isSelector) {
                    $scope.productHasSelector = true;
                    $scope.mainSelectorGroup = rawMaterialGroup;
                }
            });
            $scope.productData = $scope.product.selectors;
            calculateFixedTotalPrice();
            //load the raw materials
            $scope.getAllMaterials(true);
            $scope.$applyAsync(function () {
                var treeScope = angular.element(document.getElementById("product-tree-root")).scope();
                treeScope.collapseAll();
            });
        };
        //retrieves the raw materials groups
        $scope.getAllMaterials = function (isExistingProduct) {
            RawMaterialsService.getAllMaterialsGrouped().then(function (response) {
                var validRawMaterialGroups = [];
                if (!isExistingProduct) {
                    $scope.product = {
                        id: 0,
                        isDisplayedToEndUser: false,
                        ratio: parseFloat(0.0),
                        lostRatio: parseFloat(0.0),
                        effectiveLostRatio: parseFloat(0.0),
                        extraData: {
                            width: parseInt(0),
                            height: parseInt(0)
                        },
                        minQuantity: parseInt(1)
                    }
                }
                angular.forEach(response, function (rawMaterialGroup) {
                    var willAdd = true;
                    if (rawMaterialGroup.rawMaterials.length <= 0) {
                        //commons.log('empty');
                        willAdd = false;
                    }
                    if (!isExistingProduct) {
                        if (rawMaterialGroup.isRequiredInEveryProduct) {
                            //add default materials to the product
                            $scope.productData.push(rawMaterialGroup);
                            //commons.log('in required of fresh product');
                            willAdd = false;
                        }
                    }
                    if (isExistingProduct) {
                        //it's an edit operation
                        if (rawMaterialGroup.isSelector) {
                            //current raw material group is selector
                            if ($scope.productHasSelector) {
                                //we already have a selector - rejecting all selectors
                                willAdd = false;
                                //adds it to the selectors list, so if we remove the current selector, we can add everything back
                                rejectedRawMaterialGroups.push(rawMaterialGroup);
                                if (!$scope.mainSelectorGroup) {
                                    commons.displayLocalizedToast('Fatal error : product marked as having selector, but it doesn\'t', 'error');
                                }
                                //commons.log('Selector exists in product data');
                                //restoring prices for the selector declared in product
                                angular.forEach($scope.mainSelectorGroup.rawMaterials, function (rawMaterial) {
                                    var price = _.findWhere($scope.product.prices, {materialId: rawMaterial.id});
                                    if (price) {
                                        rawMaterial.sellingPrice = {
                                            'RON': parseFloat((price.sellingPrice * $rootScope.serverSettings.applicationEuro).toFixed($scope.decimals)),
                                            'EUR': parseFloat(price.sellingPrice.toPrecision($scope.decimals))
                                        };
                                    } else {
                                        rawMaterial.sellingPrice = {
                                            'RON': 0,
                                            'EUR': 0
                                        };
                                    }

                                });
                            }
                        } else {
                            var existsInProductsSelectorList = _.findWhere($scope.productData, {id: rawMaterialGroup.id});
                            if (existsInProductsSelectorList) {
                                willAdd = false;
                            }
                        }
                    } else {
                        if (rawMaterialGroup.isSelector) {
                            //adding default prices for all raw materials
                            angular.forEach(rawMaterialGroup.rawMaterials, function (rawMaterial) {
                                rawMaterial.sellingPrice = {
                                    'RON': 0,
                                    'EUR': 0
                                };
                            });
                        }
                    }
                    if (willAdd) {
                        //commons.log('Adding '+rawMaterialGroup.name);
                        validRawMaterialGroups.push(rawMaterialGroup);
                    }
                });
                $scope.rawMaterialsGroups = validRawMaterialGroups;
                $scope.rawMaterialsGroupsTreeCallbacks = {
                    beforeDrag: beforeDraggingRawMaterial,
                    dropped: rawMaterialGroupDropped
                };
                $scope.loading = false;
            });
        };
        //calculates a total per group of raw materials
        $scope.calculateFixedTotalPricePerGroup = function (rawMaterialGroup) {
            var sum = 0;
            angular.forEach(rawMaterialGroup.rawMaterials, function (rawMaterial) {
                sum += parseFloat(rawMaterial.pricePerProductionUnit * $scope.currencyMultiplier * ($scope.product.ratio + $scope.product.effectiveLostRatio));
            });
            return localize.getLocalizedString('_Total_') + " " + sum.toFixed($scope.decimals) + " " + $scope.pricesCurrency;
        };

        //production price for raw material of a selector
        $scope.getSelectorAllProductionPrice = function (rawMaterial, isDebug) {
            var result = parseFloat(rawMaterial.pricePerProductionUnit * $scope.currencyMultiplier * ($scope.product.ratio + $scope.product.effectiveLostRatio)).toFixed($scope.decimals) + " " + $scope.pricesCurrency;
            if (isDebug) {
                result += "=" + (rawMaterial.pricePerProductionUnit * $scope.currencyMultiplier).toFixed($scope.decimals) + $scope.pricesCurrency + " / mp x (" + $scope.product.ratio.toFixed($scope.decimals) + " mp + " + $scope.product.effectiveLostRatio.toFixed($scope.decimals) + "mp) " + $scope.pricesCurrency;
            }
            return result;
        };
        //production price and total fixed price for a raw material of a selector
        $scope.getSelectorAllProductionPrice = function (rawMaterial) {
            var result = parseFloat(rawMaterial.pricePerProductionUnit * $scope.currencyMultiplier * ($scope.product.ratio + $scope.product.effectiveLostRatio));
            result += $scope.totalFixedPrice;
            return result.toFixed($scope.decimals) + " " + $scope.pricesCurrency;
        };
        var priceStorage = {};
        $scope.onSelectorPriceIsAboutToChange = function (forRawMaterial) {
            if (!priceStorage.oldSellingPrice) {
                priceStorage.oldSellingPrice = angular.copy(forRawMaterial.sellingPrice);
            }
        };
        //listener for ng blur - so we can save the entered value
        $scope.onSelectorPriceChanged = function (forRawMaterial) {
            //however, never trust the enduser
            if (!forRawMaterial.sellingPrice || !forRawMaterial.sellingPrice[$scope.pricesCurrency]) {
                //commons.log('Error : Invalid number');
                delete priceStorage.oldSellingPrice;
                return;
            }
            if ($scope.pricesCurrency == "RON") {
                forRawMaterial.sellingPrice["EUR"] = forRawMaterial.sellingPrice["RON"] / $rootScope.serverSettings.applicationEuro;
            } else {
                forRawMaterial.sellingPrice["RON"] = forRawMaterial.sellingPrice["EUR"] * $rootScope.serverSettings.applicationEuro;
            }
            if ((forRawMaterial.sellingPrice[$scope.pricesCurrency] - priceStorage.oldSellingPrice[$scope.pricesCurrency]) != 0) {
                //place a server call to store selling price per production unit
                ProductsService.storeSellingPrice($scope.product.id, forRawMaterial.id, forRawMaterial.sellingPrice["EUR"]).then(function (response) {
                    //commons.log('Price now : ' + forRawMaterial.sellingPrice["EUR"]);
                    commons.displayLocalizedToast('_Success_', 'success');
                });
            }
            delete priceStorage.oldSellingPrice;
        };
        //calculates commercial addition %
        $scope.calculateAdditionForSellingPrice = function (rawMaterial) {
            rawMaterial.hasLowAddition = false;
            var result = 0;
            if (rawMaterial.sellingPrice && !isNaN(rawMaterial.sellingPrice[$scope.pricesCurrency])) {
                result = parseFloat(100 * rawMaterial.sellingPrice[$scope.pricesCurrency] / ($scope.totalFixedPrice + (rawMaterial.pricePerProductionUnit * $scope.currencyMultiplier * ($scope.product.ratio + $scope.product.effectiveLostRatio))));
                result = result - 100;
            }
            if (result < $rootScope.serverSettings.applicationMinAddedPercent) {
                rawMaterial.hasLowAddition = true;
            }
            return result == 0 ? "0 %" : result.toFixed(0) + " %";
        };

        $scope.calculateRatios = function () {
            $scope.product.ratio = parseFloat($scope.product.extraData.width * $scope.product.extraData.height / 1000000);
            $scope.product.effectiveLostRatio = parseFloat($scope.product.lostRatio * $scope.product.extraData.width * $scope.product.extraData.height / 100000000);
        };
        //removes something from the tree
        $scope.askQuestionThenRemove = function (event, scope) {
            var targetNode = {};
            if (!scope.$nodeScope) {
                //child of the root
                targetNode = scope.$modelValue;
            } else {
                //child of a child
                targetNode = scope.$nodeScope.$modelValue;
            }
            //check if raw group can be removed (is not mandatory)
            if (targetNode.isRequiredInEveryProduct) {
                commons.displayLocalizedToast('How the Hell did you managed to press that delete button?', 'error');
                return;
            }
            var confirm = $mdDialog.confirm()
                .parent(angular.element(document.body))
                .title(localize.getLocalizedString('_DeleteQuestion_', targetNode.name, ''))
                .ariaLabel('Delete')
                .ok(localize.getLocalizedString('_Yes_'))
                .cancel(localize.getLocalizedString('_No_')).
                targetEvent(event);
            $mdDialog.show(confirm).then(function () {
                //call service to remove then do below operations
                ProductsService.removeRawGroupFromProduct($scope.product.id, targetNode.id).then(function (response) {
                    postRawMaterialRemoval(targetNode, scope);
                });

            });
        };
        //opens / closes a node in product
        $scope.toggleOpenNode = function (scope) {
            scope.toggle();
        };

        var selfDestroy = $scope.$on('$destroy', function () {
            //removing the watcher
            pricesCurrencyWatcher();
            if ($rootScope.isDebug) {
                //remove cookies
                delete $cookies.productId;
            }
            selfDestroy();
        });
        $scope.init();
    }
}).call(this);