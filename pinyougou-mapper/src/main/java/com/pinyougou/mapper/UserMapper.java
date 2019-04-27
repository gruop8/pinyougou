package com.pinyougou.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import tk.mybatis.mapper.common.Mapper;

import com.pinyougou.pojo.User;

/**
 * UserMapper 数据访问接口
 * @date 2019-03-28 09:54:28
 * @version 1.0
 */
public interface UserMapper extends Mapper<User>{

    /**  修改密码和名称 */
    @Update("update tb_user set username=#{username},password=#{password} where username=#{name}")
    void updateUser(@Param("username") String username, @Param("password") String password, @Param("name") String name);

    /** 查询电话号码 */
    @Select("select phone from tb_user where username=#{name}")
    String findPhone(String name);

    /** 修改电话号码 */
    @Update("update tb_user set phone=#{phone} where username=#{name}")
    void updatePhone(@Param("name") String name, @Param("phone") String phone);


    @Select("select nick_name,sex,birthday,address,head_pic from tb_user where username = #{username}")
    User findByUserId(String username);

    void updateByUsername(User user);
}