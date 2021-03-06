package com.pinyougou.user.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.*;
import com.pinyougou.service.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户控制器
 *
 * @author lee.siu.wah
 * @version 1.0
 * <p>File Created at 2019-04-15<p>
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Reference(timeout = 10000)
    private UserService userService;
    @Reference(timeout = 10000)
    private AddressService addressService;
    @Reference(timeout = 10000)
    private ProvincesService provincesService;
    @Reference(timeout = 10000)
    private CitiesService citiesService;
    @Reference(timeout = 10000)
    private AreasService areasService;

    /**
     * 用户注册
     */
    @PostMapping("/save")
    public boolean save(@RequestBody User user, String code) {
        try {
            // 检验验证码是否正确
            boolean flag = userService.checkSmsCode(user.getPhone(), code);
            if (flag) {
                userService.save(user);
            }
            return flag;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }


    /**
     * 发送短信验证码
     */
    @GetMapping("/sendSmsCode")
    public boolean sendSmsCode(String phone) {
        try {
            return userService.sendSmsCode(phone);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @GetMapping("/showAddress")
    public List<Address> showAddress(String userId) {
        try {
            return addressService.findAddressByUser(userId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @GetMapping("/findCity")
    public Map<String, Object> findCityByParentId(String parentId) {
        //所有省
        List<Provinces> provincesList = provincesService.findAll();
        //所有市
        List<Cities> citiesList = citiesService.findAll();
        //所有区
        List<Areas> areasList = areasService.findAll();
        Map<String, Object> map = new HashMap<>();
        map.put("provinces", provincesList);
        map.put("cities", citiesList);
        map.put("areas", areasList);
        return map;
    }

    @GetMapping("/findProvinces")
    public Map<String, Object> findProvinces() {
        //所有省
        try {
            List<Provinces> provincesList = provincesService.findAll();
            Map<String, Object> map = new HashMap<>();
            map.put("provinces", provincesList);
            return map;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @GetMapping("/findCities")
    public Map<String, Object> findCitiesByProvinceId(String parentId) {
        Provinces provinces = provincesService.findByProvince(parentId);
        //所有市
        try {
            List<Cities> citiesList = citiesService.findCitiesByProvinceId(provinces.getProvinceId());
            Map<String, Object> map = new HashMap<>();
            map.put("cities", citiesList);
            return map;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @GetMapping("/findAreas")
    public Map<String, Object> findAreasByCityId(String parentId) {
        Cities cities = citiesService.findByCityId(parentId);
        try {
            //所有区
            List<Areas> areasList = areasService.findAreasByCitiyId(cities.getCityId());
            Map<String, Object> map = new HashMap<>();
            map.put("areas", areasList);
            return map;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @PostMapping("/saveAddress")
    public boolean saveAddress(@RequestBody Address address) {
        try {
            String userId = SecurityContextHolder.getContext()
                    .getAuthentication().getName();
            address.setUserId(userId);
            addressService.save(address);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @PostMapping("/delete")
    public boolean delete(Long id){
        try {
            addressService.delete(id);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @PostMapping("/updateIsDefault")
    public boolean updateIsDefault(Long id){
        try {
            addressService.updateIsDefault(id);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /** 显示个人信息 */
    @GetMapping("/findInfo")
    public User findInfo(HttpServletRequest request) {
        String userId = request.getRemoteUser();
        return userService.findInfoByUserId(userId);
    }

    /** 用户信息修改 */
    @PostMapping("/userUpdate")
    public boolean userUpdate(@RequestBody User user, HttpServletRequest request){
        try {
            String username = request.getRemoteUser();
            user.setBirthday(new Date());
            user.setUsername(username);
            userService.userUpdate(user);
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    @GetMapping("/findAddress")
    public Address findAddressById(Long id){
        return addressService.findOne(id);
    }

    @PostMapping("/update")
    public boolean update(@RequestBody Address address){
        try {
            addressService.update(address);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }



    /** 修改昵称和密码 */
    @PostMapping("/updateUser")
    public boolean updateUser(String username, String password){
        try {
            String name = SecurityContextHolder.getContext().getAuthentication().getName();
            userService.updateUser(username, password, name);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /** 判断验证码 */
    @PostMapping("/affirmCode")
    public boolean affirmCode(String code, HttpServletRequest request){
        try {
            String oldCode = (String)request.getSession()
                    .getAttribute(VerifyController.VERIFY_CODE);
            System.out.println("oldCode = " + oldCode);
            if (code.equalsIgnoreCase(oldCode)){
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /** 查询电话号码 */
    @GetMapping("/findPhone")
    public String findPhone(){
        try {
            String name = SecurityContextHolder.getContext().getAuthentication().getName();
            return userService.findPhone(name);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /** 判断短信验证码 */
    @PostMapping("/messageCode")
    public boolean messageCode(String phone, String code){
        try {
            boolean flag = userService.checkSmsCode(phone, code);
            return flag;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /** 修改电话号码 */
    @PostMapping("/updatePhone")
    public boolean updatePhone(String phone){
        try {
            String name = SecurityContextHolder.getContext().getAuthentication().getName();
            userService.updatePhone(name, phone);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
