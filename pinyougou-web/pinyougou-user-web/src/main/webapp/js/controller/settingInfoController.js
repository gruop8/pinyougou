/** 个人信息控制器 */
app.controller('settingInfoController', function($scope, baseService){

    $scope.user = {};
    $scope.address = {};
    /** 定义保存格式 */
    $scope.user = {name : "", sex : "", birthday : "", address : {provinceId : "", cityId : "", townId : ""}};

    /** 定义获取登录用户名方法 */
    $scope.showName = function(){
        baseService.sendGet("/user/showName")
            .then(function(response){
                $scope.loginName = response.data.loginName;
            });
    };



    /** 加载完后就立刻加载所有省 */
    $scope.findProvinces = function () {
        $scope.user = {};
        baseService.sendGet("user/findProvinces").then(function (response) {
            $scope.province1 = response.data;
        });
    };

    $scope.$watch('user.address.provinceId',function (newValue, oldValue) {
        if(newValue){
            $scope.findCitiesByProvinceId(newValue);
        }else {
            $scope.cities1 = [];
        }
    });

    $scope.findCitiesByProvinceId = function(parentId){
        baseService.sendGet("/user/findCities",
            "parentId=" + parentId).then(function(response){
            $scope.cities1 = response.data;
        });
    };

    $scope.$watch('user.address.cityId',function (newValue) {
        if(newValue){
            $scope.findAreasBiCityId(newValue);
        }else {
            $scope.cities1 = [];
        }
    });

    $scope.findAreasBiCityId = function(parentId){
        baseService.sendGet("/user/findAreas",
            "parentId=" + parentId).then(function(response){
            $scope.areas1 = response.data;
        });
    };



    /** 保存信息 */
    $scope.update = function () {
        baseService.sendPost("/user/userUpdate", $scope.user).then(function (response) {
            if (response.data){
                alert("修改成功");
            }else {
                alert("修改失败");
            }
        });
    };

    /** 图片上传 */
    $scope.upload = function () {
        baseService.uploadFile().then(function (response) {
            if(response.data.status == 200){

                $scope.picEntity.url = response.data.url;

            }
            else {
                alert("上传失败");
            }
        });
    };

});