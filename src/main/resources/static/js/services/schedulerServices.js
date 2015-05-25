/**
 * Created by Badu on 2/15/2015.
 */
(function () {
    "use strict";

    angular.module('photoX').service('schedulingServices', applicationServices);

    applicationServices.$inject = ['$http', '$q', '$rootScope', '$log', '$timeout'];

    function applicationServices($http, $q, $rootScope, $log, $timeout) {

        function provideTestProductData() {
            
            var d = $q.defer();
            $timeout(function() {

                var testProducts = [

                    {
                        "productId": 1,
                        "totalBasePrice": 0.05640151499999999,
                        "name": "Fotografia 305 mm",
                        "description": "810x305mm",
                        "groupedSelectors": [
                            {
                                "requiredQuantity": 1,
                                "selectorSequence": 1,
                                "selectors": [
                                    {
                                        "name": "Edge E 305mm (mată)",
                                        "description": "Rolă, lungime 93 m, lățime 305 mm",
                                        "pricePerUnit": 1,
                                        "materialId": 14
                                    },
                                    {
                                        "name": "Edge F 305mm (lucioasă)",
                                        "description": "Rolă, lungime 93 m, lățime 305mm",
                                        "pricePerUnit": 1,
                                        "materialId": 15
                                    },
                                    {
                                        "name": "Royal N 305mm (mată)",
                                        "description": "Rolă, lungime 78m, lățime 305mm",
                                        "pricePerUnit": 1,
                                        "materialId": 16
                                    },
                                    {
                                        "name": "Royal F 305mm (lucioasă)",
                                        "description": "Rolă, lungime 78m, lățime 305mm",
                                        "pricePerUnit": 1,
                                        "materialId": 17
                                    },
                                    {
                                        "name": "Premier F 305mm (lucioasă)",
                                        "description": "Rolă, lungime 88m, lățime 305mm",
                                        "pricePerUnit": 1,
                                        "materialId": 18
                                    },
                                    {
                                        "name": "Metal 305mm",
                                        "description": "Rolă, lungime 28.2 m, lățiime 305 mm",
                                        "pricePerUnit": 4.47,
                                        "materialId": 3
                                    },
                                    {
                                        "name": "Premier Y 305mm (raster)",
                                        "description": "Rolă, lungime 88 m, lățime 305 mm",
                                        "pricePerUnit": 1.905,
                                        "materialId": 4
                                    },
                                    {
                                        "name": "Premier E 305 mm(mată)",
                                        "description": "Rolă, 88m lungime, 305mm lățime",
                                        "pricePerUnit": 1,
                                        "materialId": 72
                                    }
                                ],
                                "parentId": 3
                            }
                        ],
                        "extraData": "{\"width\":305,\"height\":810}",
                        "complex": false
                    },
                    {
                        "productId": 4,
                        "totalBasePrice": 9.81608515,
                        "name": "Album 40x30",
                        "description": "",
                        "groupedSelectors": [
                            {
                                "requiredQuantity": 2,
                                "selectorSequence": 2,
                                "selectors": [
                                    {
                                        "name": "Faceoff alb",
                                        "description": "",
                                        "pricePerUnit": 3,
                                        "materialId": 9
                                    },
                                    {
                                        "name": "Faceoff negru",
                                        "description": "",
                                        "pricePerUnit": 3,
                                        "materialId": 10
                                    }
                                ],
                                "parentId": 23
                            },
                            {
                                "requiredQuantity": 1,
                                "selectorSequence": 3,
                                "selectors": [
                                    {
                                        "name": "Copertă piele",
                                        "description": "",
                                        "pricePerUnit": 7,
                                        "materialId": 12
                                    },
                                    {
                                        "name": "Copertă carton",
                                        "description": "",
                                        "pricePerUnit": 5,
                                        "materialId": 13
                                    }
                                ],
                                "parentId": 22
                            },
                            {
                                "requiredQuantity": 10,
                                "selectorSequence": 1,
                                "selectors": [
                                    {
                                        "name": "Edge E 305mm (mată)",
                                        "description": "Rolă, lungime 93 m, lățime 305 mm",
                                        "pricePerUnit": 0.6,
                                        "materialId": 14
                                    },
                                    {
                                        "name": "Edge F 305mm (lucioasă)",
                                        "description": "Rolă, lungime 93 m, lățime 305mm",
                                        "pricePerUnit": 0.6,
                                        "materialId": 15
                                    },
                                    {
                                        "name": "Royal N 305mm (mată)",
                                        "description": "Rolă, lungime 78m, lățime 305mm",
                                        "pricePerUnit": 0.7,
                                        "materialId": 16
                                    },
                                    {
                                        "name": "Royal F 305mm (lucioasă)",
                                        "description": "Rolă, lungime 78m, lățime 305mm",
                                        "pricePerUnit": 0.7,
                                        "materialId": 17
                                    },
                                    {
                                        "name": "Premier F 305mm (lucioasă)",
                                        "description": "Rolă, lungime 88m, lățime 305mm",
                                        "pricePerUnit": 0.8,
                                        "materialId": 18
                                    },
                                    {
                                        "name": "Metal 305mm",
                                        "description": "Rolă, lungime 28.2 m, lățiime 305 mm",
                                        "pricePerUnit": 2.4,
                                        "materialId": 3
                                    },
                                    {
                                        "name": "Premier Y 305mm (raster)",
                                        "description": "Rolă, lungime 88 m, lățime 305 mm",
                                        "pricePerUnit": 0.8,
                                        "materialId": 4
                                    },
                                    {
                                        "name": "Premier E 305 mm(mată)",
                                        "description": "Rolă, 88m lungime, 305mm lățime",
                                        "pricePerUnit": 0.8,
                                        "materialId": 72
                                    }
                                ],
                                "parentId": 3
                            }
                        ],
                        "extraData": "{\"width\":810,\"height\":305}",
                        "complex": true
                    },
                    {
                        "productId": 14,
                        "totalBasePrice": 0.695547,
                        "name": "Produs simplu fara selector",
                        "description": "Produs simplu fara selector",
                        "extraData": "{\"width\":300,\"height\":300}",
                        "complex": false
                    },
                    {
                        "productId": 16,
                        "totalBasePrice": 0.021585765,
                        "name": "Fotografia 310x310",
                        "description": "810x305mm",
                        "groupedSelectors": [
                            {
                                "requiredQuantity": 1,
                                "selectorSequence": 1,
                                "selectors": [
                                    {
                                        "name": "Edge E 305mm (mată)",
                                        "description": "Rolă, lungime 93 m, lățime 305 mm",
                                        "pricePerUnit": 1,
                                        "materialId": 14
                                    },
                                    {
                                        "name": "Edge F 305mm (lucioasă)",
                                        "description": "Rolă, lungime 93 m, lățime 305mm",
                                        "pricePerUnit": 1,
                                        "materialId": 15
                                    },
                                    {
                                        "name": "Royal N 305mm (mată)",
                                        "description": "Rolă, lungime 78m, lățime 305mm",
                                        "pricePerUnit": 1,
                                        "materialId": 16
                                    },
                                    {
                                        "name": "Royal F 305mm (lucioasă)",
                                        "description": "Rolă, lungime 78m, lățime 305mm",
                                        "pricePerUnit": 1,
                                        "materialId": 17
                                    },
                                    {
                                        "name": "Premier F 305mm (lucioasă)",
                                        "description": "Rolă, lungime 88m, lățime 305mm",
                                        "pricePerUnit": 1,
                                        "materialId": 18
                                    },
                                    {
                                        "name": "Metal 305mm",
                                        "description": "Rolă, lungime 28.2 m, lățiime 305 mm",
                                        "pricePerUnit": 4.47,
                                        "materialId": 3
                                    },
                                    {
                                        "name": "Premier Y 305mm (raster)",
                                        "description": "Rolă, lungime 88 m, lățime 305 mm",
                                        "pricePerUnit": 1.905,
                                        "materialId": 4
                                    },
                                    {
                                        "name": "Premier E 305 mm(mată)",
                                        "description": "Rolă, 88m lungime, 305mm lățime",
                                        "pricePerUnit": 1,
                                        "materialId": 72
                                    }
                                ],
                                "parentId": 3
                            }
                        ],
                        "extraData": "{\"width\":305,\"height\":310}",
                        "complex": false
                    },
                    {
                        "productId": 24,
                        "totalBasePrice": 0.1587762,
                        "name": "Album 7.5x7.5",
                        "description": "",
                        "groupedSelectors": [
                            {
                                "requiredQuantity": 2,
                                "selectorSequence": 2,
                                "selectors": [
                                    {
                                        "name": "Faceoff alb",
                                        "description": "",
                                        "pricePerUnit": 3,
                                        "materialId": 9
                                    },
                                    {
                                        "name": "Faceoff negru",
                                        "description": "",
                                        "pricePerUnit": 3,
                                        "materialId": 10
                                    }
                                ],
                                "parentId": 23
                            },
                            {
                                "requiredQuantity": 1,
                                "selectorSequence": 3,
                                "selectors": [
                                    {
                                        "name": "Copertă piele",
                                        "description": "",
                                        "pricePerUnit": 7,
                                        "materialId": 12
                                    },
                                    {
                                        "name": "Copertă carton",
                                        "description": "",
                                        "pricePerUnit": 5,
                                        "materialId": 13
                                    }
                                ],
                                "parentId": 22
                            },
                            {
                                "requiredQuantity": 10,
                                "selectorSequence": 1,
                                "selectors": [
                                    {
                                        "name": "Edge E 305mm (mată)",
                                        "description": "Rolă, lungime 93 m, lățime 305 mm",
                                        "pricePerUnit": 0.6,
                                        "materialId": 14
                                    },
                                    {
                                        "name": "Edge F 305mm (lucioasă)",
                                        "description": "Rolă, lungime 93 m, lățime 305mm",
                                        "pricePerUnit": 0.6,
                                        "materialId": 15
                                    },
                                    {
                                        "name": "Royal N 305mm (mată)",
                                        "description": "Rolă, lungime 78m, lățime 305mm",
                                        "pricePerUnit": 0.7,
                                        "materialId": 16
                                    },
                                    {
                                        "name": "Royal F 305mm (lucioasă)",
                                        "description": "Rolă, lungime 78m, lățime 305mm",
                                        "pricePerUnit": 0.7,
                                        "materialId": 17
                                    },
                                    {
                                        "name": "Premier F 305mm (lucioasă)",
                                        "description": "Rolă, lungime 88m, lățime 305mm",
                                        "pricePerUnit": 0.8,
                                        "materialId": 18
                                    },
                                    {
                                        "name": "Metal 305mm",
                                        "description": "Rolă, lungime 28.2 m, lățiime 305 mm",
                                        "pricePerUnit": 2.4,
                                        "materialId": 3
                                    },
                                    {
                                        "name": "Premier Y 305mm (raster)",
                                        "description": "Rolă, lungime 88 m, lățime 305 mm",
                                        "pricePerUnit": 0.8,
                                        "materialId": 4
                                    },
                                    {
                                        "name": "Premier E 305 mm(mată)",
                                        "description": "Rolă, 88m lungime, 305mm lățime",
                                        "pricePerUnit": 0.8,
                                        "materialId": 72
                                    }
                                ],
                                "parentId": 3
                            }
                        ],
                        "extraData": "{\"width\":152,\"height\":76}",
                        "complex": true
                    },
                    {
                        "productId": 30,
                        "totalBasePrice": 0,
                        "name": "Album 10x7.5",
                        "description": "",
                        "groupedSelectors": [
                            {
                                "requiredQuantity": 2,
                                "selectorSequence": 2,
                                "selectors": [
                                    {
                                        "name": "Faceoff alb",
                                        "description": "",
                                        "pricePerUnit": 3,
                                        "materialId": 9
                                    },
                                    {
                                        "name": "Faceoff negru",
                                        "description": "",
                                        "pricePerUnit": 3,
                                        "materialId": 10
                                    }
                                ],
                                "parentId": 23
                            },
                            {
                                "requiredQuantity": 1,
                                "selectorSequence": 3,
                                "selectors": [
                                    {
                                        "name": "Copertă piele",
                                        "description": "",
                                        "pricePerUnit": 7,
                                        "materialId": 12
                                    },
                                    {
                                        "name": "Copertă carton",
                                        "description": "",
                                        "pricePerUnit": 5,
                                        "materialId": 13
                                    }
                                ],
                                "parentId": 22
                            },
                            {
                                "requiredQuantity": 10,
                                "selectorSequence": 1,
                                "selectors": [
                                    {
                                        "name": "Edge E 305mm (mată)",
                                        "description": "Rolă, lungime 93 m, lățime 305 mm",
                                        "pricePerUnit": 0.6,
                                        "materialId": 14
                                    },
                                    {
                                        "name": "Edge F 305mm (lucioasă)",
                                        "description": "Rolă, lungime 93 m, lățime 305mm",
                                        "pricePerUnit": 0.6,
                                        "materialId": 15
                                    },
                                    {
                                        "name": "Royal N 305mm (mată)",
                                        "description": "Rolă, lungime 78m, lățime 305mm",
                                        "pricePerUnit": 0.7,
                                        "materialId": 16
                                    },
                                    {
                                        "name": "Royal F 305mm (lucioasă)",
                                        "description": "Rolă, lungime 78m, lățime 305mm",
                                        "pricePerUnit": 0.7,
                                        "materialId": 17
                                    },
                                    {
                                        "name": "Premier F 305mm (lucioasă)",
                                        "description": "Rolă, lungime 88m, lățime 305mm",
                                        "pricePerUnit": 0.8,
                                        "materialId": 18
                                    },
                                    {
                                        "name": "Metal 305mm",
                                        "description": "Rolă, lungime 28.2 m, lățiime 305 mm",
                                        "pricePerUnit": 2.4,
                                        "materialId": 3
                                    },
                                    {
                                        "name": "Premier Y 305mm (raster)",
                                        "description": "Rolă, lungime 88 m, lățime 305 mm",
                                        "pricePerUnit": 0.8,
                                        "materialId": 4
                                    },
                                    {
                                        "name": "Premier E 305 mm(mată)",
                                        "description": "Rolă, 88m lungime, 305mm lățime",
                                        "pricePerUnit": 0.8,
                                        "materialId": 72
                                    }
                                ],
                                "parentId": 3
                            }
                        ],
                        "extraData": "{\"width\":76,\"height\":203}",
                        "complex": true
                    },
                ];

                d.resolve(testProducts);

            }, 1000);

            return d.promise;
        }

        function provideTestScheduleData() {
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
            provideTestScheduleData : provideTestScheduleData,
            provideTestProductData  : provideTestProductData
        };
        return result;
    }
}).call(this);