package com.example.casshiro.shiro;

import com.example.casshiro.dao.PermissionDao;
import com.example.casshiro.dao.RoleDao;
import com.example.casshiro.dao.UserDao;
import com.example.casshiro.entity.Permission;
import com.example.casshiro.entity.Role;
import com.example.casshiro.entity.User;
import com.example.casshiro.entity.UserActive;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cas.CasRealm;
import org.apache.shiro.subject.PrincipalCollection;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
public class MyShiroCasRealm extends CasRealm {

    @Resource
    UserDao userDao;
    @Resource
    RoleDao roleDao;
    @Resource
    PermissionDao permissionDao;
    /**
     * 用于授权
     *
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection){
        System.out.println("进行授权 -------->");
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        String userName = (String) super.getAvailablePrincipal(principalCollection);
        User user = userDao.findUserByName(userName);
        List<Role> roleByUserId = roleDao.findRoleByUserId(user.getId());

        List<Permission> permissionList = new ArrayList<>();
        roleByUserId.forEach(
                role -> {
                    List<Permission> permissionByRoleId = permissionDao.findPermissionByRoleId(role.getId());
                    info.addRole(role.getName());
                    permissionList.addAll(permissionByRoleId);
                }
        );
        permissionList.forEach(
                permission -> {
                    info.addStringPermission(permission.getName());
                }
        );
        return info;
    }

    /**
     * 用于认证
     *
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
//        调用父类的认证，父类认证已经完成了
        AuthenticationInfo authenticationInfo = super.doGetAuthenticationInfo(authenticationToken);
        String name = (String) authenticationInfo.getPrincipals().getPrimaryPrincipal();
        SecurityUtils.getSubject().getSession().setAttribute("no", name);
        return authenticationInfo;
    }
}
