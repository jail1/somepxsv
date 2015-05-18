/**
 * Created by Badu on 2/15/2015.
 */
(function () {
    "use strict";

    angular.module('photoX').service('ApplicationServices', applicationServices);

    applicationServices.$inject = ['$http', '$q', '$rootScope', '$log', '$timeout'];

    function applicationServices($http, $q, $rootScope, $log, $timeout) {

        function getClientsList() {
            var d = $q.defer();
            $http.get('/private/API/v1/getClientsList').success(function (response) {
                if (_.has(response, 'errorMessage')) {
                    $log.error("Error : " + response.errorMessage);
                    d.resolve(response);
                } else {
                    $rootScope.clients = response;
                    $log.info($rootScope.clients.length + " clients read");
                    d.resolve();
                }
            }).error(function (rejection) {
                $log.error("SERVER ERROR");
                $log.error(rejection);
                d.resolve(rejection);
            });
            return d.promise;
        }

        function addOrUpdateClient(client) {
            var d = $q.defer();
            var isUpdate = _.has(client, 'id');
            $http.post('/private/API/v1/addOrUpdateClient', client).success(function (response) {
                if (_.has(response, 'errorMessage')) {
                    $log.error("Error : " + response.errorMessage);
                    d.resolve(response);
                } else {
                    if (isUpdate) {
                        var updatedClient = _.findWhere($rootScope.clients, {id: client.id});
                        if (!updatedClient) {
                            $log.error("FATAL ERROR : client #" + client.id + " not found");
                        } else {
                            updatedClient = response;
                            $log.info("Client " + client.id + " updated");
                        }
                    } else {
                        $log.info("Client " + response.fullName + " created");
                        $rootScope.clients.push(response);
                    }
                    d.resolve();
                }
            }).error(function (rejection) {
                $log.error("SERVER ERROR");
                $log.error(rejection);
                d.resolve(rejection);
            });
            return d.promise;
        }

        function deleteClient(client) {
            var d = $q.defer();
            $http.get('/private/API/v1/deleteClient/' + client.id).success(function (response) {
                if (_.has(response, 'errorMessage')) {
                    $log.error("Error : " + response.errorMessage);
                    d.resolve(response);
                } else {
                    $rootScope.clients = _.reject($rootScope.clients, function (item) {
                        return item.id == client.id;
                    });
                    d.resolve();
                }
            }).error(function (rejection) {
                $log.error("SERVER ERROR");
                $log.error(rejection);
                d.resolve(rejection);
            });
            return d.promise;
        }

        function getPersons() {
            var d = $q.defer();
            $http.get('/private/API/v1/getPersons').success(function (response) {
                if (_.has(response, 'errorMessage')) {
                    $log.error("Error : " + response.errorMessage);
                    d.resolve(response);
                } else {
                    $rootScope.persons = response;
                    $log.info($rootScope.persons.length + " persons read");
                    d.resolve();
                }
            }).error(function (rejection) {
                $log.error("SERVER ERROR");
                $log.error(rejection);
                d.resolve(rejection);
            });
            return d.promise;
        }

        function addOrUpdatePerson(person) {
            var d = $q.defer();
            var isUpdate = _.has(person, 'id');
            $http.post('/private/API/v1/addOrUpdatePerson', person).success(function (response) {
                if (_.has(response, 'errorMessage')) {
                    $log.error("Error : " + response.errorMessage);
                    d.resolve(response);
                } else {
                    if (isUpdate) {
                        var updatedPerson = _.findWhere($rootScope.persons, {id: person.id});
                        if (!updatedPerson) {
                            $log.error("FATAL ERROR : person #" + person.id + " not found");
                        } else {
                            updatedPerson = response;
                            $log.info("Person " + person.id + " updated");
                        }
                    } else {
                        $log.info("Person " + response.firstName + " " + response.lastName + " created");
                        $rootScope.persons.push(response);
                    }
                    d.resolve();
                }
            }).error(function (rejection) {
                $log.error("SERVER ERROR");
                $log.error(rejection);
                d.resolve(rejection);
            });
            return d.promise;
        }

        function deletePerson(person) {
            var d = $q.defer();
            $http.get('/private/API/v1/deletePerson/' + person.id).success(function (response) {
                if (_.has(response, 'errorMessage')) {
                    $log.error("Error : " + response.errorMessage);
                    d.resolve(response);
                } else {
                    $rootScope.persons = _.reject($rootScope.persons, function (item) {
                        return item.id == person.id;
                    });
                    d.resolve();
                }
            }).error(function (rejection) {
                $log.error("SERVER ERROR");
                $log.error(rejection);
                d.resolve(rejection);
            });
            return d.promise;
        }

        //TODO : calling this will get both persons and products
        function getPersonsProducts() {
            var d = $q.defer();
            $http.get('/private/API/v1/getPersonsProducts').success(function (response) {
                if (_.has(response, 'errorMessage')) {
                    $log.error("Error : " + response.errorMessage);
                    d.resolve(response);
                } else {
                    $rootScope.personsProducts = response;
                    $log.info($rootScope.personsProducts.length + " personsProducts read");
                    d.resolve();
                }
            }).error(function (rejection) {
                $log.error("SERVER ERROR");
                $log.error(rejection);
                d.resolve(rejection);
            });
            return d.promise;
        }

        function addOrUpdatePersonProduct(person) {
            var d = $q.defer();
            var isUpdate = _.has(person, 'id');
            $http.post('/private/API/v1/addOrUpdatePersonProduct', person).success(function (response) {
                if (_.has(response, 'errorMessage')) {
                    $log.error("Error : " + response.errorMessage);
                    d.resolve(response);
                } else {
                    if (isUpdate) {
                        var updatedPersonProduct = _.findWhere($rootScope.personProductsProducts, {id: personProduct.id});
                        if (!updatedPersonProduct) {
                            $log.error("FATAL ERROR : personProduct #" + personProduct.id + " not found");
                        } else {
                            updatedPersonProduct = response;
                            $log.info("PersonProduct " + personProduct.id + " updated");
                        }
                    } else {
                        $log.info("PersonProduct " + response.firstName + " " + response.lastName + " created");
                        $rootScope.personsProducts.push(response);
                    }
                    d.resolve();
                }
            }).error(function (rejection) {
                $log.error("SERVER ERROR");
                $log.error(rejection);
                d.resolve(rejection);
            });
            return d.promise;
        }

        function deletePersonProduct(personProduct) {
            var d = $q.defer();
            $http.get('/private/API/v1/deletePersonProduct/' + personProduct.id).success(function (response) {
                if (_.has(response, 'errorMessage')) {
                    $log.error("Error : " + response.errorMessage);
                    d.resolve(response);
                } else {
                    $rootScope.personsProducts = _.reject($rootScope.personsProducts, function (item) {
                        return item.id == personProduct.id;
                    });
                    d.resolve();
                }
            }).error(function (rejection) {
                $log.error("SERVER ERROR");
                $log.error(rejection);
                d.resolve(rejection);
            });
            return d.promise;
        }

        function getPriceTags() {
            var d = $q.defer();
            $http.get('/private/API/v1/getPriceTags').success(function (response) {
                if (_.has(response, 'errorMessage')) {
                    $log.error("Error : " + response.errorMessage);
                    d.resolve(response);
                } else {
                    $rootScope.priceTags = response;
                    $log.info($rootScope.priceTags.length + " price tags read");
                    d.resolve();
                }
            }).error(function (rejection) {
                $log.error("SERVER ERROR");
                $log.error(rejection);
                d.resolve(rejection);
            });
            return d.promise;
        }

        function addOrUpdatePriceTag(priceTag) {
            var d = $q.defer();
            var isUpdate = _.has(priceTag, 'id');
            $http.post('/private/API/v1/addOrUpdatePriceTag', priceTag).success(function (response) {
                if (_.has(response, 'errorMessage')) {
                    $log.error("Error : " + response.errorMessage);
                    d.resolve(response);
                } else {
                    if (isUpdate) {
                        var updatedPriceTag = _.findWhere($rootScope.priceTags, {id: priceTag.id});
                        if (!updatedPriceTag) {
                            $log.error("FATAL ERROR : priceTag #" + priceTag.id + " not found");
                        } else {
                            updatedPriceTag = response;
                            $log.info("Pricetag " + priceTag.id + " updated");
                        }
                    } else {
                        $log.info("PriceTag " + response.name + " created");
                        $rootScope.priceTags.push(response);
                    }
                    d.resolve();
                }
            }).error(function (rejection) {
                $log.error("SERVER ERROR");
                $log.error(rejection);
                d.resolve(rejection);
            });
            return d.promise;
        }

        function deletePriceTag(priceTag) {
            var d = $q.defer();
            $http.get('/private/API/v1/deletePriceTag/' + priceTag.id).success(function (response) {
                if (_.has(response, 'errorMessage')) {
                    $log.error("Error : " + response.errorMessage);
                    d.resolve(response);
                } else {
                    $rootScope.priceTags = _.reject($rootScope.priceTags, function (item) {
                        return item.id == priceTag.id;
                    });
                    d.resolve();
                }
            }).error(function (rejection) {
                $log.error("SERVER ERROR");
                $log.error(rejection);
                d.resolve(rejection);
            });
            return d.promise;
        }

        //TODO : calling this will get both products and prices
        function getProductsPrices() {
            var d = $q.defer();
            $http.get('/private/API/v1/getProductsPrices').success(function (response) {
                if (_.has(response, 'errorMessage')) {
                    $log.error("Error : " + response.errorMessage);
                    d.resolve(response);
                } else {
                    $rootScope.productsPrices = response;
                    $log.info($rootScope.productsPrices.length + " product prices read");
                    d.resolve();
                }
            }).error(function (rejection) {
                $log.error("SERVER ERROR");
                $log.error(rejection);
                d.resolve(rejection);
            });
            return d.promise;
        }

        function addOrUpdateProductPrice(productPrice) {
            var d = $q.defer();
            var isUpdate = _.has(productPrice, 'id');
            $http.post('/private/API/v1/addOrUpdateProductPrice', productPrice).success(function (response) {
                if (_.has(response, 'errorMessage')) {
                    $log.error("Error : " + response.errorMessage);
                    d.resolve(response);
                } else {
                    if (isUpdate) {
                        var updatedProductPrice = _.findWhere($rootScope.productsPrices, {id: productPrice.id});
                        if (!updatedProductPrice) {
                            $log.error("FATAL ERROR : productPrice #" + productPrice.id + " not found");
                        } else {
                            updatedProductPrice = response;
                            $log.info("productPrice " + productPrice.id + " updated");
                        }
                    } else {
                        $log.info("productPrice " + response.price + " RON " + response.duration + " minutes created");
                        $rootScope.productsPrices.push(response);
                    }
                    d.resolve();
                }
            }).error(function (rejection) {
                $log.error("SERVER ERROR");
                $log.error(rejection);
                d.resolve(rejection);
            });
            return d.promise;
        }

        function deleteProductPrice(productPrice) {
            var d = $q.defer();
            $http.get('/private/API/v1/deleteProductPrice/' + productPrice.id).success(function (response) {
                if (_.has(response, 'errorMessage')) {
                    $log.error("Error : " + response.errorMessage);
                    d.resolve(response);
                } else {
                    $rootScope.productsPrices = _.reject($rootScope.productsPrices, function (item) {
                        return item.id == productPrice.id;
                    });
                    d.resolve();
                }
            }).error(function (rejection) {
                $log.error("SERVER ERROR");
                $log.error(rejection);
                d.resolve(rejection);
            });
            return d.promise;
        }

        function getProducts() {
            var d = $q.defer();
            $http.get('/private/API/v1/getProducts').success(function (response) {
                if (_.has(response, 'errorMessage')) {
                    $log.error("Error : " + response.errorMessage);
                    d.resolve(response);
                } else {
                    $rootScope.products = response;
                    //$log.info($rootScope.products.length + " products read");
                    d.resolve(response);
                }
            }).error(function (rejection) {
                $log.error("SERVER ERROR");
                $log.error(rejection);
                d.resolve(rejection);
            });
            return d.promise;
        }

        function addOrUpdateProduct(product) {
            var d = $q.defer();
            var isUpdate = _.has(product, 'id');
            $http.post('/private/API/v1/addOrUpdateProduct', product).success(function (response) {
                if (_.has(response, 'errorMessage')) {
                    $log.error("Error : " + response.errorMessage);
                    d.resolve(response);
                } else {
                    if (isUpdate) {
                        var updatedProduct = _.findWhere($rootScope.products, {id: product.id});
                        if (!updatedProduct) {
                            $log.error("FATAL ERROR : product #" + product.id + " not found");
                        } else {
                            updatedProduct = response;
                            $log.info("Product " + product.id + " updated");
                        }
                    } else {
                        $log.info("Product " + response.name + " created");
                        $rootScope.products.push(response);
                    }
                    d.resolve();
                }
            }).error(function (rejection) {
                $log.error("SERVER ERROR");
                $log.error(rejection);
                d.resolve(rejection);
            });
            return d.promise;
        }

        function deleteProduct(product) {
            var d = $q.defer();
            $http.get('/private/API/v1/deleteProduct/' + product.id).success(function (response) {
                if (_.has(response, 'errorMessage')) {
                    $log.error("Error : " + response.errorMessage);
                    d.resolve(response);
                } else {
                    $rootScope.products = _.reject($rootScope.products, function (item) {
                        return item.id == product.id;
                    });
                    d.resolve();
                }
            }).error(function (rejection) {
                $log.error("SERVER ERROR");
                $log.error(rejection);
                d.resolve(rejection);
            });
            return d.promise;
        }

        function getCurrentReservations() {
            var d = $q.defer();
            $http.get('/private/API/v1/getCurrentReservations').success(function (response) {
                if (_.has(response, 'errorMessage')) {
                    $log.error("Error : " + response.errorMessage);
                    d.resolve(response);
                } else {
                    $rootScope.reservations = response;
                    $log.info($rootScope.reservations.length + " reservations read");
                    d.resolve();
                }
            }).error(function (rejection) {
                $log.error("SERVER ERROR");
                $log.error(rejection);
                d.resolve(rejection);
            });
            return d.promise;
        }

        function addOrUpdateReservation(reservation) {
            var d = $q.defer();
            var isUpdate = _.has(reservation, 'id');
            $http.post('/private/API/v1/addOrUpdateReservation', reservation).success(function (response) {
                if (_.has(response, 'errorMessage')) {
                    $log.error("Error : " + response.errorMessage);
                    d.resolve(response);
                } else {
                    if (isUpdate) {
                        var updatedReservation = _.findWhere($rootScope.reservations, {id: reservation.id});
                        if (!updatedReservation) {
                            $log.error("FATAL ERROR : reservation #" + reservation.id + " not found");
                        } else {
                            updatedReservation = response;
                            $log.info("Reservation " + reservation.id + " updated");
                        }
                    } else {
                        $log.info("Reservation " + response.name + " created");
                        $rootScope.reservations.push(response);
                    }
                    d.resolve();
                }
            }).error(function (rejection) {
                $log.error("SERVER ERROR");
                $log.error(rejection);
                d.resolve(rejection);
            });
            return d.promise;
        }

        //TODO : parameter array of reservations
        //NOT recommended to be used in update mode - use simple method above
        function addOrUpdateReservations(reservations) {
            var d = $q.defer();
            var isUpdate = false;
            angular.forEach(reservations, function (reservation) {
                if (reservation.id) {
                    if (!isUpdate) {
                        isUpdate = true;
                    }
                }
            });
            $http.post('/private/API/v1/addOrUpdateReservations', reservations).success(function (response) {
                if (_.has(response, 'errorMessage')) {
                    $log.error("Error : " + response.errorMessage);
                    d.resolve(response);
                } else {
                    if (isUpdate) {
                        angular.forEach(reservations, function (reservation) {
                            var updatedReservation = _.findWhere($rootScope.reservations, {id: reservation.id});
                            if (!updatedReservation) {
                                $log.error("FATAL ERROR : reservation #" + reservation.id + " not found");
                            } else {
                                updatedReservation = _.findWhere(response, {id: reservation.id});
                                $log.info("Reservation " + reservation.id + " updated");
                            }
                        });
                    } else {
                        $log.info("-- " + response.length + " reservations created");
                        $rootScope.reservations.push(response);
                    }
                    d.resolve();
                }
            }).error(function (rejection) {
                $log.error("SERVER ERROR");
                $log.error(rejection);
                d.resolve(rejection);
            });
            return d.promise;
        }

        function deleteReservation(reservation) {
            var d = $q.defer();
            $http.get('/private/API/v1/deleteReservation/' + reservation.id).success(function (response) {
                if (_.has(response, 'errorMessage')) {
                    $log.error("Error : " + response.errorMessage);
                    d.resolve(response);
                } else {
                    $rootScope.reservations = _.reject($rootScope.reservations, function (item) {
                        return item.id == reservation.id;
                    });
                    d.resolve();
                }
            }).error(function (rejection) {
                $log.error("SERVER ERROR");
                $log.error(rejection);
                d.resolve(rejection);
            });
            return d.promise;
        }

        function provideTestData() {
            var d = $q.defer();
            $timeout(function () {
                var currentYear = moment().year();
                var currentMonth = moment().month();
                var currentDay = moment().date();

                var testEvents = [
                    {
                        id: 1,
                        title: 'Popescu Costel',
                        product: 'Balet pe gheata',
                        type: 'warning',
                        startsAt: new Date(currentYear, currentMonth, currentDay - 13, 10, 0),
                        endsAt: new Date(currentYear, currentMonth, currentDay - 13, 11, 0),
                        editable: true,
                        deletable: true,
                        incrementsBadgeTotal: true
                    },
                    {
                        id: 2,
                        title: 'Bogdan Dinu',
                        product: 'Terapie Yumeiho',
                        type: 'info',
                        startsAt: new Date(currentYear, currentMonth, currentDay - 13, 17, 0),
                        endsAt: new Date(currentYear, currentMonth, currentDay - 13, 18, 0),
                        editable: true,
                        deletable: true,
                        incrementsBadgeTotal: true
                    },
                    {
                        id: 3,
                        title: 'Adina Dinu',
                        product: 'Terapie Yumeiho',
                        type: 'important',
                        startsAt: new Date(currentYear, currentMonth, currentDay - 13, 10, 0),
                        endsAt: new Date(currentYear, currentMonth, currentDay - 13, 11, 0),
                        editable: true,
                        deletable: true,
                        incrementsBadgeTotal: true
                    },
                    {
                        id: 4,
                        title: 'Dudu Ionel',
                        product: 'Alergare usoara',
                        type: 'important',
                        startsAt: new Date(currentYear, currentMonth, currentDay - 13, 10, 0),
                        endsAt: new Date(currentYear, currentMonth, currentDay - 13, 11, 0),
                        editable: true,
                        deletable: true,
                        incrementsBadgeTotal: true
                    },
                    {
                        id: 5,
                        title: 'Popescu Costel',
                        product: 'Balet pe gheata',
                        type: 'warning',
                        startsAt: new Date(currentYear, currentMonth, currentDay - 13, 11, 0),
                        endsAt: new Date(currentYear, currentMonth, currentDay - 13, 12, 0),
                        editable: true,
                        deletable: true,
                        incrementsBadgeTotal: true
                    },
                    {
                        id: 6,
                        title: 'Bogdan Dinu',
                        product: 'Terapie Yumeiho',
                        type: 'info',
                        startsAt: new Date(currentYear, currentMonth, currentDay - 13, 15, 0),
                        endsAt: new Date(currentYear, currentMonth, currentDay - 13, 16, 0),
                        editable: true,
                        deletable: true,
                        incrementsBadgeTotal: true
                    },
                    {
                        id: 7,
                        title: 'Adina Dinu',
                        product: 'Terapie Yumeiho',
                        type: 'important',
                        startsAt: new Date(currentYear, currentMonth, currentDay - 13, 11, 0),
                        endsAt: new Date(currentYear, currentMonth, currentDay - 13, 12, 0),
                        editable: true,
                        deletable: true,
                        incrementsBadgeTotal: true
                    },
                    {
                        id: 8,
                        title: 'Dudu Ionel',
                        product: 'Alergare usoara',
                        type: 'important',
                        startsAt: new Date(currentYear, currentMonth, currentDay - 13, 15, 0),
                        endsAt: new Date(currentYear, currentMonth, currentDay - 13, 16, 0),
                        editable: true,
                        deletable: true,
                        incrementsBadgeTotal: true
                    },
                    {
                        id: 9,
                        title: 'Popescu Costel',
                        product: 'Balet pe gheata',
                        type: 'warning',
                        startsAt: new Date(currentYear, currentMonth, currentDay - 13, 10, 0),
                        endsAt: new Date(currentYear, currentMonth, currentDay - 13, 11, 0),
                        editable: true,
                        deletable: true,
                        incrementsBadgeTotal: true
                    },
                    {
                        id: 10,
                        title: 'Bogdan Dinu',
                        product: 'Terapie Yumeiho',
                        type: 'info',
                        startsAt: new Date(currentYear, currentMonth, currentDay - 13, 17, 0),
                        endsAt: new Date(currentYear, currentMonth, currentDay - 13, 18, 0),
                        editable: true,
                        deletable: true,
                        incrementsBadgeTotal: true
                    },
                    {
                        id: 11,
                        title: 'Adina Dinu',
                        product: 'Terapie Yumeiho',
                        type: 'important',
                        startsAt: new Date(currentYear, currentMonth, currentDay - 13, 10, 0),
                        endsAt: new Date(currentYear, currentMonth, currentDay - 13, 11, 0),
                        editable: true,
                        deletable: true,
                        incrementsBadgeTotal: true
                    },
                    {
                        id: 12,
                        title: 'Dudu Ionel',
                        product: 'Alergare usoara',
                        type: 'important',
                        startsAt: new Date(currentYear, currentMonth, currentDay - 13, 10, 0),
                        endsAt: new Date(currentYear, currentMonth, currentDay - 13, 11, 0),
                        editable: true,
                        deletable: true,
                        incrementsBadgeTotal: true
                    },
                    {
                        id: 13,
                        title: 'Popescu Costel',
                        product: 'Balet pe gheata',
                        type: 'warning',
                        startsAt: new Date(currentYear, currentMonth, currentDay - 13, 11, 0),
                        endsAt: new Date(currentYear, currentMonth, currentDay - 13, 12, 0),
                        editable: true,
                        deletable: true,
                        incrementsBadgeTotal: true
                    },
                    {
                        id: 14,
                        title: 'Bogdan Dinu',
                        product: 'Terapie Yumeiho',
                        type: 'info',
                        startsAt: new Date(currentYear, currentMonth, currentDay - 13, 15, 0),
                        endsAt: new Date(currentYear, currentMonth, currentDay - 13, 16, 0),
                        editable: true,
                        deletable: true,
                        incrementsBadgeTotal: true
                    },
                    {
                        id: 15,
                        title: 'Adina Dinu',
                        product: 'Terapie Yumeiho',
                        type: 'important',
                        startsAt: new Date(currentYear, currentMonth, currentDay - 13, 11, 0),
                        endsAt: new Date(currentYear, currentMonth, currentDay - 13, 12, 0),
                        editable: true,
                        deletable: true,
                        incrementsBadgeTotal: true
                    },
                    {
                        id: 16,
                        title: 'Dudu Ionel',
                        product: 'Alergare usoara',
                        type: 'important',
                        startsAt: new Date(currentYear, currentMonth, currentDay - 13, 15, 0),
                        endsAt: new Date(currentYear, currentMonth, currentDay - 13, 16, 0),
                        editable: true,
                        deletable: true,
                        incrementsBadgeTotal: true
                    },
                    //=======================
                    {
                        id: 17,
                        title: 'Popescu Costel',
                        product: 'Balet pe gheata',
                        type: 'warning',
                        startsAt: new Date(currentYear, currentMonth, currentDay - 5, 10, 0),
                        endsAt: new Date(currentYear, currentMonth, currentDay - 5, 11, 0),
                        editable: true,
                        deletable: true,
                        incrementsBadgeTotal: true
                    },
                    {
                        id: 18,
                        title: 'Bogdan Dinu',
                        product: 'Terapie Yumeiho',
                        type: 'info',
                        startsAt: new Date(currentYear, currentMonth, currentDay - 5, 17, 0),
                        endsAt: new Date(currentYear, currentMonth, currentDay - 5, 18, 0),
                        editable: true,
                        deletable: true,
                        incrementsBadgeTotal: true
                    },
                    {
                        id: 19,
                        title: 'Adina Dinu',
                        product: 'Terapie Yumeiho',
                        type: 'important',
                        startsAt: new Date(currentYear, currentMonth, currentDay - 5, 10, 0),
                        endsAt: new Date(currentYear, currentMonth, currentDay - 5, 11, 0),
                        editable: true,
                        deletable: true,
                        incrementsBadgeTotal: true
                    },
                    {
                        id: 20,
                        title: 'Dudu Ionel',
                        product: 'Alergare usoara',
                        type: 'important',
                        startsAt: new Date(currentYear, currentMonth, currentDay - 5, 10, 0),
                        endsAt: new Date(currentYear, currentMonth, currentDay - 5, 11, 0),
                        editable: true,
                        deletable: true,
                        incrementsBadgeTotal: true
                    },
                    //=======================
                    {
                        id: 21,
                        title: 'Popescu Costel',
                        product: 'Balet pe gheata',
                        type: 'warning',
                        startsAt: new Date(currentYear, currentMonth, currentDay, 10, 0),
                        endsAt: new Date(currentYear, currentMonth, currentDay, 11, 0),
                        editable: true,
                        deletable: true,
                        incrementsBadgeTotal: true
                    },
                    {
                        id: 22,
                        title: 'Bogdan Dinu',
                        product: 'Terapie Yumeiho',
                        type: 'info',
                        startsAt: new Date(currentYear, currentMonth, currentDay, 17, 0),
                        endsAt: new Date(currentYear, currentMonth, currentDay, 18, 0),
                        editable: true,
                        deletable: true,
                        incrementsBadgeTotal: true
                    },
                    {
                        id: 23,
                        title: 'Adina Dinu',
                        product: 'Terapie Yumeiho',
                        type: 'important',
                        startsAt: new Date(currentYear, currentMonth, currentDay, 10, 0),
                        endsAt: new Date(currentYear, currentMonth, currentDay, 12, 0),
                        editable: true,
                        deletable: true,
                        incrementsBadgeTotal: true
                    },
                    {
                        id: 24,
                        title: 'Dudu Ionel',
                        product: 'Alergare usoara',
                        type: 'important',
                        startsAt: new Date(currentYear, currentMonth, currentDay, 10, 0),
                        endsAt: new Date(currentYear, currentMonth, currentDay, 12, 0),
                        editable: true,
                        deletable: true,
                        incrementsBadgeTotal: true
                    },
                    {
                        id: 25,
                        title: 'Popescu Costel',
                        product: 'Balet pe gheata',
                        type: 'warning',
                        startsAt: new Date(currentYear, currentMonth, currentDay, 11, 0),
                        endsAt: new Date(currentYear, currentMonth, currentDay, 13, 0),
                        editable: true,
                        deletable: true,
                        incrementsBadgeTotal: true
                    },
                    {
                        id: 26,
                        title: 'Bogdan Dinu',
                        product: 'Terapie Yumeiho',
                        type: 'info',
                        startsAt: new Date(currentYear, currentMonth, currentDay, 15, 0),
                        endsAt: new Date(currentYear, currentMonth, currentDay, 16, 0),
                        editable: true,
                        deletable: true,
                        incrementsBadgeTotal: true
                    },
                    {
                        id: 27,
                        title: 'Adina Dinu',
                        product: 'Terapie Yumeiho',
                        type: 'important',
                        startsAt: new Date(currentYear, currentMonth, currentDay, 11, 0),
                        endsAt: new Date(currentYear, currentMonth, currentDay, 12, 0),
                        editable: true,
                        deletable: true,
                        incrementsBadgeTotal: true
                    },
                    {
                        id: 28,
                        title: 'Dudu Ionel',
                        product: 'Alergare usoara',
                        type: 'important',
                        startsAt: new Date(currentYear, currentMonth, currentDay, 15, 0),
                        endsAt: new Date(currentYear, currentMonth, currentDay, 16, 0),
                        editable: true,
                        deletable: true,
                        incrementsBadgeTotal: true
                    },
                    //=======================
                    {
                        id: 29,
                        title: 'Popescu Costel',
                        product: 'Balet pe gheata',
                        type: 'warning',
                        startsAt: new Date(currentYear, currentMonth, currentDay - 1, 10, 0),
                        endsAt: new Date(currentYear, currentMonth, currentDay - 1, 11, 0),
                        editable: true,
                        deletable: true,
                        incrementsBadgeTotal: true
                    },
                    {
                        id: 30,
                        title: 'Bogdan Dinu',
                        product: 'Terapie Yumeiho',
                        type: 'info',
                        startsAt: new Date(currentYear, currentMonth, currentDay - 1, 17, 0),
                        endsAt: new Date(currentYear, currentMonth, currentDay - 1, 18, 0),
                        editable: true,
                        deletable: true,
                        incrementsBadgeTotal: true
                    },
                    {
                        id: 31,
                        title: 'Adina Dinu',
                        product: 'Terapie Yumeiho',
                        type: 'important',
                        startsAt: new Date(currentYear, currentMonth, currentDay - 1, 10, 0),
                        endsAt: new Date(currentYear, currentMonth, currentDay - 1, 11, 0),
                        editable: true,
                        deletable: true,
                        incrementsBadgeTotal: true
                    },
                    {
                        id: 32,
                        title: 'Dudu Ionel',
                        product: 'Alergare usoara',
                        type: 'important',
                        startsAt: new Date(currentYear, currentMonth, currentDay - 1, 10, 0),
                        endsAt: new Date(currentYear, currentMonth, currentDay - 1, 11, 0),
                        editable: true,
                        deletable: true,
                        incrementsBadgeTotal: true
                    },
                    {
                        id: 33,
                        title: 'Popescu Costel',
                        product: 'Balet pe gheata',
                        type: 'warning',
                        startsAt: new Date(currentYear, currentMonth, currentDay - 1, 11, 0),
                        endsAt: new Date(currentYear, currentMonth, currentDay - 1, 12, 0),
                        editable: true,
                        deletable: true,
                        incrementsBadgeTotal: true
                    },
                    {
                        id: 34,
                        title: 'Bogdan Dinu',
                        product: 'Terapie Yumeiho',
                        type: 'info',
                        startsAt: new Date(currentYear, currentMonth, currentDay - 1, 15, 0),
                        endsAt: new Date(currentYear, currentMonth, currentDay - 1, 16, 0),
                        editable: true,
                        deletable: true,
                        incrementsBadgeTotal: true
                    },
                    {
                        id: 35,
                        title: 'Adina Dinu',
                        product: 'Terapie Yumeiho',
                        type: 'important',
                        startsAt: new Date(currentYear, currentMonth, currentDay - 1, 11, 0),
                        endsAt: new Date(currentYear, currentMonth, currentDay - 1, 12, 0),
                        editable: true,
                        deletable: true,
                        incrementsBadgeTotal: true
                    },
                    {
                        id: 36,
                        title: 'Dudu Ionel',
                        product: 'Alergare usoara',
                        type: 'important',
                        startsAt: new Date(currentYear, currentMonth, currentDay - 1, 15, 0),
                        endsAt: new Date(currentYear, currentMonth, currentDay - 1, 16, 0),
                        editable: true,
                        deletable: true,
                        incrementsBadgeTotal: true
                    }
                ];
                d.resolve(testEvents);
            }, 1000);
            return d.promise;
        }

        var result = {
            getClientsList: getClientsList,
            addOrUpdateClient: addOrUpdateClient,
            deleteClient: deleteClient,
            getPersons: getPersons,
            addOrUpdatePerson: addOrUpdatePerson,
            deletePerson: deletePerson,
            getPersonsProducts: getPersonsProducts,
            addOrUpdatePersonProduct: addOrUpdatePersonProduct,
            deletePersonProduct: deletePersonProduct,
            getPriceTags: getPriceTags,
            addOrUpdatePriceTag: addOrUpdatePriceTag,
            deletePriceTag: deletePriceTag,
            getProductsPrices: getProductsPrices,
            addOrUpdateProductPrice: addOrUpdateProductPrice,
            deleteProductPrice: deleteProductPrice,
            getProducts: getProducts,
            addOrUpdateProduct: addOrUpdateProduct,
            deleteProduct: deleteProduct,
            getCurrentReservations: getCurrentReservations,
            addOrUpdateReservations: addOrUpdateReservations,
            addOrUpdateReservation: addOrUpdateReservation,
            deleteReservation: deleteReservation,

            provideTestData : provideTestData
        };
        return result;
    }
}).call(this);