/**
 * Created by Badu on 2/15/2015.
 */
(function () {
    "use strict";

    angular.module('photoX').service('schedulingServices', applicationServices);

    applicationServices.$inject = ['$http', '$q', '$rootScope', '$log', '$timeout'];

    function applicationServices($http, $q, $rootScope, $log, $timeout) {

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
            provideTestData : provideTestData
        };
        return result;
    }
}).call(this);