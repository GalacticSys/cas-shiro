package com.example.casshiro.dao;

import com.example.casshiro.entity.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
@Mapper
public interface RoleDao {
    @Select("select user_role.id as id,role.name as name\n" +
            "        from role\n" +
            "        inner join user_role on user_role.id=role.id\n" +
            "        where user_role.uid=#{stuId}")
    public List<Role> findRoleByUserId(@Param("stuId") Integer stuId);
}
