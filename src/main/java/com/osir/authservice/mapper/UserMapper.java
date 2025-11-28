package com.osir.authservice.mapper;

import com.osir.takeoutpojo.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;

@Mapper
public interface UserMapper {


    /**
     * 根据openid查询用户
     * @param openid
     * @return
     */
    @Select("select * from user where openid=#{openid}")
    User getByOpenid(String openid);

    /**
     * 插入用户
     * @param user
     */
    void insert(User user);

    /**
     * 根据id查询用户
     * @param userId
     * @return
     */
    @Select("select * from user where id=#{userId}")
    User getById(Long userId);

    /**
     * 统计用户总数
     * @return
     */
    @Select("select count(*) from user where create_time <= #{time}")
    Integer getUserTotal(LocalDateTime time);

    /**
     * 统计新增用户
     * @param begin
     * @param end
     * @return
     */
    @Select("select count(*) from user where create_time between #{begin} and #{end}")
    Integer getNewUser(LocalDateTime begin,LocalDateTime end);
}
