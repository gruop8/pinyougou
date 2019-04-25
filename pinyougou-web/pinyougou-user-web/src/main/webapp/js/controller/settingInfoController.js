/** 个人信息控制器 */
app.controller('settingInfoController', function($scope, baseService){


    $scope.update = function () {
        baseService.sendGet("/info/update", $scope.user).then(
            function (response) {
                $scope.
        });
    }

});