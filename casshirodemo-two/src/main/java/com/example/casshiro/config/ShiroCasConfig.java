package com.example.casshiro.config;

import com.example.casshiro.shiro.MyShiroCasRealm;
import com.example.casshiro.shiro.ShiroFormAuthenticationFilter;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.cas.CasFilter;
import org.apache.shiro.cas.CasSubjectFactory;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.authc.LogoutFilter;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.jasig.cas.client.session.SingleSignOutFilter;
import org.jasig.cas.client.session.SingleSignOutHttpSessionListener;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.apache.shiro.mgt.SecurityManager;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.DelegatingFilterProxy;
//import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;

import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroCasConfig {
    //    cas 的server地址
    public static final String casServerUrlPrefix = "http://localhost:8080/cas";
    //    cas 登录页面的地址
    public static final String casLoginUrl = casServerUrlPrefix + "/login";
    //    cas 登出页面地址
    public static final String casLogoutUrl = casServerUrlPrefix + "/logout";
    //    当前工程对外提供的服务地址
    public static final String shiroServerUrlPrefix = "http://localhost:8082";
    //    casFilter cas 拦截的地址
    public static final String casFilterUrlPattern = "/shiro-cas-two";
    //    登录的地址
    public static final String loginUrl = casLoginUrl + "?service=" + shiroServerUrlPrefix + casFilterUrlPattern;
    //    退出的地址
    public static final String logoutUrl = casLogoutUrl + "?service=" + loginUrl;
    //    登录成功的地址
    public static final String loginSuccessUrl = "/";
    //    失败的地址
    public static final String unauthorizedUrl = "/403";

    @Bean
    public SecurityManager securityManager() {
        DefaultWebSecurityManager defaultWebSecurityManager = new DefaultWebSecurityManager();
//        才采用缓存
        defaultWebSecurityManager.setCacheManager(null);
//        指定shiro
        defaultWebSecurityManager.setRealm(myShiroCasRealm());
//        指定subjectFactory，如果实现的cas的remember me（免登录） 的功能，
//        defaultWebSecurityManager.setSubjectFactory(new CasSubjectFactory());
        return defaultWebSecurityManager;
    }


    @Bean
    public MyShiroCasRealm myShiroCasRealm() {
        MyShiroCasRealm myShiroCasRealm = new MyShiroCasRealm();
//        设置cas登录服务器地址的前缀
        myShiroCasRealm.setCasServerUrlPrefix(casServerUrlPrefix);
//        客户端回调地址，登录成功后的跳转的地址（自己的服务器）
        myShiroCasRealm.setCasService(shiroServerUrlPrefix + casFilterUrlPattern);
        myShiroCasRealm.setCachingEnabled(true);
//        //启用身份验证缓存，即缓存AuthenticationInfo信息，默认false
//        myShiroCasRealm.setAuthenticationCachingEnabled(true);
//        //缓存AuthenticationInfo信息的缓存名称 在ehcache-shiro.xml中有对应缓存的配置
//        myShiroCasRealm.setAuthenticationCacheName("authenticationCache");
//        //启用授权缓存，即缓存AuthorizationInfo信息，默认true
////        shiroRealm.setAuthorizationCachingEnabled(true);
//        //缓存AuthorizationInfo信息的缓存名称  在ehcache-shiro.xml中有对应缓存的配置
//        myShiroCasRealm.setAuthorizationCacheName("authorizationCache");
        return myShiroCasRealm;
    }
    /**
     * 注册单点登出listener
     *
     * @return
     */
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public ServletListenerRegistrationBean singleSignOutHttpSessionListener() {
        System.out.println("注册单点登出listener");
        ServletListenerRegistrationBean<SingleSignOutHttpSessionListener> listener = new ServletListenerRegistrationBean<>();
        listener.setEnabled(true);
        listener.setListener(new SingleSignOutHttpSessionListener());
        listener.setOrder(1);
        return listener;
    }

    /**
     * 注册单点登出filter
     *
     * @return
     */
    @Bean
    public FilterRegistrationBean singleSignOutFilter() {
        System.out.println("注册单点登出filter");
        FilterRegistrationBean bean = new FilterRegistrationBean();
        bean.setFilter(new SingleSignOutFilter());
        bean.setEnabled(true);
        bean.addUrlPatterns("/*");
        bean.addInitParameter("casServerUrlPrefix", casServerUrlPrefix);
        bean.addInitParameter("serverName","SingleSignOutFilter");
        return bean;
    }






    //    注册DelegatingFilterProxy(shiro)    是一个代理类，用于管理拦截器的生命周期，  所有的请求都会拦截 ,在创建的时候，filter的执行会优先于bean的执行，所以需要使用该类先来管理bean
    /*
     *   先在filter中加入DelegatingFilterProxy类，"targetFilterLifecycle"指明作用于filter的所有生命周期。
     *   原理是，DelegatingFilterProxy类是一个代理类，所有的请求都会首先发到这个filter代理，然后再按照"filter-name"委派到spring中的这个bean。
     *   在Spring中配置的bean的name要和web.xml中的<filter-name>一样.
        此外，spring bean实现了Filter接口，但默认情况下，是由spring容器来管理其生命周期的(不是由tomcat这种服务器容器来管理)。
        如果设置"targetFilterLifecycle"为True，则spring来管理Filter.init()和Filter.destroy()；若为false，则这两个方法失效！！
    该步只是将当前的的生命周期交给了spring管理，具体的管理还是需要下面的LifecycleBeanPostProcessor的对象去进行操作
 bean.addUrlPatterns("/*"); :表示的是拦截所有的请求
     * */

    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean bean = new FilterRegistrationBean();
        bean.setFilter(new DelegatingFilterProxy("shiroFilter")); //设置的shiro的拦截器 ShiroFilterFactoryBean
        bean.addInitParameter("targetFilterLifecycle", "true");
        bean.setEnabled(true);
        bean.addUrlPatterns("/*");
        return bean;
    }
    //    设置方法的自动初始化和销毁，init和destory方法被自动调用。注意，如果使用了该类，则不需要手动初始化方法和销毁方法，否则出错
    @Bean(name = "lifecycleBeanPostProcessor")
    public LifecycleBeanPostProcessor getLifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }
    //    开启shiro aop 的注解支持，使用代理的方式，所以需要开启代码的支持
    @Bean
    public DefaultAdvisorAutoProxyCreator getDefaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator proxyCreator = new DefaultAdvisorAutoProxyCreator();
