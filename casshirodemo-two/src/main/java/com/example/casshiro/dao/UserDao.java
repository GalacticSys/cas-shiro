package com.example.casshiro.dao;

import com.example.casshiro.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 用户登录
 * @author 胡江涛
 *
 */
@Mapper
public interface UserDao {
	//根据学生ID查询用户
	@Select("SELECT id,username,password FROM user WHERE username = #{name}")
	public User findUserByName (@Param("name") String name);

}
