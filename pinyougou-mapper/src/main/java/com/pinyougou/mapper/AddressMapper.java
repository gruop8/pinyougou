package com.pinyougou.mapper;

import org.apache.ibatis.annotations.Update;
import tk.mybatis.mapper.common.Mapper;

import com.pinyougou.pojo.Address;

/**
 * AddressMapper 数据访问接口
 * @date 2019-03-28 09:54:28
 * @version 1.0
 */
public interface AddressMapper extends Mapper<Address>{

    @Update("UPDATE tb_address set is_default = '1' where id = #{id}")
    void updateIsDefaultById(Long id);

    @Update("UPDATE tb_address SET is_default = '0' where is_default = '1'")
    void updateIsDefault();
}