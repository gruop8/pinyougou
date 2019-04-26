// 定义购物车的控制器
app.controller('cartController', function ($scope, $controller, baseService) {

    // 继承baseController
    $controller('baseController', {$scope : $scope});

    // 查询购物车
    $scope.findCart = function () {
        baseService.sendGet("/cart/findCart").then(function(response){
            // 获取响应数据
            $scope.carts = response.data;

            // 定义json对象封装统计的数据
            $scope.totalEntity = {totalNum : 0, totalMoney : 0};
            // 迭代用户的购物车集合
            for (var i = 0; i < $scope.carts.length; i++){
                // 获取商家的购物车
                var cart = $scope.carts[i];
                // 迭代商家购物车中的商品
                for (var j = 0; j < cart.orderItems.length; j++){
                    // 获取购买的商品
                    var orderItem = cart.orderItems[j];

                    // 统计购买数量
                    $scope.totalEntity.totalNum += orderItem.num;
                    // 统计总金额
                    $scope.totalEntity.totalMoney += orderItem.totalFee;

                }
            }
        });
    };

    // 添加商品到购物车
    $scope.addCart = function (itemId, num) {
        baseService.sendGet("/cart/addCart?itemId="
            + itemId + "&num=" + num).then(function(response){
            // 获取响应数据
            if (response.data){
                // 重新查询购物车
                $scope.findCart();
            }
        });
    };



    //选择全部结算的商品
    $scope.selectAll = function () {
        if ($scope.allCheck) {
            angular.forEach($scope.carts, function (cart) {
                cart.checked = true;
                angular.forEach(cart.orderItems, function (innerItem) {
                    innerItem.checked = true;
                });
            });
            $scope.select.selectNum = $scope.totalEntity.totalNum;
            $scope.select.selectMoney = $scope.totalEntity.totalMoney;
        } else {
            angular.forEach($scope.carts, function (cart) {
                cart.checked = false;
                angular.forEach(cart.orderItems, function (innerItem) {
                    innerItem.checked = false;
                })
            });
            $scope.select.selectNum = 0;
            $scope.select.selectMoney = 0;
        }
    };

    //选择一个商家的商品
    $scope.selectCarts = function (cart, carts) {
        if (cart.checked) {

            for(var i = 0; i < cart.orderItems.length; i++){
                $scope.select.selectNum += cart.orderItems[i].num;
                $scope.select.selectMoney += cart.orderItems[i].totalFee;
            }

            var flag = true;
            angular.forEach(cart.orderItems, function (innerItem) {
                innerItem.checked = true;
            });
            angular.forEach(carts, function (outerItem) {
                if (outerItem.checked == false || typeof(outerItem.checked) == "undefined") {
                    flag = false;
                }
            });

            if(flag)
            {
                $scope.allCheck = true;
            }
        } else {
            for(var i = 0; i < cart.orderItems.length; i++){
                $scope.select.selectNum -= cart.orderItems[i].num;
                $scope.select.selectMoney -= cart.orderItems[i].totalFee;
            }

            $scope.allCheck = false;
            angular.forEach(cart.orderItems, function (innerItem) {
                innerItem.checked = false;
            });

        }
    };


    //选中商品总金额
    $scope.select = {selectNum : 0, selectMoney : 0};

    //选中一个商品
    $scope.selectOne = function ( orderItem, orderCart, cart, carts ) {

        var flag = true;
        if (orderItem.checked) {
            // 统计购买数量
            $scope.select.selectNum += orderItem.num;
            // 统计总金额
            $scope.select.selectMoney += orderItem.totalFee;
            angular.forEach(orderCart, function (innerItem) {
                if (innerItem.checked == false || typeof(innerItem.checked) == "undefined") {
                    flag = false;
                }
            });
        }
        else {
            $scope.select.selectNum -= orderItem.num;
            $scope.select.selectMoney -= orderItem.totalFee;
            $scope.allCheck = false;
            cart.checked = false;
            flag = false;
        }
        if (flag) {
            cart.checked = true;
            angular.forEach(carts, function (outerItem) {
                if (outerItem.checked == false || typeof(outerItem.checked) == "undefined") {
                    flag = false;
                }
            });
        }
        if (flag) {
            $scope.allCheck = true;
        }
    };
});