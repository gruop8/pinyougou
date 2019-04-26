/** 定义控制器层 */
app.controller('IndexController', function($scope, baseService){
    /** 定义获取登录用户名方法 */
    $scope.showName = function(){
        baseService.sendGet("/user/showName")
            .then(function(response){
                $scope.loginName = response.data.loginName;
            });
    };


    $scope.page = '1';
    $scope.rows = '2';
    // 搜索方法
    $scope.search = function () {
        // 发送异步请求
        baseService.sendGet("/Search?page=" + $scope.page + "&rows=" + $scope.rows).then(function (response) {
            // 获取响应数据 response.data: {total : 100, rows : [{},{}]}
            $scope.totalPages = response.data[0].totalPages;
            $scope.orders = response.data[1].orders;
            // 调用生成页码的方法
            initPageNums();
        });
    };

    var initPageNums = function () {
        // pageNums
        $scope.pageNums = [];
        // 开始页码
        var firstPage = 1;
        // 结束页码
        var lastPage = $scope.totalPages;

        // 前面加点
        $scope.firstDot = true;
        // 后面加点
        $scope.lastDot = true;

        // 判断总页数是不是大于5
        if ($scope.totalPages > 5){

            // 判断当前页码是否靠前面近些
            if ($scope.page <= 3){ // 前面
                lastPage = 5;
                $scope.firstDot = false;
            }else if($scope.page >= $scope.totalPages - 3){ // 后面
                // 判断当前页码是否靠后面近些
                firstPage = $scope.totalPages - 4;
                $scope.lastDot = false;
            }else{ // 中间
                firstPage = $scope.page - 2;
                lastPage = $scope.page + 2;
            }
        }else {
            $scope.firstDot = false;
            $scope.lastDot = false;
        }

        // 循环生成5个页码
        for (var i = firstPage; i <= lastPage; i++){
            $scope.pageNums.push(i);
        }
    };


    // 根据页码查询
    $scope.pageSearch = function (page) {
        page = parseInt(page);
        // 判断页码的有效性: 不能小于1、不能大于总页数、不能等于当前页码
        if (page >= 1 && page <= $scope.totalPages
            && page != $scope.page){
            // 当前页码
            $scope.page = page;
            // 跳转的页码
            $scope.jumpPage = page;
            // 执行搜索
            $scope.search();
        }
    };


});