(function () {
    'use strict'

    angular.module('menu', []).directive('menuLink', function () {
        return {
            scope: {
                section: '='
            },
            templateUrl: 'views/fragments/menu-link.tmpl.html',
            link: function ($scope, $element) {
                var controller = $element.parent().controller();
                $scope.isSelected = function () {
                    return controller.isSelected($scope.section);
                };
                $scope.focusSection = function () {
                    controller.toggleCheckToggleOpen($scope.section);
                };
            }
        };
    }).directive('menuToggle', function () {
            return {
                scope: {
                    section: '='
                },
                templateUrl: 'views/fragments/menu-toggle.tmpl.html',
                link: function ($scope, $element) {
                    var controller = $element.parent().controller();
                    $scope.isOpen = function () {
                        return controller.isOpen($scope.section);
                    };
                    $scope.toggle = function () {
                        controller.toggleOpen($scope.section);
                    };

                    var parentNode = $element[0].parentNode.parentNode.parentNode;
                    if (parentNode.classList.contains('parent-list-item')) {
                        var heading = parentNode.querySelector('h2');
                        if (heading) {
                            $element[0].firstChild.setAttribute('aria-describedby', heading.id);
                        }
                    }
                }
            };
        }).filter('nospace', function () {
            return function (value) {
                return (!value) ? '' : value.replace(/ /g, '');
            };
        })
        .filter('humanizeDoc', function () {
            return function (doc) {
                if (!doc) return;
                if (doc.type === 'directive') {
                    return doc.name.replace(/([A-Z])/g, function ($1) {
                        return '-' + $1.toLowerCase();
                    });
                }
                return doc.label || doc.name;
            };
        });
})();