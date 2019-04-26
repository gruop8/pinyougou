package com.pinyougou.shop.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.Seller;
import com.pinyougou.service.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 商家控制器
 *
 * @author lee.siu.wah
 * @version 1.0
 * <p>File Created at 2019-04-01<p>
 */
@RestController
@RequestMapping("/seller")
public class SellerController {

    @Reference(timeout = 10000)
    private SellerService sellerService;
    @Autowired
    private PasswordEncoder passwordEncoder;


    /** 商家申请入驻 */
    @PostMapping("/save")
    public boolean save(@RequestBody Seller seller){
        try{
            String password = passwordEncoder.encode(seller.getPassword());
            seller.setPassword(password);
            sellerService.save(seller);
            return true;
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return false;
    }

    /** 查询原密码 */
    @PostMapping("/findOldPassword")
    public boolean findOldPassword(String oldPassword){
        try {
            // 获取安全上下文对象
            SecurityContext context = SecurityContextHolder.getContext();
            // 获取用户名
            String sellerId = context.getAuthentication().getName();
            System.out.println("loginName = " + sellerId);
            //查询数据库加密的原密码
            String password = sellerService.findOldPassword(sellerId);
            //比较原密码
            boolean matches = passwordEncoder.matches(oldPassword, password);
            return matches;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /** 修改密码 */
    @PostMapping("/updatePassword")
    public boolean updatePassword(String newPassword){
        try {
            // 获取安全上下文对象
            SecurityContext context = SecurityContextHolder.getContext();
            // 获取用户名
            String sellerId = context.getAuthentication().getName();
            //根据用户id修改密码
            String password = passwordEncoder.encode(newPassword);
            sellerService.updatePassword(sellerId, password);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    /**
     * 修改商家的资料（message）
     */
    @GetMapping("/message")
    public List<Seller> message(Seller seller) {
        //获取登录用户名
        String sellerId = SecurityContextHolder.getContext()
                .getAuthentication().getName();

        //设置用户的id
        seller.setSellerId(sellerId);
        //根据用户的id查询查询商家
        return sellerService.findSeller(sellerId);
    }
    /**
     * 修改商家信息
     */
    @PostMapping("/updatemessage")
    public boolean updatemessage(@RequestBody Seller seller){
        try{
            sellerService.updateMessage(seller);
            return true;
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return false;
    }

}
