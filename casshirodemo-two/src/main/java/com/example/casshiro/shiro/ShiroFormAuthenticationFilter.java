package com.example.casshiro.shiro;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.cas.CasFilter;
import org.apache.shiro.subject.Subject;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ShiroFormAuthenticationFilter extends CasFilter {
    private String homePageUrl;


    private String ssoLoginUrl;    //cas sso 登录页面
    private String localLoginUrl;  //本地客户端的认证回调地址
    private String desSecret;       //本地客户端的认证回调地址  的DES加密密钥

    /*@Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {

        return true;

    }*/

    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {
        System.out.println("==========executeLogin==========");
        AuthenticationToken token = createToken(request, response);
        try {
            System.out.println("========= token 是否为空===" + token);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (token == null) {
            String msg = "createToken method implementation returned null. A valid non-null AuthenticationToken " +
                    "must be created in order to execute a login attempt.";
            throw new IllegalStateException(msg);
        }
        try {
            System.out.println("========是否登录成功===========");
            Subject subject = getSubject(request, response);
            subject.login(token);
            System.out.println("========登录成功===========");
            return onLoginSuccess(token, subject, request, response);
        } catch (AuthenticationException e) {
            return onLoginFailure(token, e, request, response);
        }

    }




    @Override
    protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request, ServletResponse response) throws Exception {
        HttpServletResponse rep = toHttp(response);
        System.out.println("=====onLoginSuccess=====");
        rep.setStatus(302);
        rep.setHeader("Location", homePageUrl);
        return true;
    }



    public HttpServletRequest toHttp(ServletRequest request) {
        return (HttpServletRequest)request;
    }

    public  HttpServletResponse toHttp(ServletResponse response) {
        return (HttpServletResponse)response;
    }

    public String getHomePageUrl() {
        return homePageUrl;
    }

    public void setHomePageUrl(String homePageUrl) {
        this.homePageUrl = homePageUrl;
    }

    public String getSsoLoginUrl() {
        return ssoLoginUrl;
    }

    public void setSsoLoginUrl(String ssoLoginUrl) {
        this.ssoLoginUrl = ssoLoginUrl;
    }

    public String getLocalLoginUrl() {
        return localLoginUrl;
    }

    public void setLocalLoginUrl(String localLoginUrl) {
        this.localLoginUrl = localLoginUrl;
    }

    public String getDesSecret() {
        return desSecret;
    }

    public void setDesSecret(String desSecret) {
        this.desSecret = desSecret;
    }
}
