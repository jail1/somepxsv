(function () {
    "use strict";
    angular.module('photoX').controller('adminAddOrEditComposedProductCtrl', adminAddOrEditComposedProductCtrl);

    adminAddOrEditComposedProductCtrl.$inject = ['$scope', '$rootScope', '$cookies', '$state', '$stateParams', 'commons', 'localize', '$mdDialog', 'ProductsService'];

    function adminAddOrEditComposedProductCtrl($scope, $rootScope, $cookies, $state, $stateParams, commons, localize, $mdDialog, ProductsService) {

        var pricesCurrencyWatcher;
        var rawMaterialsSelectorCombinations = [];

        function combinations(choices, iteratingField, callback, prefix) {
            if (!choices.length) {
                return callback(prefix);
            }
            for (var c = 0; c < choices[0][iteratingField].length; c++) {
                combinations(choices.slice(1), iteratingField, callback, (prefix || []).concat(choices[0][iteratingField][c]));
            }
        }

        function storeCombination(result) {
            result.id = rawMaterialsSelectorCombinations.length;
            rawMaterialsSelectorCombinations.push(result);
        }

        function makeCombinations() {
            rawMaterialsSelectorCombinations = [];
            if ($scope.availableSelectors.length > 1) {
                combinations($scope.availableSelectors, 'rawMaterials', storeCombination);
            }
            $scope.selectorCombinations = rawMaterialsSelectorCombinations;
        }

        //post server action
        function postSimpleProductAdded(nodeData) {
            calculateFixedTotalPrice();
            commons.displayLocalizedToast('_Success_', 'success');
        }

        //handler - dropped onto our product tree
        function basicProductDropped(event) {
            try {
                var source = event.source.nodeScope;
                var nodeData = source.$modelValue;
                var parent = event.dest.nodesScope;
                //commons.log("raw material group was dropped");
                //commons.log(parent.$treeScope.$element[0].id);
                if (parent.$treeScope.$element[0].id == "product-tree-root") {
                    ProductsService.addChildToProduct($scope.product.id, nodeData.id).then(function (response) {
                        postSimpleProductAdded(nodeData);
                    });
                }
            } catch (ex) {
                commons.displayLocalizedToast(ex, 'error');
            }
        }

        //post server operation - after a simple product was deleted
        function postSimpleProductRemoval(targetNode, scope) {
            $scope.basicProducts.push(targetNode);
            scope.remove();
            calculateFixedTotalPrice();
            commons.displayLocalizedToast('_Success_', 'success');
        }

        function checkHasAllPricesDefined(source) {
            try {
                if (source.$modelValue.notDraggable) {
                    commons.displayLocalizedToast('_IncompletePrices2_', 'error');
                    return false;
                }
                source.collapse();
                return true;
            }
            catch (ex) {
                commons.displayToast(ex, 'error');
                return false;
            }
        }

        function getSimpleProducts() {
            ProductsService.getAllUnlistedProducts($scope.product.id).then(function (response) {
                $scope.basicProducts = response;
                $scope.basicProductsTreeCallbacks = {
                    beforeDrag: checkHasAllPricesDefined,
                    dropped: basicProductDropped
                };
                var errorToastDisplayed = false;
                angular.forEach($scope.basicProducts, function (basicProduct) {
                    angular.forEach(basicProduct.selectors, function (rawMaterialGroup) {
                        basicProduct.extraData = angular.fromJson(basicProduct.extraData);
                        basicProduct.ratio = parseFloat(basicProduct.extraData.width * basicProduct.extraData.height / 1000000);
                        basicProduct.effectiveLostRatio = parseFloat(basicProduct.lostRatio * basicProduct.extraData.width * basicProduct.extraData.height / 100000000);
                        if (rawMaterialGroup.isSelector) {
                            //TODO : avoid same selectors in the list ???
                            angular.forEach(rawMaterialGroup.rawMaterials, function (rawMaterial) {
                                var price = _.findWhere(basicProduct.prices, {materialId: rawMaterial.id});
                                if (price) {
                                    rawMaterial.sellingPrice = {
                                        'RON': parseFloat((basicProduct.minQuantity * price.sellingPrice * $rootScope.serverSettings.applicationEuro).toFixed($scope.decimals)),
                                        'EUR': parseFloat(basicProduct.minQuantity * price.sellingPrice.toPrecision($scope.decimals))
                                    };
                                } else {
                                    basicProduct.notDraggable = true;
                                    if (!errorToastDisplayed) {
                                        commons.displayLocalizedToast('_IncompletePrices_', 'error');
                                        errorToastDisplayed = true;
                                    }
                                    commons.log('Eroare : ' + basicProduct.name + ' nu are preț definit pe ' + rawMaterialGroup.name + ' la opțiunea ' + rawMaterial.name);
                                    rawMaterial.sellingPrice = {
                                        'RON': 0,
                                        'EUR': 0
                                    };
                                }

                            });
                        }
                    });
                });
            });
            calculateFixedTotalPrice();
            $scope.loading = false;
        }

        //calculates a grand total for all fixed costs and expose it into $scope
        function calculateFixedTotalPrice() {
            var sum = 0;
            var collectedSelectors = [];
            angular.forEach($scope.productData, function (basicProduct) {
                angular.forEach(basicProduct.selectors, function (rawMaterialGroup) {
                    if (!rawMaterialGroup.isSelector) {
                        angular.forEach(rawMaterialGroup.rawMaterials, function (rawMaterial) {
                            sum += parseFloat(basicProduct.minQuantity * rawMaterial.pricePerProductionUnit * $scope.currencyMultiplier * (basicProduct.ratio + basicProduct.effectiveLostRatio));
                        });
                    } else {
                        //also, we collect available selectors here
                        //TODO : avoid same selectors to be present in the collected selectors ???
                        angular.forEach(rawMaterialGroup.rawMaterials, function (rawMaterial) {
                            rawMaterial.productionPrice = {};
                            rawMaterial.productionPrice["EUR"] = parseFloat(basicProduct.minQuantity * rawMaterial.pricePerProductionUnit * (basicProduct.ratio + basicProduct.effectiveLostRatio));
                            rawMaterial.productionPrice["RON"] = parseFloat(basicProduct.minQuantity * rawMaterial.pricePerProductionUnit * $rootScope.serverSettings.applicationEuro * (basicProduct.ratio + basicProduct.effectiveLostRatio));
                        });
                        collectedSelectors.push(rawMaterialGroup);
                    }
                });
            });
            $scope.totalFixedPriceWithoutSelectorProductionPrice = sum;
            $scope.availableSelectors = collectedSelectors;
            makeCombinations();
        }

        var cachedCombinedProductionPrices = [];
        $scope.getProductionPriceForCombination = function (combination) {
            var sum = $scope.totalFixedPriceWithoutSelectorProductionPrice;
            angular.forEach(combination, function (rawMaterial) {
                sum += rawMaterial.productionPrice[$scope.pricesCurrency];
            });
            cachedCombinedProductionPrices[combination.id] = sum;
            return sum;
        };
        var cachedCombinedSellingPrices = [];
        $scope.makeSellingPriceForCombination = function (combination) {
            var sum = 0;
            angular.forEach(combination, function (rawMaterial) {
                sum += rawMaterial.sellingPrice[$scope.pricesCurrency];
            });
            cachedCombinedSellingPrices[combination.id] = sum;
            return sum;
        };
        $scope.calculateAdditionForSellingPriceOfCombination = function (combination) {
            combination.hasLowAddition = false;
            var result = parseFloat(100 * cachedCombinedSellingPrices[combination.id] / cachedCombinedProductionPrices[combination.id]);
            result = result - 100;
            if (result < $rootScope.serverSettings.applicationMinAddedPercent) {
                combination.hasLowAddition = true;
            }
            return result == 0 ? "0 %" : result.toFixed(0) + " %";
        };

        $scope.getExtraDataFromParent = function (forRawMaterial) {
            var parentProduct = findBasicProductParentOfRawMaterial(forRawMaterial);
            return parentProduct.extraData.height + " " + localize.getLocalizedString('_ProductHeight_') + " " + parentProduct.extraData.width + " " + localize.getLocalizedString('_ProductWidth_');
        };

        //init
        $scope.init = function () {
            $scope.decimals = $rootScope.serverSettings.applicationDefaultDecimals;
            $scope.loading = true;
            $scope.pricesCurrency = "EUR";
            $scope.currencyMultiplier = 1;
            $scope.selectedMaterials = [];
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
            $scope.productData = [];
            if ($rootScope.isDebug && $cookies.productId) {
                //commons.log('Forcing product id ' + $cookies.productId + " in debug mode");
                ProductsService.getProductById(parseInt($cookies.productId)).then(function (response) {
                    $scope.loadProductComplete(response);
                });
            } else {
                if ($stateParams.productId) {
                    //it's an edit operation
                    if ($rootScope.isDebug) {
                        $cookies.productId = parseInt($stateParams.productId);
                    }
                    ProductsService.getProductById(parseInt($stateParams.productId)).then(function (response) {
                        $scope.loadProductComplete(response);
                    });
                } else {
                    $scope.product = {
                        id: 0,
                        isDisplayedToEndUser: false,
                        isComplex: true,
                        ratio: parseFloat(0.0),
                        lostRatio: parseFloat(0.0),
                        effectiveLostRatio: parseFloat(0.0),
                        extraData: {
                            width: parseInt(0),
                            height: parseInt(0)
                        },
                        minQuantity: parseInt(1)
                    };
                    $scope.loading = false;
                }
            }
        };
        //goes back to products list
        $scope.goToProductList = function () {
            if ($rootScope.isDebug) {
                delete $cookies.productId;
            }
            $state.go('materials.products');
        };
        //saves only product fields (children and selectors saved elsewhere
        $scope.saveProduct = function () {
            var serverProduct = {
                id: $scope.product.id,
                isListable: $scope.product.isListable,
                isComplex: true,
                name: $scope.product.name,
                description: $scope.product.description,
                extraData: angular.toJson($scope.product.extraData),
                ratio: parseFloat($scope.product.extraData.width * $scope.product.extraData.height / 1000000),
                lostRatio: parseFloat($scope.product.lostRatio),
                minQuantity: parseInt($scope.product.minQuantity)
            };
            ProductsService.createOrUpdateProduct(serverProduct).then(function (response) {
                commons.displayLocalizedToast('_Success_', 'success');
                if ($scope.product.id == 0) {
                    //('now product has id')
                    $scope.product.id = response.id;
                    getSimpleProducts();
                }
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
            $scope.productData = $scope.product.children;
            $scope.productPrices = $scope.product.prices;
            angular.forEach($scope.productData, function (basicProduct) {
                basicProduct.extraData = angular.fromJson(basicProduct.extraData);
                basicProduct.ratio = parseFloat(basicProduct.extraData.width * basicProduct.extraData.height / 1000000);
                basicProduct.effectiveLostRatio = parseFloat(basicProduct.lostRatio * basicProduct.extraData.width * basicProduct.extraData.height / 100000000);
                //commons.log(basicProduct.name + " r: "+basicProduct.ratio);
                angular.forEach(basicProduct.selectors, function (rawMaterialsGrop) {
                    if (rawMaterialsGrop.isSelector) {
                        angular.forEach(rawMaterialsGrop.rawMaterials, function (rawMaterial) {
                            var price = _.findWhere($scope.productPrices, {materialId: rawMaterial.id});
                            if (price) {
                                rawMaterial.sellingPrice = {
                                    'RON': parseFloat((basicProduct.minQuantity * price.sellingPrice * $rootScope.serverSettings.applicationEuro).toFixed($scope.decimals)),
                                    'EUR': parseFloat(basicProduct.minQuantity * price.sellingPrice.toPrecision($scope.decimals))
                                };
                                //Price now : 6.0282546515 EUR / piece
                                //Price now : 2.6734616515 EUR / piece
                                //$log.commons.log(price.sellingPrice);
                            } else {
                                commons.log('Eroare : ' + basicProduct.name + ' nu are preț definit pe ' + rawMaterialsGrop.name + ' la opțiunea ' + rawMaterial.name);
                                rawMaterial.sellingPrice = {
                                    'RON': 0,
                                    'EUR': 0
                                };
                            }
                        });
                    }
                });
            });
            getSimpleProducts();
            $scope.$applyAsync(function () {
                var treeScope = angular.element(document.getElementById("product-tree-root")).scope();
                treeScope.collapseAll();
            });
        };
        //calculates a total per group of raw materials
        $scope.calculateFixedTotalPricePerGroup = function (basicProduct, rawMaterialGroup) {
            var sum = 0;
            angular.forEach(rawMaterialGroup.rawMaterials, function (rawMaterial) {
                sum += parseFloat(basicProduct.minQuantity * rawMaterial.pricePerProductionUnit * $scope.currencyMultiplier * (basicProduct.ratio + basicProduct.effectiveLostRatio));
            });
            return localize.getLocalizedString('_Total_') + " " + sum.toFixed($scope.decimals) + " " + $scope.pricesCurrency;
        };

        //production price for raw material of a selector
        $scope.getProductionPriceByRawMaterialAndBasicProduct = function (basicProduct, rawMaterial, isDebug) {
            var result = parseFloat(basicProduct.minQuantity * rawMaterial.pricePerProductionUnit * $scope.currencyMultiplier * (basicProduct.ratio + basicProduct.effectiveLostRatio)).toFixed($scope.decimals) + " " + $scope.pricesCurrency;
            if (isDebug) {
                result += "=" + (basicProduct.minQuantity == 1 ? "" : basicProduct.minQuantity + "buc x ") + (rawMaterial.pricePerProductionUnit * $scope.currencyMultiplier).toFixed($scope.decimals) + $scope.pricesCurrency + " / mp x (" + basicProduct.ratio.toFixed($scope.decimals) + " mp + " + basicProduct.effectiveLostRatio.toFixed($scope.decimals) + "mp) " + $scope.pricesCurrency;
            }
            return result;
        };
        //removes something from the tree
        $scope.askQuestionThenRemoveBasicProduct = function (event, scope) {
            var targetNode = {};
            if (!scope.$nodeScope) {
                //child of the root
                targetNode = scope.$modelValue;
            } else {
                //child of a child
                targetNode = scope.$nodeScope.$modelValue;
            }
            var confirm = $mdDialog.confirm()
                .parent(angular.element(document.body))
                .title(localize.getLocalizedString('_DeleteQuestion_', targetNode.name, ''))
                .ariaLabel('Delete')
                .ok(localize.getLocalizedString('_Yes_'))
                .cancel(localize.getLocalizedString('_No_')).
                targetEvent(event);
            $mdDialog.show(confirm).then(function () {
                //commons.log("remove product - placing server call");
                ProductsService.removeParentOfProduct(targetNode.id).then(function (response) {
                    postSimpleProductRemoval(targetNode, scope);
                });
            });
        };
        var quantityStorage = {};
        $scope.onMinQuantityIsAboutToChange = function (basicProduct) {
            if (!quantityStorage.oldQuantity) {
                quantityStorage.oldQuantity = angular.copy(basicProduct.minQuantity);
            }
        };
        $scope.onMinQuantityChanged = function (basicProduct) {
            //however, never trust the enduser
            if (!basicProduct.minQuantity) {
                //commons.log('Error : Invalid number');
                delete quantityStorage.oldQuantity;
                return;
            }
            if ((basicProduct.minQuantity - quantityStorage.oldQuantity) != 0) {
                //updates selling prices
                angular.forEach($scope.productData, function (basicProduct) {
                    angular.forEach(basicProduct.selectors, function (rawMaterialsGroup) {
                        if (rawMaterialsGroup.isSelector) {
                            angular.forEach(rawMaterialsGroup.rawMaterials, function (rawMaterial) {
                                var price = _.findWhere(basicProduct.prices, {materialId: rawMaterial.id});
                                if (price) {
                                    rawMaterial.sellingPrice = {
                                        'RON': parseFloat((basicProduct.minQuantity * price.sellingPrice * $rootScope.serverSettings.applicationEuro).toFixed($scope.decimals)),
                                        'EUR': parseFloat(basicProduct.minQuantity * price.sellingPrice.toPrecision($scope.decimals))
                                    };
                                } else {
                                    commons.log('Eroare : ' + basicProduct.name + ' nu are preț definit pe ' + rawMaterialsGroup.name + ' la opțiunea ' + rawMaterial.name);
                                    rawMaterial.sellingPrice = {
                                        'RON': 0,
                                        'EUR': 0
                                    };
                                }
                            });
                        }
                    });
                });
                //calculates fixed price
                calculateFixedTotalPrice();
                ProductsService.storeMinQuantity(basicProduct.id, basicProduct.minQuantity).then(function (response) {
                    commons.displayLocalizedToast('_Success_', 'success');
                });
                delete quantityStorage.oldQuantity;
            }
        };
        //price setter for selectors
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
                //make sellingPrice per piece - taking into account all production costs
                var declaredQuantity = findBasicProductParentOfRawMaterial(forRawMaterial).minQuantity;
                var savingPricePerPiece = forRawMaterial.sellingPrice["EUR"];
                if (declaredQuantity > 1) {
                    savingPricePerPiece = (forRawMaterial.sellingPrice["EUR"]) / declaredQuantity;
                }
                //place a server call to store selling price per production unit
                ProductsService.storeSellingPrice($scope.product.id, forRawMaterial.id, savingPricePerPiece).then(function (response) {
                    //commons.log('Price now : ' + savingPricePerPiece + " EUR / piece");
                    commons.displayLocalizedToast('_Success_', 'success', angular.element(document.getElementById("price-input-" + forRawMaterial.id)));
                });
            }
            delete priceStorage.oldSellingPrice;
        };

        function findBasicProductParentOfRawMaterial(rawMaterialWereLookingFor) {
            var parentProduct;
            var willContinue = true;
            angular.forEach($scope.productData, function (basicProduct) {
                if (willContinue) {
                    angular.forEach(basicProduct.selectors, function (rawMaterialsGroup) {
                        if (willContinue) {
                            angular.forEach(rawMaterialsGroup.rawMaterials, function (rawMaterial) {
                                if (willContinue) {
                                    if (parseInt(rawMaterial.id) == parseInt(rawMaterialWereLookingFor.id)) {
                                        parentProduct = basicProduct;
                                        willContinue = false;
                                    }
                                }
                            });
                        }
                    });
                }
            });
            return parentProduct;
        }

        //production price when we have only one selector
        $scope.getTotalFixedPrice = function () {
            var sum = 0;
            angular.forEach($scope.productData, function (basicProduct) {
                angular.forEach(basicProduct.selectors, function (rawMaterialsGroup) {
                    if (!rawMaterialsGroup.isSelector) {
                        angular.forEach(rawMaterialsGroup.rawMaterials, function (rawMaterial) {
                            sum += parseFloat(rawMaterial.pricePerProductionUnit * $scope.currencyMultiplier * (basicProduct.ratio + basicProduct.effectiveLostRatio));
                        });
                    }
                });
            });
            return sum;
        };
        //production price for raw material of a selector (when it has only one selector)
        $scope.getProductionPriceByRawMaterial = function (rawMaterial) {
            var basicProduct = findBasicProductParentOfRawMaterial(rawMaterial);
            if (!basicProduct) {
                $log.error('Basic product parent of ' + rawMaterial.id + " " + rawMaterial.name + " NOT FOUND");
                return 0;
            }
            return parseFloat(basicProduct.minQuantity * rawMaterial.pricePerProductionUnit * $scope.currencyMultiplier * (basicProduct.ratio + basicProduct.effectiveLostRatio));
        };
        //production price for raw material of a selector (when it has only one selector)
        $scope.getSelectorAllProductionPrice = function (rawMaterial) {
            var result = $scope.getProductionPriceByRawMaterial(rawMaterial);
            result += $scope.getTotalFixedPrice();
            return result;
        };
        $scope.calculateAdditionForSellingPrice = function (rawMaterial) {
            rawMaterial.hasLowAddition = false;
            var result = parseFloat(100 * rawMaterial.sellingPrice[$scope.pricesCurrency] / $scope.getSelectorAllProductionPrice(rawMaterial));
            result = result - 100;
            if (result < $rootScope.serverSettings.applicationMinAddedPercent) {
                rawMaterial.hasLowAddition = true;
            }
            return result == 0 ? "0 %" : result.toFixed(0) + " %";
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
}).
    call(this);