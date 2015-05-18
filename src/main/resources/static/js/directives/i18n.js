( function () {
    'use strict';

    angular.module('localization', []).factory('localize', localize);

    localize.$inject = ['$http', '$rootScope', '$filter', '$log', 'LOCALE_FILES'];

    function localize($http, $rootScope, $filter, $log, LOCALE_FILES) {

        var dictionary = [];
        var language = "ro_RO";

        function addEntriesToDictionary(entries) {
            // store the returned array in the dictionary
            angular.forEach(entries, function (entry) {
                dictionary.push(entry);
            });
            // broadcast that the dictionary has been altered
            $rootScope.$broadcast('localizeResourcesUpdated');
        }

        function addEntryToDictionary(entry) {
            //adds an entry to the dictionary
            dictionary.push(entry);
            // broadcast that the dictionary has been altered
            $rootScope.$broadcast('localizeResourcesUpdated');
        }

        // success handler for all server communication
        function successCallback(data) {
            // store the returned array in the dictionary
            angular.forEach(data, function (entry) {
                dictionary.push(entry);
            });
            // broadcast that the dictionary has been altered
            $rootScope.$broadcast('localizeResourcesUpdated');
        }

        // allows setting of language on the fly
        function setLanguage(value) {
            language = value;
            //loading file for the first time
            initLocalizedResources();
        }

        function getLanguage() {
            return language;
        }

        // loads the language resource file from the server
        function initLocalizedResources() {
            // request the resource file
            angular.forEach(LOCALE_FILES, function (file) {
                $http({
                    method: "GET",
                    url: file,
                    cache: false
                }).success(successCallback).error(function (error) {
                    $log.error("Failed to find " + file);
                    $log.error(error);
                });
            });
        }

        // checks the dictionary for a localized resource string
        function getLocalizedString() {
            var namedKey = arguments[0];
            var result = '';// default the result to an empty string
            if (namedKey === undefined) {
                $log.error("LOCALIZE ERROR : attempted to read KEY UNDEFINED");
                return result;
            }
            else if (namedKey === '') {
                $log.error("LOCALIZE ERROR : attempted to read KEY EMPTY STRING");
                return result;
            }
            if (dictionary.length <= 0) {
                return result;
            }
            // make sure the dictionary has valid data
            if ((dictionary !== []) && (dictionary.length > 0)) {
                // use the filter service to only return those entries which match the value
                // and only take the first result
                var entry = $filter('filter')(dictionary, function (element) {
                        return element.key === namedKey;
                    }
                )[0];
                if (entry === undefined) {
                    $log.error("LOCALIZE ERROR : Could not find KEY named " + namedKey + " for language " + language + ". FORGOT TO DECLARE IT?");
                    return namedKey;
                } else {
                    // set the result getting value for the current language
                    result = entry[language];
                }
            }
            //replaces {0} , {1} and so on
            if (arguments.length > 1){
                for (var index = 1; index < arguments.length; index++) {
                    var target = '{' + (index-1) + '}';
                    result = result.replace(target, arguments[index]);
                }
            }
            //console.log(result+ " FOR "+localize.language);
            // return the value to the call
            return result;
        }

        var service = {
            dictionary: dictionary,
            setLanguage: setLanguage,
            getLanguage: getLanguage,
            getLocalizedString: getLocalizedString,
            addEntryToDictionary: addEntryToDictionary,
            addEntriesToDictionary: addEntriesToDictionary
        };
        initLocalizedResources();
        return service;
    }


    angular.module('localization').directive('updateTitle', updateTitle);
    updateTitle.$inject = ['$rootScope', '$timeout', 'localize'];

    function updateTitle($rootScope, $timeout, localize) {
        return {
            link: function (scope, element) {
                var listener = function (event, toState, toParams, fromState, fromParams) {
                    // Set asynchronously so page changes before title does
                    $timeout(function () {
                        element.text(localize.getLocalizedString(toState.name));
                    });
                };
                $rootScope.$on('$stateChangeStart', listener);
            }
        };
    }

// localization service responsible for retrieving resource files from the server and
// managing the translation dictionary

// simple translation filter
// usage {{ TOKEN | i18n }}
    angular.module('localization').filter('i18n', i18Filter);
    i18Filter.$inject = ['localize'];
    function i18Filter(localize) {
        return function (input) {
            //console.log(input);
            return localize.getLocalizedString(input);
        };
    }

// translation directive that can handle dynamic strings
// updates the text value of the attached element
// usage <span data-i18n="TOKEN" ></span>
// or
// <span data-i18n="TOKEN|VALUE1|VALUE2" ></span>

    angular.module('localization').directive('i18n', i18nDirective);
    i18nDirective.$inject = ['localize'];
    function i18nDirective(localize) {
        function updateText(elm, token) {
            var values = token.split('|');
            if (values.length >= 1) {
                // construct the tag to insert into the element
                var tag = localize.getLocalizedString(values[0]);
                // update the element only if data was returned
                if ((tag !== null) && (tag !== undefined) && (tag !== '')) {
                    if (values.length > 1) {
                        for (var index = 1; index < values.length; index++) {
                            if (values[index] === 'lowercase') {
                                tag = tag.toLowerCase();
                            } else {
                                var target = '{' + (index - 1) + '}';
                                tag = tag.replace(target, values[index]);
                            }
                        }
                    }
                    // insert the text into the element
                    elm.text(tag);
                }
            }
        }

        function link(scope, elm, attrs) {
            scope.$on('localizeResourcesUpdated', function () {
                directive.updateText(elm, attrs.i18n);
            });

            attrs.$observe('i18n', function (value) {
                directive.updateText(elm, attrs.i18n);
            })
        }

        var directive = {
            restrict: "EAC",
            updateText: updateText,
            link: link
        }
        return directive;
    };

// translation directive that can handle dynamic strings
// updates the attribute value of the attached element
// usage <span data-i18n-attr="TOKEN|ATTRIBUTE" ></span>
// or
// <span data-i18n-attr="TOKEN|ATTRIBUTE|VALUE1|VALUE2" ></span>
    angular.module('localization').directive('i18nAttr', i18nAttribute);
    i18nAttribute.$inject = ['localize'];
    function i18nAttribute(localize) {
        function updateText(elm, token) {
            var values = token.split('|');
            // construct the tag to insert into the element
            var tag = localize.getLocalizedString(values[0]);
            // update the element only if data was returned
            if ((tag !== null) && (tag !== undefined) && (tag !== '')) {
                if (values.length > 2) {
                    for (var index = 2; index < values.length; index++) {
                        var target = '{' + (index - 2) + '}';
                        tag = tag.replace(target, values[index]);
                    }
                }

                // insert the text into the element
                elm.attr(values[1], tag);
            }
        }

        function link(scope, elm, attrs) {
            scope.$on('localizeResourcesUpdated', function () {
                directive.updateText(elm, attrs.i18nAttr);
            });

            attrs.$observe('i18nAttr', function (value) {
                directive.updateText(elm, value);
            });
        }

        var directive = {
            restrict: "EAC",
            updateText: updateText,
            link: link
        };
        return directive;
    }
}());