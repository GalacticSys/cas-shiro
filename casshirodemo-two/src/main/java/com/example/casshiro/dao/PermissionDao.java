package com.example.casshiro.dao;


import com.example.casshiro.entity.Permission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
@Mapper
public interface PermissionDao {
    @Select("select permission.id as id,permission.name as name\n" +
            "        from permission\n" +
            "        inner join role_permission on role_permission.pid=permission.id\n" +
            "        where role_permission.rid=#{roleId}")
    public List<Permission> findPermissionByRoleId(@Param("roleId") Integer roleId);
}
