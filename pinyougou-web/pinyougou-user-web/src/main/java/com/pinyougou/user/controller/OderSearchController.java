package com.pinyougou.user.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.service.OrderSearchService;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class OderSearchController {

    @Reference(timeout = 10000)
    private OrderSearchService orderSearchService;

    @GetMapping("/Search")
    public List<Map<String,Object>> search(Integer page, Integer rows){
        // 获取安全上下文对象
        SecurityContext context = SecurityContextHolder.getContext();
        String userId = context.getAuthentication().getName();

        return orderSearchService.search(page,rows,userId);
}
}
