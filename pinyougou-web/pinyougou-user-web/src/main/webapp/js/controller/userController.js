/** 定义控制器层 */
app.controller('userController', function($scope, $timeout, baseService){

    // 定义user对象
    $scope.user = {};
    $scope.address = {};

    $scope.showName = function(){
        baseService.sendGet("/user/showName")
            .then(function(response){
                $scope.loginName = response.data.loginName;
            });
    };
    /** 用户注册 */
    $scope.save = function () {

        // 判断两次密码是否一致
        if ($scope.okPassword && $scope.user.password == $scope.okPassword){
            // 发送异步请求
            baseService.sendPost("/user/save?code=" + $scope.code, $scope.user)
                .then(function(response){
                if (response.data){
                    // 清空表单数据
                    $scope.user = {};
                    $scope.okPassword = "";
                    $scope.code = "";
                }else{
                    alert("注册失败！");
                }
            });

        }else{
            alert("两次密码不一致！");
        }
    };




    // 发送短信验证码
    $scope.sendSmsCode = function () {

        // 判断手机号码
        if ($scope.user.phone && /^1[3|4|5|7|8]\d{9}$/.test($scope.user.phone)){
            // 发送异步请求
            baseService.sendGet("/user/sendSmsCode?phone=" + $scope.user.phone)
                .then(function(response){
                if (response.data){
                    // 调用倒计时方法
                    $scope.downcount(90);

                }else{
                    alert("发送失败！");
                }
            });
        }else {
            alert("手机号码格式不正确！")
        }
    };


    $scope.smsTip = "获取短信验证码";
    $scope.disabled = false;

    // 倒计时方法
    $scope.downcount = function (seconds) {

        seconds--;

        if (seconds >= 0){
            $scope.smsTip = seconds + "秒后，重新获取！";
            $scope.disabled = true;
            // 第一个参数：回调的函数
            // 第二个参数：间隔的时间毫秒数
            $timeout(function(){
                $scope.downcount(seconds);
            }, 1000);
        }else {
            $scope.smsTip = "获取短信验证码";
            $scope.disabled = false;
        }

    };

    $scope.showAddress = function () {
        baseService.sendGet("user/showAddress?userId = " + 'itcast').then(function (response) {
            $scope.dataList = response.data;
        })
    };

    $scope.findProvinces = function () {
        $scope.address = {};
        baseService.sendGet("user/findProvinces").then(function (response) {
            $scope.province1 = response.data;
        })
    };


    $scope.$watch('address.provinceId',function (newValue, oldValue) {
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

    $scope.$watch('address.cityId',function (newValue) {
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

    $scope.saveOrUpdate = function () {
        // 请求参数
        //alert(JSON.stringify($scope.entity));
        var url = "saveAddress"; // 添加URL
        // 判断id是否存在
        if ($scope.address.id){
            url = "update"; // 修改URL
        }

        baseService.sendPost("user/" + url,$scope.address).then(function (response) {
            if(response.data){
                location.reload()
            }else {
                alert("添加失败")
            }
        })
    };

    $scope.delete = function (id) {
        baseService.sendPost("/user/delete?id=" + id).then(function (response) {
            if(response.data){
                alert("删除成功");
                location.reload()
            }else {
                alert("删除失败")
            }
        })
    };

    $scope.updateIsDefault = function (id) {
        baseService.sendPost("/user/updateIsDefault?id=" + id ).then(function (response) {
            if(response.data){
                location.reload()
            }else {
                alert("修改失败。")
            }
        });
    }

    $scope.findAddress = function (id) {
        baseService.sendGet("/user/findAddress?id="+ id).then(function (response) {
            $scope.address = response.data;
        })
    }



    //修改密码
    $scope.updateUser = function () {
        //判断密码是否一致
        if ($scope.qrPassword && $scope.upPassword == $scope.qrPassword){
            baseService.sendPost("/user/updateUser?username=" + $scope.username + "&password=" + $scope.upPassword).then(function (response) {
                if(response.data){
                    location.href = "http://www.pinyougou.com";
                }else {
                    alert("验证码错误");
                }
            });
        }else {
            alert("密码不一致");
        }
    };

    //查询电话号码
    $scope.findPhone = function () {
        baseService.sendGet("/user/findPhone").then(function (response) {
            $scope.phoneNum = response.data;

        });
    };

    //判断验证码
    $scope.affirmCode = function (code, phone) {
        baseService.sendPost("/user/affirmCode?code=" + code).then(function (response) {
            if(response.data){
                $scope.sendCode(phone);
            }else {
                alert("验证码错误");
                window.location.reload(true);
            }
        });
    };

    //发送短信验证码
    $scope.sendCode = function (phone) {
        //发送短信验证码
        baseService.sendGet("/user/sendSmsCode?phone=" + phone).then(function (response) {
            if (response.data){
                // 调用倒计时方法
                $scope.downcount(90);

            }else{
                alert("发送失败！");
                window.location.reload(true);
            }
        });
    };

    //判断短信验证码
    $scope.messageCode = function (phone, code) {
        baseService.sendPost("/user/messageCode?phone=" + phone + "&code=" + code).then(function (response) {
            if(response.data){
                alert("验证码正确");
            }else {
                alert("验证码错误！");
                window.location.reload(true);
            }
        });
    };

    //修改电话号码
    $scope.updatePhone = function () {
        baseService.sendPost("/user/updatePhone?phone=" + $scope.newPhone).then(function (response) {
            if(!response.data){
                alert("修改失败");
                window.location.reload(true);
            }
        });
    };
});