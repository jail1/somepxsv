( function () {
    'use strict';
    angular
        .module('dropzone.module', []);


    angular.module('dropzone.module', []).directive('fileDropperBrowser', dropzone);
    dropzone.$inject = ['$log'];
    function dropzone($log) {
        function link(scope, element, attrs) {
            var dropZone = element;
            var dropZoneDom = element.get(0);
            var fileInput = angular.element('<input type="file" />');
            fileInput.css({
                position: 'absolute',
                top: 0,
                left: 0,
                'z-index': '2',
                width: '100%',
                height: '100%',
                opacity: '0',
                cursor: 'pointer'
            });
            angular.element(element).append(fileInput);
            fileInput.bind('change', function (e) {
                scope.$apply(function () {
                    scope.onFileChosen({files: e.target.files});
                });
            });
            dropZoneDom.addEventListener('dragover', function (evt) {
                evt.stopPropagation();
                evt.preventDefault();
                evt.dataTransfer.dropEffect = 'copy';
                dropZone.addClass("dragover");
            }, false);
            dropZoneDom.addEventListener('dragleave', function (evt) {
                evt.stopPropagation();
                evt.preventDefault();
            }, false);
            dropZoneDom.addEventListener('drop', function (evt) {
                evt.stopPropagation();
                evt.preventDefault();
                dropZone.removeClass("dragover");
                scope.$apply(function () {
                    scope.onFileChosen({files: evt.dataTransfer.files});
                });
            }, false);
        }
        return {
            restrict: 'EA',
            replace: true,
            scope: {
                onFileChosen: '&'
            },
            link : link
        }
    }
}());