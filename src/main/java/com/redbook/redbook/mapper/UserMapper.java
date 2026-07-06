package com.redbook.redbook.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.redbook.redbook.entiy.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    @Select("SELECT id, username, avatar, phone, password, gender, bio, create_time, update_time FROM user WHERE phone = #{phone}")
    User selectByPhone(@Param("phone") String phone);
}