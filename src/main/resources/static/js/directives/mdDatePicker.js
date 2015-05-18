( function () {

    'use strict';

    angular.module('mdDatePicker', []);

    function mdDatePickerController($scope, $timeout, $window, $templateCache, $mdTheming, $animate, $compile, $document, $location) {
        var self = this,
            activeLocale,
            $datePicker,
            backdrop,
            parent,
            $datePickerContainer,
            storedLocationHash;

        var unbindOnDestroy = [];
        var unbindDestroyListener = $scope.$on('$destroy', function () {
            unbindOnDestroy.forEach(function (unbind) {
                unbind();
            });
        });
        unbindOnDestroy.push(unbindDestroyListener);

        this.init = function (element, locale) {
            $datePickerContainer = element;
            self.build(locale);
        };

        this.registerDestroyFn = function (fn) {
            unbindOnDestroy.push(fn);
        }

        this.build = function (locale) {
            if (locale === activeLocale) {
                return;
            }

            parent = angular.element($document[0].querySelector('body'));
            activeLocale = locale;
            moment.locale(activeLocale);

            if (angular.isDefined($scope.model)) {
                $scope.selected = {
                    model: moment($scope.model).format('LL'),
                    date: $scope.model
                };
                $scope.activeDate = moment($scope.model);
            } else {
                $scope.selected = {
                    model: undefined,
                    date: new Date()
                };
                $scope.activeDate = moment();
            }

            $scope.moment = moment;
            $scope.days = [];
            $scope.daysOfWeek = [moment.weekdaysMin(1), moment.weekdaysMin(2), moment.weekdaysMin(3), moment.weekdaysMin(4), moment.weekdaysMin(5), moment.weekdaysMin(6), moment.weekdaysMin(0)];
            $scope.years = [];

            var hoursWatcher = $scope.$watch('hours', function (newValue, oldValue) {
                $scope.selected.date = moment($scope.selected.date).hours(newValue);
            });
            var minutesWatcher = $scope.$watch('minutes', function (newValue, oldValue) {
                $scope.selected.date = moment($scope.selected.date).minutes(newValue);
            });

            this.registerDestroyFn(hoursWatcher);
            this.registerDestroyFn(minutesWatcher);

            $scope.hours = moment($scope.selected.date).hours();
            $scope.minutes = moment($scope.selected.date).minutes();

            for (var y = moment().year() - 100; y <= moment().year() + 100; y++) {
                $scope.years.push(y);
            }
            generateCalendar();

            // $timeout(function () {
            //     $scope.openPicker();
            // }, 1000);
        };

        $scope.previousMonth = function () {
            $scope.activeDate = $scope.activeDate.subtract(1, 'month');
            generateCalendar();
        };

        $scope.nextMonth = function () {
            $scope.activeDate = $scope.activeDate.add(1, 'month');
            generateCalendar();
        };

        $scope.operateHours = function (value) {
            if (value > 0) {
                $scope.selected.date = moment($scope.selected.date).add(1, 'hours');
            } else {
                $scope.selected.date = moment($scope.selected.date).subtract(1, 'hours');
            }
            $scope.hours = moment($scope.selected.date).hours();
        };

        $scope.operateMinutes = function (value) {
            if (value > 0) {
                $scope.selected.date = moment($scope.selected.date).add(15, 'minutes');
            } else {
                $scope.selected.date = moment($scope.selected.date).subtract(15, 'minutes');
            }
            $scope.minutes = moment($scope.selected.date).minutes();
        };

        $scope.select = function (day) {
            $scope.selected = {
                model: day.format('LL'),
                date: day.toDate()
            };
            $scope.model = day.toDate();
            generateCalendar();
        };

        $scope.selectYear = function (year) {
            $scope.yearSelection = false;
            $scope.selected.model = moment($scope.selected.date).year(year).format('LL');
            $scope.selected.date = moment($scope.selected.date).year(year).toDate();
            $scope.model = moment($scope.selected.date).toDate();
            $scope.activeDate = $scope.activeDate.add(year - $scope.activeDate.year(), 'year');
            $location.hash(self.storedLocationHash);
            generateCalendar();
        };

        $scope.openPicker = function () {
            $scope.yearSelection = false;
            self.storedLocationHash = $location.hash();
            // Add a backdrop that will close on click
            backdrop = $compile('<md-backdrop class="md-opaque md-bottom-sheet-backdrop">')($scope);
            backdrop.on('click', function () {
                $timeout($scope.closePicker);
            });
            $mdTheming.inherit(backdrop, parent);
            $animate.enter(backdrop, parent);
            $datePicker = $compile($templateCache.get($scope.popupTemplate))($scope);
            $animate.enter($datePicker, parent);
        };

        $scope.closePicker = function () {
            $location.hash(self.storedLocationHash);
            $animate.leave(backdrop);
            $animate.leave($datePicker);
            $scope.dateSelected({$value: moment($scope.selected.date).toDate()});
        };

        $scope.displayYearSelection = function () {
            //Bogdan : bug fixed, if year selection, hide it
            if ($scope.yearSelection) {
                console.log("year selection already");
                $scope.yearSelection = false;
                return;
            }
            var calendarHeight = angular.element($document[0].getElementsByClassName('date-picker-calendar'));
            var computedStyle = $window.getComputedStyle(calendarHeight[0]);
            $scope.yearSelection = true;
            var $yearSelector = angular.element($document[0].getElementsByClassName('date-picker-year-selector'));
            $yearSelector.css('max-height', computedStyle.height);
            $yearSelector.css('height', '100%');
            $timeout(function () {
                $location.hash('year-' + $scope.activeDate.format('YYYY'));
            });
        };

        function generateCalendar() {
            var days = [],
                previousDay = angular.copy($scope.activeDate).date(0),
                firstDayOfMonth = angular.copy($scope.activeDate).date(1),
                lastDayOfMonth = angular.copy(firstDayOfMonth).endOf('month'),
                maxDays = angular.copy(lastDayOfMonth).date();
            $scope.emptyFirstDays = [];
            for (var i = firstDayOfMonth.day() === 0 ? 6 : firstDayOfMonth.day() - 1; i > 0; i--) {
                $scope.emptyFirstDays.push({});
            }
            for (var j = 0; j < maxDays; j++) {
                var date = angular.copy(previousDay.add(1, 'days'));
                date.selected = angular.isDefined($scope.selected.model) && date.isSame($scope.selected.date, 'day');
                date.today = date.isSame(moment(), 'day');
                days.push(date);
            }
            $scope.emptyLastDays = [];
            for (var k = 7 - (lastDayOfMonth.day() === 0 ? 7 : lastDayOfMonth.day()); k > 0; k--) {
                $scope.emptyLastDays.push({});
            }
            $scope.days = days;
        }
    };


    angular.module('mdDatePicker').controller('mdDatePickerController', mdDatePickerController);

    mdDatePickerController.$inject = ['$scope', '$timeout', '$window', '$templateCache', '$mdTheming', '$animate', '$compile', '$document', '$location'];

    function mdDatePicker() {
        return {
            restrict: 'AE',
            controller: 'mdDatePickerController',
            scope: {
                model: '=',
                label: '@',
                fixedLabel: '&',
                icon: '@',
                dateSelected: '&dateSelected',
                popupTemplate: '='
            },
            templateUrl: 'views/templates/date-picker.html',
            link: function (scope, element, attrs, ctrl) {
                ctrl.init(element, checkLocale(attrs.locale));

                var localeObserver = attrs.$observe('locale', function () {
                    ctrl.build(checkLocale(attrs.locale));
                });
                ctrl.registerDestroyFn(localeObserver);

                function checkLocale(locale) {
                    if (!locale) {
                        return (navigator.language !== null ? navigator.language : navigator.browserLanguage).split("_")[0].split("-")[0] || 'en';
                    }
                    return locale;
                }
            }
        };
    };

    angular.module('mdDatePicker').directive('mdDatePicker', mdDatePicker);

}());