(function () {

    'use strict';

    var isDebug = true;

    var iconsUrls = [
        {
            uri: './images/icons/magnify.svg',
            alias: 'magnify'
        },
        {
            uri: './images/icons/camera.svg',
            alias: 'camera'
        },
        {
            uri: './images/icons/pencil.svg',
            alias: 'pencil'
        },
        {
            uri: './images/icons/chevron-left.svg',
            alias: 'chevron-left'
        },
        {
            uri: './images/icons/chevron-right.svg',
            alias: 'chevron-right'
        },
        {
            uri: './images/icons/chevron-up.svg',
            alias: 'chevron-up'
        },
        {
            uri: './images/icons/chevron-down.svg',
            alias: 'chevron-down'
        },
        {
            uri: './images/icons/arrow-up.svg',
            alias: 'arrow-up'
        },
        {
            uri: './images/icons/arrow-down.svg',
            alias: 'arrow-down'
        },
        {
            uri: './images/icons/close.svg',
            alias: 'close'
        },
        {
            uri: './images/icons/alarm.svg',
            alias: 'alarm'
        },
        {
            uri: './images/icons/alert.svg',
            alias: 'alert'
        },
        {
            uri: './images/icons/account-check.svg',
            alias: 'account-check'
        },
        {
            uri: './images/icons/account-plus.svg',
            alias: 'account-plus'
        },
        {
            uri: './images/icons/account-key.svg',
            alias: 'account-key'
        },
        {
            uri: './images/icons/account-remove.svg',
            alias: 'account-remove'
        },
        {
            uri: './images/icons/check.svg',
            alias: 'check'
        },
        {
            uri: './images/icons/cloud-check.svg',
            alias: 'cloud-check'
        },
        {
            uri: './images/icons/plus.svg',
            alias: 'plus'
        },
        {
            uri: './images/icons/bookmark-check.svg',
            alias: 'bookmark-check'
        },
        {
            uri: './images/icons/folder-move.svg',
            alias: 'folder-move'
        },
        {
            uri: './images/icons/menu.svg',
            alias: 'menu'
        },
        {
            uri: './images/icons/view-carousel.svg',
            alias: 'view-carousel'
        },
        {
            uri: './images/icons/logout.svg',
            alias: 'logout'
        },
        {
            uri: './images/icons/cart.svg',
            alias: 'cart'
        },
        {
            uri: './images/icons/eye.svg',
            alias: 'eye'
        },
        {
            uri: './images/icons/arrow-up.svg',
            alias: 'arrow-up'
        },
        {
            uri: './images/icons/arrow-down.svg',
            alias: 'arrow-down'
        },
        {
            uri: './images/icons/alert.svg',
            alias: 'alert'
        },
        {
            uri: './images/icons/account-key.svg',
            alias: 'account-key'
        },
        {
            uri: './images/icons/plus.svg',
            alias: 'plus'
        },
        {
            uri: './images/icons/bookmark-check.svg',
            alias: 'bookmark-check'
        },
        {
            uri:'./images/icons/folder-move.svg',
            alias:'folder-move'
        },
        {
            uri:'./images/icons/menu.svg',
            alias:'menu'
        },
        {
            uri:'./images/icons/view-carousel.svg',
            alias:'view-carousel'
        },
        {
            uri:'./images/icons/logout.svg',
            alias:'logout'
        },
        {
            uri:'./images/icons/library-plus.svg',
            alias:'library-plus'
        },
        {
            uri:'./images/icons/dice-2.svg',
            alias:'dice-2'
        },
        {
            uri: './images/icons/magnify.svg',
            alias: 'magnify'
        },
        {
            uri: './images/icons/chevron-left.svg',
            alias: 'chevron-left'
        },
        {
            uri: './images/icons/chevron-right.svg',
            alias: 'chevron-right'
        },
        {
            uri: './images/icons/chevron-up.svg',
            alias: 'chevron-up'
        },
        {
            uri: './images/icons/chevron-down.svg',
            alias: 'chevron-down'
        },
        {
            uri: './images/icons/close.svg',
            alias: 'close'
        },
        {
            uri: './images/icons/account-remove.svg',
            alias: 'account-remove'
        },
        {
            uri: './images/icons/check.svg',
            alias: 'check'
        },
        {
            uri: './images/icons/cloud-check.svg',
            alias: 'cloud-check'
        },
        {
            uri: './images/icons/delete.svg',
            alias: 'delete'
        },
        {
            uri: './images/icons/settings.svg',
            alias: 'settings'
        },
        {
            uri: './images/icons/database-outline.svg',
            alias: 'database-outline'
        },
        {
            uri: './images/icons/calendar-multiple-check.svg',
            alias: 'calendar-multiple-check'
        }
    ];

    function stompFunctionsProvider() {
        var socket;
        var stompClient;
        var parent = this;
        this.displayError = function (message) {
            //parent.toaster.pop('error', parent.localize.getLocalizedString('_error_'), message);
        };

        this.$get = ['$log', 'localize', '$rootScope', function ($log, localize, $rootScope) {
            return {
                connect: function () {
                    parent.localize = localize;
                    socket = new SockJS('/notify');
                    stompClient = Stomp.over(socket);
                    stompClient.connect({}, function (frame) {
                        $log.log('Stomp client connected.');
                        stompClient.subscribe('/topic/notify', function (greeting) {
                            var msg = JSON.parse(JSON.parse(greeting.body));
                            if (msg.status !== undefined) {
                                $log.log(msg.status);
                            } else if (msg.warning !== undefined) {
                                $log.warn(msg.warning);
                            } else if (msg.error !== undefined) {
                                $log.error(msg.error);
                            } else if (msg.event !== undefined) {
                                $rootScope.$broadcast('stompEvent', msg.event);
                            }
                        });
                    });
                },
                disconnect: function () {
                    $log.log('Stomp client DISCONNECTED.');
                    stompClient.disconnect();
                },
                sendStompMessage: function (message) {
                    stompClient.send("/topic/notify", {}, JSON.stringify({'message': message}));
                }
            }
        }];
    };

    function loggedInUserDetailsProvider() {
        this.$get = ['$log', '$stateParams', '$rootScope', 'localize', 'tmhDynamicLocale', '_',
            function ($log, $stateParams, $rootScope, localize, tmhDynamicLocale, _) {
                return {
                    obtainLoggedInUserDetails: function () {
                        $rootScope.serverSettings = serverSettings;
                        $rootScope.loggedInUser = loggedinUser;
                        $stateParams.language = $rootScope.loggedInUser.preferedLanguage;
                        if (localize.getLanguage() !== $rootScope.loggedInUser.preferedLanguage) {
                            localize.setLanguage($rootScope.loggedInUser.preferedLanguage);
                        }
                        if ($rootScope.loggedInUser.preferedLanguage === 'ro_RO') {
                            tmhDynamicLocale.set('ro');
                        }

                    }
                };
            }];
    }

    function loadMenuFileProvider() {
        var self = this;
        var deferredPromises = [];

        this.registerConfigCallback = function (deferredConfigPromise) {
            deferredPromises.push(deferredConfigPromise);
            return deferredConfigPromise.promise;
        }

        this.$get = ['$q', '$http', 'MENU_FILE', '$cookies', '$state',
            function ($q, $http, MENU_FILE, $cookies, $state) {
                if ($cookies.state) {
                    self.$state = $state;
                    self.savedState = $cookies.state;
                }
                $http({
                    method: "GET",
                    url: MENU_FILE,
                    cache: false
                }).success(function (data) {
                    angular.forEach(deferredPromises, function (deferredPromise) {
                        deferredPromise.resolve(data);
                    })
                }).error(function () {
                    console.error("Failed to find " + MENU_FILE);
                });
                return {
                    registerPromise: function () {
                        var d = $q.defer();
                        deferredPromises.push(d);
                        return d.promise;
                    },
                    changeStateToPrevious: self.changeStateToPrevious
                }
            }
        ];
        this.changeStateToPrevious = function () {
            if (self.savedState) {
                self.$state.go(self.savedState);
            }
        }
    };

    function config($mdThemingProvider,
                    $httpProvider,
                    $stateProvider,
                    $urlRouterProvider,
                    stompFunctionsProvider,
                    loadMenuFileProvider,
                    $locationProvider,
                    $mdIconProvider,
                    ICONS_URLS) {
        //Because stupid Jim Liu hasn't read documentation, disabling debug will break angular-ui-tree
        //$compileProvider.debugInfoEnabled(isDebug);
        //=====================================================================
        //HTTP
        //=====================================================================
        $httpProvider.defaults.headers.common['Content-Type'] = "application/json;charset=utf-8";
        //reduce the number of $apply calls
        $httpProvider.useApplyAsync(true);
        $httpProvider.interceptors.push(function ($q, $log, $rootScope) {
            return {
                'response': function (response) {
                    //detecting login page:
                    if (_.has(response, 'data') && typeof response.data === 'string') {
                        if (response.data.indexOf('<!DOCTYPE html>') != -1) {
                            //console.error("Have to go to login");
                            //yeap, it's the login page
                            window.location = "/login";
                        }
                    }
                    return response;
                },
                'responseError': function (rejection) {
                    console.log("===REJECTION===");
                    console.log(rejection);
                    console.log("===REJECTION===");
                    //TODO : re-enable rejection if needed
                    $rootScope.$broadcast('showAPICallError', rejection);
                    //stompFunctionsProvider.displayError(rejection.statusText);
                    return $q.reject(rejection);
                }
            };
        });
        //=====================================================================
        // Material design theme
        //=====================================================================
        $mdThemingProvider.theme('default').primaryPalette('cyan').accentPalette('blue');
        //=====================================================================
        // STATES
        //=====================================================================
        //The god damn hack - made in Heaven
        var initInjector = angular.injector(['ng']);
        //var $http = initInjector.get('$http');
        var $q = initInjector.get('$q');
        var deferredConfigPromise = $q.defer();

        //$urlRouterProvider.deferIntercept();

        loadMenuFileProvider.registerConfigCallback(deferredConfigPromise).then(function (data) {
            $locationProvider.html5Mode(true);
            //console.log("Configuring routes... ", $locationProvider.html5Mode());
            /**
             if (angular.isObject(isHtml5)) {
                isHtml5 = isHtml5.enabled;
            }
             **/
            angular.forEach(data, function (section) {
                var parentRouteData = {
                    name: section.route,
                    url: section.url,
                    controller: section.controller
                };
                if (section.abstract == "true") {
                    //console.log(section.url + " is abstract");
                    parentRouteData.abstract = true;
                    parentRouteData.template = '<ui-view />';
                } else {
                    parentRouteData.templateUrl = section.templateUrl;
                }
                $stateProvider.state(parentRouteData);
                angular.forEach(section.pages, function (page) {
                    var routeData = {
                        name: page.route,
                        url: page.url,
                        templateUrl: page.templateUrl,
                        controller: page.controller
                    };
                    if (parentRouteData.abstract) {
                        routeData.parent = parentRouteData;
                    }
                    $stateProvider.state(routeData);
                });
            });
            var routeData = {url: '/logout', templateUrl: '', controller: ''};
            $stateProvider.state('logout', routeData);
            var routeData = {
                url: '/profile',
                templateUrl: '/views/partials/user_profile.html',
                controller: 'userProfileController'
            };
            $stateProvider.state('profile', routeData);
            $urlRouterProvider.otherwise('/');
            loadMenuFileProvider.changeStateToPrevious();
        });
        //configure icons
        angular.forEach(ICONS_URLS, function (url) {
            $mdIconProvider.icon(url.alias, url.uri, 24);
        });
    }

    config.$inject = ['$mdThemingProvider',
        '$httpProvider',
        '$stateProvider',
        '$urlRouterProvider',
        'stompFunctionsProvider',
        'loadMenuFileProvider',
        '$locationProvider',
        '$mdIconProvider',
        'ICONS_URLS'
    ];

    function appRun($log,
                    $state,
                    $stateParams,
                    $rootScope,
                    localize,
                    UsersService,
                    loggedInUserDetailsProvider,
                    stompFunctionsProvider,
                    tmhDynamicLocale,
                    $cookies,
                    $http,
                    $mdToast,
                    $templateCache,
                    ICONS_URLS) {

        console.log('this is the right file.');

        angular.forEach(ICONS_URLS, function (url) {
            $http.get(url.uri, {cache: $templateCache});
        });

        //stompFunctionsProvider.connect();
        loggedInUserDetailsProvider.obtainLoggedInUserDetails();

        $rootScope.isDebug = isDebug;
        //To get access to localize in views.
        $rootScope.localize = localize;

        var unbind1 = $rootScope.$on('stompEvent', function (event, message) {
            $log.log("=====Stompevent=====");
            $log.log(message);
            $log.log("=====Stompevent=====");

        });

        $rootScope.$on('showAPICallError', function (event, error) {
            var errorToast = {
                template: '<md-toast class="md-toast error">' + (error.errorMessage ? localize.getLocalizedString(error.errorMessage) : error.data.message) + '</md-toast>',
                hideDelay: 3000,
                position: 'bottom fit'
            }
            $mdToast.show(errorToast);
        });

        $rootScope.switchLanguage = function () {
            if (localize.getLanguage() === "ro_RO") {
                UsersService.setLanguageForLoggedInUser('en_US');
                localize.setLanguage('en_US');
                tmhDynamicLocale.set('en');
            } else {
                UsersService.setLanguageForLoggedInUser('ro_RO');
                localize.setLanguage('ro_RO');
                tmhDynamicLocale.set('ro');
            }
        };
        $rootScope.$on('$stateChangeSuccess', function (event, toState, toParams, fromState, fromParams) {
            //$log.info("State was changed : " , toState);
        });
        $rootScope.$on('$stateChangeError', console.log.bind(console));
        $rootScope.$on('$stateNotFound', function (event, unfoundState, fromState, fromParams) {
            $log.error("State not found : " + unfoundState);
        });
        $rootScope.$on('$stateChangeStart', function (event, toState, toParams) {
            //$log.log("State change start ", toState);
            if (toState.url == "/logout") {
                UsersService.logout();
            } else {
                $rootScope.state = toState;
                $cookies.state = toState.name;
                toParams.language = localize.getLanguage();
            }
        });
        $rootScope.$on('$viewContentLoading', function (event, viewConfig) {
            //$log.log("View loading");
        });
    }

    appRun.$inject = [
        '$log',
        '$state',
        '$stateParams',
        '$rootScope',
        'localize',
        'UsersService',
        'loggedInUserDetails',
        'stompFunctions',
        'tmhDynamicLocale',
        '$cookies',
        '$http',
        '$mdToast',
        '$templateCache',
        'ICONS_URLS'
    ];
    /**
     * TODO : remove this in production and copy an application of each role
     * so we can hide functionalities exposed through menu
     * Temporary
     *
     *
     */
    try {
        if (!loggedinUser) {
            return;
        }
    } catch (ex) {
        window.location = "/login";
    }
    //defaults for client
    var localeFiles = ["/js/i18n/resources_locales.json"];
    var menuFile = "/js/i18n/customer_menu_locale.json";
    var isAgency = true;
    if (_.findWhere(loggedinUser.authorities, {authority: 'ROLE_PHOTOGRAPHER'}) === undefined) {
        isAgency = false;
    }
    if (isAgency) {
        //menu changes if it's agency
        menuFile = "/js/i18n/agency_menu_locale.json";
    }
    var isSuperAdmin = true;
    if (_.findWhere(loggedinUser.authorities, {authority: 'ROLE_SUPER_ADMIN'}) === undefined) {
        isSuperAdmin = false;
    }
    if (isSuperAdmin) {
        //menu and locales changes if super admin
        localeFiles = ["/js/i18n/resources_locales.json", "/js/i18n/admin_locales.json"];
        menuFile = "/js/i18n/admin_menu_locale.json";
    }
    /**
     * End of temporary
     */

    angular.module('photoX', [
        'ngCookies',
        'ngMaterial',
        'ngSanitize',
        'localization',
        'dropzone.module',
        'input-directives',
        'tmh.dynamicLocale',
        'ng-panel',
        'underscore',
        'menu',
        'keyman',
        'ui.tree',
        'commons',
        'ui.router',
        'calendar',
        'mdDatePicker',
        'mdTable',
        'pagination',
        'progressbar',
        'toggle-switch',
        'dynamic-checkboxes'
        ])
        .constant("APP_URL", "/private/API/v1")
        .constant("LOCALE_FILES", localeFiles)
        .constant("MENU_FILE", menuFile)
        .constant("ICONS_URLS", iconsUrls)
        .provider('loggedInUserDetails', loggedInUserDetailsProvider)
        .provider('stompFunctions', stompFunctionsProvider)
        .provider('loadMenuFile', loadMenuFileProvider)
        .config(config)
        .run(appRun);
})();
