(function () {

    "use strict";

    angular.module('photoX').controller('clientsListController', clientsListController);

    clientsListController.$inject = ['$scope', 'localize', 'UsersService', '$log', '$mdDialog', '$state', 'commons'];

    function clientsListController($scope, localize, UsersService, $log, $mdDialog, $state, commons) {

        $scope.goToClientDefinition = function () {
            $state.go("clients.add");
        };

        var recurseCreateFakeNodes = function(collection){
            collection.forEach(function(user){
                if (user.referees.length == 0){
                    commons.log('making referee for user '+user.username);
                    user.referees.push({
                        username : 'Drop user above! Attention : you can\'t drag this...',
                        accountType : 'none'
                    });
                }else{
                    recurseCreateFakeNodes(user.referees);
                }
            })
        };

        var postDeleteUser = function(scope){
            scope.remove();
            commons.log("Removed ", scope.$nodeScope.$modelValue.username);
            commons.displayLocalizedToast('_Success_', 'success');
        };

        var postUserMoved = function(serverResponse){
            commons.log(serverResponse);
            commons.displayLocalizedToast('_Success_', 'success');
        };

        var postLoadingRelations = function(serverResponse){
            //this is temporary, so we can move nodes arround...
            //TODO : remove drag and drop ability when going to production
            recurseCreateFakeNodes(serverResponse);
            $scope.users = serverResponse;
            $scope.treeCallbacks = {
                dropped: function (event) {
                    var source = event.source.nodeScope;
                    var sourceData = source.$modelValue;
                    var parent = event.dest.nodesScope;
                    if (!parent.$nodeScope){
                        commons.log('dropped, but it is probably same place or something');
                        commons.log(parent);
                        angular.copy($scope.backupData, $scope.users);
                        return;
                    }
                    var targetData = parent.$nodeScope.$modelValue;
                    //$log.log("Source : " , sourceData);
                    //$log.log("Parent : ", targetData);
                    var confirm = $mdDialog.confirm()
                        .parent(angular.element(document.body))
                        .title(localize.getLocalizedString('_MoveQuestion_', sourceData.username, targetData.username))
                        .ariaLabel('Move')
                        .ok(localize.getLocalizedString('_Yes_'))
                        .cancel(localize.getLocalizedString('_No_')).
                        targetEvent(event);
                    $mdDialog.show(confirm).then(function () {
                        UsersService.setParentOfUser(sourceData.id , targetData.id).then(postUserMoved);
                    }, function () {
                        angular.copy($scope.backupData, $scope.users);
                    });
                },
                beforeDrag: function (source) {
                    //console.log('before drag '+source.$modelValue.username);
                    angular.copy($scope.users, $scope.backupData);
                    return true;
                }
            };
            $scope.loading = false;
        };

        $scope.init = function () {
            $scope.loading = true;
            UsersService.getRefereeList().then(postLoadingRelations);
        };

        $scope.askQuestionThenRemove = function (event, scope) {
            var user = scope.$nodeScope.$modelValue;
            var confirm = $mdDialog.confirm()
                .parent(angular.element(document.body))
                .title(localize.getLocalizedString('_DeleteQuestion_', user.username, ''))
                .ariaLabel('Delete')
                .ok(localize.getLocalizedString('_Yes_'))
                .cancel(localize.getLocalizedString('_No_')).
                targetEvent(event);
            $mdDialog.show(confirm).then(function () {
                UsersService.deleteUser(user).then(postDeleteUser(scope));
            }, function () {
                commons.log("NOT Removing");
            });
        };

        $scope.toggleOpenNode = function (scope) {
            scope.toggle();
        };

        $scope.editAccount = function(event, scope){
            var user = scope.$nodeScope.$modelValue;
            //commons.log("should edit -> "+user.username);
            $state.go('clients.edit',{userId : user.id});
        };

        $scope.backupData = [];

        $scope.init();
    }
}).call(this);