package com.example.casshiro.shiro;

import com.example.casshiro.dao.PermissionDao;
import com.example.casshiro.dao.RoleDao;
import com.example.casshiro.dao.UserDao;
import com.example.casshiro.entity.Permission;
import com.example.casshiro.entity.Role;
import com.example.casshiro.entity.User;
import com.example.casshiro.entity.UserActive;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

public class MyRealm extends AuthorizingRealm {
    @Resource
    UserDao userDao;
    @Resource
    RoleDao roleDao;
    @Resource
    PermissionDao permissionDao;

    //登录验证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken)authenticationToken;
        String userName = usernamePasswordToken.getUsername();
        String password = new String((char[]) usernamePasswordToken.getCredentials());
        User user = userDao.findUserByName(userName);
         if (user!=null){
             if (user.getPassword().equals(password)){
                 UserActive userActive = new UserActive();
                 userActive.setUser(user);
                 List<Role> roleByStudentId = roleDao.findRoleByUserId(user.getId());
                 userActive.setRoleList(roleByStudentId);
                 List<Permission> permissionList = new ArrayList<>();
                 roleByStudentId.forEach(
                         role -> {
                             List<Permission> permissionByRoleId = permissionDao.findPermissionByRoleId(role.getId());
                             permissionList.addAll(permissionByRoleId);
                         }
                 );
                 userActive.setPermissionLsit(permissionList);
                 SimpleAuthenticationInfo authorizationInfo = new SimpleAuthenticationInfo(userActive,user.getPassword(),getName());
                return authorizationInfo;
             }
         }
        return null;
    }


//    获取权限
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        UserActive primaryPrincipal = (UserActive) principalCollection.getPrimaryPrincipal();
        System.out.println(primaryPrincipal.toString());
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        primaryPrincipal.getPermissionLsit().forEach(
                permission -> {
                    simpleAuthorizationInfo.addStringPermission(permission.getName());
                }
        );
        primaryPrincipal.getRoleList().forEach(
                role -> {
                    simpleAuthorizationInfo.addRole(role.getName());
                }
        );
        return simpleAuthorizationInfo;
    }


}
