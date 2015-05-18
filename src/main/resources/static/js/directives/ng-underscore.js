(function (window) {
    "use strict";
    var underscore = angular.module('underscore', []);
    underscore.factory('_', function () {
        _.replaceInCollection = function(collection, item, newItem){
            var existingItem = _.findWhere(collection, {id: item.id});
            var keys = _.keys(existingItem);
            _.each(keys, function(key){
                existingItem[key] = newItem[key];
            });
        }
        return window._; //Underscore must already be loaded on the page
    });
}(window) );