//        设置代理方式，true是cglib的代理方式，false是普通的jdk代理方式
        proxyCreator.setProxyTargetClass(true);
        return proxyCreator;
    }

    //    开启注解
    @Bean
    public AuthorizationAttributeSourceAdvisor attributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }

    //  使用工厂模式，创建并初始化ShiroFilter

    @Bean(name = "shiroFilter")
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(SecurityManager securityManager, CasFilter casFilter) {
        ShiroFilterFactoryBean factoryBean = new ShiroFilterFactoryBean();
        factoryBean.setSecurityManager(securityManager);
//        如果不设置，会自动寻找目录下的/login.jsp页面
        factoryBean.setLoginUrl(loginUrl);
//        设置无权限访问页面
        factoryBean.setUnauthorizedUrl(unauthorizedUrl);
//        添加casFilter中，注意，casFilter需要放到shiroFilter的前面
        Map<String, Filter> linkedHashMap = new LinkedHashMap<>();
        linkedHashMap.put("casFilter", casFilter);
        factoryBean.setSuccessUrl(loginSuccessUrl);
        factoryBean.setFilters(linkedHashMap);
        loadShiroFilterChain(factoryBean);
        return factoryBean;
    }

    //    定义cas的拦截器
    @Bean(name = "casFilter")
    public CasFilter getCasFilter() {
        CasFilter filter = new CasFilter ();
//        自动注入拦截器的名称
        filter.setName("casFilter");
//        是否自动的将当前的拦截器进行注入
        filter.setEnabled(true);
//        在登录失败之后，也就是shiro执行CasRealm的doGetAuthenticationInfo  方法向CasServer验证tiker
        filter.setFailureUrl(loginUrl);//认证失败之后，重新登录
        filter.setSuccessUrl(loginSuccessUrl);
        return filter;
    }
    /**
     * 加载shiroFilter权限控制规则（从数据库读取然后配置）,角色/权限信息由MyShiroCasRealm对象提供doGetAuthorizationInfo实现获取来的
     * 生产中会将这部分规则放到数据库中
     *
     * @param shiroFilterFactoryBean
     */

    private void loadShiroFilterChain(ShiroFilterFactoryBean shiroFilterFactoryBean) {
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();

        filterChainDefinitionMap.put(casFilterUrlPattern, "casFilter");

        //2.不拦截的请求
        filterChainDefinitionMap.put("/css/**", "anon");
        filterChainDefinitionMap.put("/js/**", "anon");
        filterChainDefinitionMap.put("/login", "anon");
        // 此处将logout页面设置为anon，而不是logout，因为logout被单点处理，而不需要再被shiro的logoutFilter进行拦截
        filterChainDefinitionMap.put("/logout", "anon");
        filterChainDefinitionMap.put("/error", "anon");
        //3.拦截的请求（从本地数据库获取或者从casserver获取(webservice,http等远程方式)，看你的角色权限配置在哪里）
        filterChainDefinitionMap.put("/", "authc"); //需要登录

        //4.登录过的不拦截
        filterChainDefinitionMap.put("/**", "authc");

        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
    }


}
