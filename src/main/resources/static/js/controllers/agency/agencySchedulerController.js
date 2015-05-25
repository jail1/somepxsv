/**
 * Created by Badu on 2/15/2015.
 */
(function () {
    "use strict";

    angular.module('photoX').controller('schedulerCalendarController', Controller);

    angular.module('photoX').controller('ToastCtrl', function ($scope, $mdToast) {
        $scope.closeToast = function () {
            $mdToast.hide();
        };
    });

    Controller.$inject = ['$scope', '$rootScope', '$log', 'moment', 'schedulingServices', '$mdDialog', '$mdSidenav', '$mdToast', '$state', '$stateParams', '$timeout', 'localize'];

    function Controller($scope, $rootScope, $log, moment, applicationServices, $mdDialog, $mdSidenav, $mdToast, $state, $stateParams, $timeout, localize) {

        /*
        * #############
        *   Toggle-able
        * #############
        */

        $scope.showCustomToast = function () {
            $mdToast.show({
                controller: 'ToastCtrl',
                template: '<md-toast>Yeap, informatia a fost salvata! <md-icon md-svg-icon="cloud-check" style="color:white"></md-icon></md-toast>',
                hideDelay: 6000
            });
        };

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
    
            function afterShowAnimation(scope, element, options) {
                // post-show code here: DOM element focus, etc.
            }

        };

        $scope.showSelectProductModal = function($event) {
            $mdDialog.show({
                targetEvent: $event,
                controller: function() {
                    $scope.selectedProduct;
                }, 
                templateUrl: 'views/dialogs/select.product.dialog.tmpl.html'
            })
            .then(function(answer) {
                $scope.alertPick = 'You said the information was "' + answer + '".';
            }, function() {
                $scope.alertPick = 'You cancelled the dialog.';
            });

            console.log($scope.alertPick)
        };

        /**
         * Table setup
         * TODO : remove id when you finish
         * TODO : add more columns like status and shit
         *
         * //read this
         * http://liamkaufman.com/blog/2013/05/13/understanding-angularjs-directives-part1-ng-repeat-and-compile/
         */
        $scope.tableHeaders = [
            {
                name: 'ID', 
                field: 'id'
            },
            {
                name: localize.getLocalizedString('_NoOrdersMonth_'),
                field: 'startsAt'
            },
            {
                name: localize.getLocalizedString('_Client_'),
                field: 'title'
            },
            {
                name: localize.getLocalizedString('_Product_'),
                field: 'product'
            }
        ];
        $scope.tableSortableFields = ['startsAt', 'product', 'title'];
        $scope.tableElementsPerPage = 10;

        $scope.tableRowClicked = function (event, row, type) {
            $log.log("Row clicked : " + type);
            $log.log(row);
            if (type == 'delete') {

                event.stopImmediatePropagation();
                var confirm = $mdDialog.confirm()
                    .parent(angular.element(document.body))
                    .title("Ștergeți rezervarea ... ?")
                    .ariaLabel('Delete')
                    .ok("Da")
                    .cancel("Nu").
                    targetEvent(event);
                $mdDialog.show(confirm).then(function () {
                    $log.log("Removing");
                }, function () {
                    $log.log("NOT Removing");
                });

            }
        }
        /**
         * End table setup
         */

        $scope.switchBetweenModes = function () {
            if ($state.current.name == 'calendar'){
                $state.go('list');
            }else{
                $state.go('calendar');
            }
        };
        $scope.openSettings = function(){
            $state.go('scheduler.settings');
        }

        $scope.init = function () {
            $scope.$state = $state;
            //$scope.showCustomToast();
            $scope.calendarView = 'day';
            $scope.currentDay = new Date();
            $scope.events = [];
            applicationServices.provideTestScheduleData().then(function (result) {
                $scope.events = result;
            });
            applicationServices.provideTestProductData().then(function (result) {
                $scope.products = result;
            });
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


        $scope.openPickProducts = function() {
            // $mdSidenav('newSchedule').toggle();
            $mdSidenav('pickProducts').toggle();
        }

        $scope.openNewSchedule = function () {
            $mdSidenav('newSchedule').toggle();
        };
        $scope.validateAndSaveData = function () {
            $mdSidenav('newSchedule').toggle();
            $log.log("Cleanup");
        }

        var currentYear = moment().year();
        var currentMonth = moment().month();
        var currentDay = moment().date();

        

        $scope.closeDialog = function () {
            $log.log("Picker dialog closed : " + $scope.reservationTime);
            $mdDialog.hide();
        }

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
            $scope.selectedEventId = event.id;
            showModal('Clicked', event);
        };

        $scope.eventEdited = function (event) {
            $scope.selectedEventId = event.id;
            showModal('Edited', event);
        };

        $scope.eventDeleted = function (event) {
            $scope.selectedEventId = event.id;
            showModal('Deleted', event);
        };

        $scope.eventDrillDownClick = function (calendarEvent) {
            $scope.selectedEventId = event.id;
            showModal('Drilldown click', calendarEvent);
        }

        /*
        * #############
        *   Utils
        * #############
        */

        $scope.toggle = function ($event, field, event) {
            $log.log('toggle');
            $event.preventDefault();
            $event.stopPropagation();

            event[field] = !event[field];
        };
        $scope.init();

        //ListView
        $scope.toggleSearch = false;

        $scope.pickedDate = new Date(currentYear, currentMonth, currentDay - 13, 10, 0);
        $scope.dateSelected = function (newValue) {
            $log.log("Date time -------");
            $log.log(newValue);
            $log.log("------- Date time");
        };

        

    }



}).call(this);

