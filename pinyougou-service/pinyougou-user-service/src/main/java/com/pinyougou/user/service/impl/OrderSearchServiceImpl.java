package com.pinyougou.user.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.ISelect;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pinyougou.common.pojo.PageResult;
import com.pinyougou.mapper.OrderItemMapper;
import com.pinyougou.mapper.OrderMapper;
import com.pinyougou.pojo.Order;
import com.pinyougou.pojo.OrderItem;
import com.pinyougou.service.OrderSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceName = "com.pinyougou.service.OrderSearchService")
@Transactional
public class OrderSearchServiceImpl implements OrderSearchService{
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderItemMapper orderItemMapper;
    @Override
    public List<Map<String,Object>> search(Integer page, Integer rows, String userId) {
        List<Map<String,Object>> resultMap = new ArrayList<>();

        PageInfo<Map<String,Object>> pageInfo = PageHelper.startPage(page, rows).doSelectPageInfo(new ISelect() {
            @Override
            public void doSelect() {
                List<Order> orderList = orderMapper.selectByUserId(userId);
                for (Order order : orderList) {
                    List<OrderItem> orderItemList = orderItemMapper.selectByOrderId(order.getOrderId());
                    order.setOrderItemList(orderItemList);
                }
                orderMapper.selectAll();
            }
        });
        Map<String,Object> pages = new HashMap<>();
        pages.put("totalPages",pageInfo.getPages());
        Map<String,Object> orders = new HashMap<>();
        orders.put("orders",pageInfo.getList());
        resultMap.add(pages);
        resultMap.add(orders);
        return resultMap;
    }
}
