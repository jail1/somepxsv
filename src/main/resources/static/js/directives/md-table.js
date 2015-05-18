( function () {

    'use strict';

    angular.module('mdTable', []);
    angular.module('mdTable').directive('mdTable', mdTable);
    //mdTable.$inject = ['']
    function mdTable() {
        return {
            restrict: 'E',
            scope: {
                headers: '=',
                sortable: '=',
                content: '&',
                count: '=',
                templateUrl: '=',
                rowClicked: '&rowClicked'
            },
            controller: function ($scope, $filter) {
                var orderBy = $filter('orderBy');
                $scope.search = {};
                $scope.currentPage = 0;
                $scope.clearSearch = function () {
                    delete $scope.search.term;
                }
                $scope.nbOfPages = function () {
                    return Math.ceil($scope.content.length / $scope.count);
                };
                $scope.handleSort = function (field) {
                    if ($scope.sortable.indexOf(field) > -1) {
                        return true;
                    } else {
                        return false;
                    }
                };
                $scope.clicked = function($event, row, type){
                    $scope.rowClicked({event:$event, row: row, type: type});
                }
                $scope.order = function (predicate, reverse) {
                    $scope.content = orderBy($scope.content, predicate, reverse);
                    $scope.predicate = predicate;
                };
                $scope.order($scope.sortable[0], false);
                $scope.getNumber = function (num) {
                    return new Array(num);
                };
                $scope.goToPage = function (page) {
                    $scope.currentPage = page;
                };
                $scope.previousPage = function () {
                    if ($scope.currentPage > 0) {
                        $scope.currentPage--;
                    }
                };
                $scope.nextPage = function () {
                    if ($scope.currentPage < $scope.nbOfPages()) {
                        $scope.currentPage++;
                    }
                }
                var refreshTable = function(value){
                    if (value) {
                        $scope.content = value;
                    }
                }
                var unbindOnDestroy = [];
                unbindOnDestroy.push($scope.$watch($scope.content, refreshTable));
                var unbindDestroyListener = $scope.$on('$destroy', function () {
                    unbindOnDestroy.forEach(function (unbind) {
                        unbind();
                    });
                });
                unbindOnDestroy.push(unbindDestroyListener);
            },
            template: '<div ng-include="templateUrl"></div>'
        }
    }

    angular.module('mdTable').filter('startFrom', startFrom);
    function startFrom() {
        return function (input, start) {
            start = +start;
            return input.slice(start);
        }
    }

}());