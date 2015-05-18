( function () {

    'use strict';

    angular.module('mdFixedBottomSheet', ['material.core']);

    angular.module('mdFixedBottomSheet').directive('mdFixedBottomSheet', MdFixedBottomSheetDirective);

    angular.module('mdFixedBottomSheet').provider('$mdFixedBottomSheet', MdFixedBottomSheetProvider);

    function MdFixedBottomSheetDirective() {
        return {
            restrict: 'E'
        };
    }

    function MdFixedBottomSheetProvider($$interimElementProvider) {

        return $$interimElementProvider('$mdFixedBottomSheet')
            .setDefaults({
                methods: ['disableParentScroll', 'escapeToClose', 'targetEvent'],
                options: bottomSheetDefaults
            });

        /* @ngInject */
        function bottomSheetDefaults($animate, $mdUtil, $mdTheming) {
            return {
                themable: true,
                targetEvent: null,
                onShow: onShow,
                onRemove: onRemove,
                escapeToClose: false,
                disableParentScroll: false
            };

            function onShow(scope, element, options) {
                var bottomSheet = new FixedBottomSheet($mdUtil.extractElementByName(element, 'md-fixed-bottom-sheet'));

                options.bottomSheet = bottomSheet;
                $mdTheming.inherit(bottomSheet.element, options.parent);
                scope.$on("$destroy", function(){
                    onRemove(scope, element, options);
                });
                return $animate.enter(bottomSheet.element, options.parent);
            }

            function onRemove(scope, element, options) {
                var bottomSheet = options.bottomSheet;
                return $animate.leave(bottomSheet.element).then(function() {
                    bottomSheet.cleanup();
                });
            }

            function FixedBottomSheet(element){
                return {
                    element : element,
                    cleanup: cleanup
                }
                function cleanup(){
                }
            }
        }
    }
}());