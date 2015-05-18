/**
 * Created by Badu on 2/15/2015.
 */
(function () {
    "use strict";

    angular.module('photoX').controller('MainCtrl', Controller);

    Controller.$inject = ['$controller', '$scope', '$rootScope', '$timeout', '$log', 'moment', '$mdDialog', '$mdSidenav', '$mdBottomSheet', '_'];

    function Controller($controller , $scope, $rootScope, $timeout, $log, moment, $mdDialog, $mdSidenav, $mdBottomSheet, _) {
        angular.extend(this, $controller('MenuController' , {$scope :$scope}));
        //    _.keys($scope);
        $scope.init = function () {

            //$log.log("Controller init " , $scope.isOpen);

            $scope.calendarView = 'day';
            $scope.calendarDay = new Date();

            $scope.inCalendarMode = false;
            /**
             applicationServices.getPersonsProducts().then(function(response){
                //$log.log(response);
                $log.log($rootScope.personsProducts);
            });
             applicationServices.getProductsPrices().then(function (response) {
                //$log.log(response);
                $log.log($rootScope.productsPrices);
            });
             **/
        };

        $scope.loadUsers = function () {
            // Use timeout to simulate a 650ms request.
            $scope.users = [];
            return $timeout(function () {
                $scope.users = [
                    {id: 1, name: 'Scooby Doo'},
                    {id: 2, name: 'Shaggy Rodgers'},
                    {id: 3, name: 'Fred Jones'},
                    {id: 4, name: 'Daphne Blake'},
                    {id: 5, name: 'Velma Dinkley'},
                ];
            }, 650);
        };

        $scope.showGridBottomSheet = function ($event) {
            $scope.alert = '';
            $mdBottomSheet.show({
                templateUrl: 'bottom-sheet-grid-template.html',
                controller: 'GridBottomSheetCtrl',
                targetEvent: $event
            }).then(function (clickedItem) {
                $scope.alert = clickedItem.name + ' clicked!';
            });
        };

        $scope.openRightMenu = function () {
            $mdSidenav('right').toggle();
        };

        var currentYear = moment().year();
        var currentMonth = moment().month();
        var currentDay = moment().date();

        $scope.events = [

            {
                title: 'Popescu Costel',
                product: 'Balet pe gheata',
                type: 'warning',
                starts_at: new Date(currentYear, currentMonth, currentDay - 13, 10, 0),
                ends_at: new Date(currentYear, currentMonth, currentDay - 13, 11, 0)
            },
            {
                title: 'Bogdan Dinu',
                product: 'Terapie Yumeiho',
                type: 'info',
                starts_at: new Date(currentYear, currentMonth, currentDay - 13, 17, 0),
                ends_at: new Date(currentYear, currentMonth, currentDay - 13, 18, 0)
            }
        ];
        //not used
        $scope.openTimeDatePicker = function () {
            $mdDialog.show({
                template: '<md-dialog>' +
                '  <md-content>' +
                '   <time-date-picker ng-model="reservationTime" orientation="true"></time-date-picker></md-content>' +
                '  <div class="md-actions">' +
                '    <md-button ng-click="closeDialog()">' +
                '      Inchide' +
                '    </md-button>' +
                '  </div>' +
                '</md-dialog>',
                controller: 'MainCtrl'
            });
        };
        //not used
        $scope.closeDialog = function () {
            $log.log("Picker dialog closed : " + $scope.reservationTime);
            $mdDialog.hide();
        }
        //not used
        $scope.openDialog = function ($event) {
            $mdDialog.show({
                targetEvent: $event,
                template: '<md-dialog>' +
                '  <md-content>Hello {{ userName }}!</md-content>' +
                '  <div class="md-actions">' +
                '    <md-button ng-click="closeDialog()">' +
                '      Close' +
                '    </md-button>' +
                '  </div>' +
                '</md-dialog>',
                controller: 'DialogController',
                onComplete: afterShowAnimation,
                locals: {
                    name: 'Bobby'
                }
            });
            // When the 'enter' animation finishes...
            function afterShowAnimation(scope, element, options) {
                // post-show code here: DOM element focus, etc.
            }
        };

        $scope.switchBetweenModes = function () {
            $scope.inCalendarMode = !$scope.inCalendarMode;
        };

        function showModal(action, event) {
            console.log("should show modal");
            /**           $modal.open({
                  templateUrl: 'modalContent.html',
                  controller: function($scope, $modalInstance) {
                      $scope.$modalInstance = $modalInstance;
                      $scope.action = action;
                      $scope.event = event;
                  }
              });**/
        }

        $scope.eventClicked = function (event) {
            $log.log(event);
            showModal('Clicked', event);
        };

        $scope.eventEdited = function (event) {
            showModal('Edited', event);
        };

        $scope.eventDeleted = function (event) {
            showModal('Deleted', event);
        };

        $scope.setCalendarToToday = function () {
            $scope.calendarDay = new Date();
        };

        $scope.toggle = function ($event, field, event) {
            $event.preventDefault();
            $event.stopPropagation();

            event[field] = !event[field];
        };
        $scope.init();

        //ListView
        $scope.toggleSearch = false;

        $scope.headers = [
            {
                name: 'Data și ora rezervării',
                field: 'starts_at'
            }, {
                name: 'Serviciu',
                field: 'product'
            }, {
                name: 'Nume',
                field: 'title'
            }
        ];
        $scope.pickedDate = new Date(currentYear, currentMonth, currentDay - 13, 10, 0);
        $scope.dateSelected = function (newValue) {
            $log.log("Date time -------");
            $log.log(newValue);
            $log.log("------- Date time");
        };
        $scope.custom = {name: 'bold', description: 'grey', last_modified: 'grey'};
        $scope.sortable = ['starts_at', 'product', 'title'];
        $scope.count = 10;

        $scope.showAdvancedDialog = function (ev) {
            $mdDialog.show({
                controller: DialogController,
                templateUrl: 'views/dialogs/dialog1.tmpl.html',
                targetEvent: ev,
            })
                .then(function (answer) {
                    $log.log("You answered : " + answer);
                    $scope.alert = 'You said the information was "' + answer + '".';
                }, function () {
                    $log.log("You canceled ");
                    $scope.alert = 'You cancelled the dialog.';
                });
        };
    }

    angular.module('photoX').controller('DialogController', DialogController);

    DialogController.$inject = ['$scope', '$mdDialog'];

    function DialogController($scope, $mdDialog) {
        $scope.hide = function () {
            $mdDialog.hide();
        };
        $scope.cancel = function () {
            $mdDialog.cancel();
        };
        $scope.answer = function (answer) {
            $mdDialog.hide(answer);
        };
    }

}).call(this);