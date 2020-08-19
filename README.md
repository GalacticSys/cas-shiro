# 单点登录demo
## 1.开发环境
    win10、jdk1.8
## 2.技术栈
    cas-overlay-template-5.3、springBoot-2.3.1.RELEASE、mybatis、shiro、thymeleaf
    shiro-cas-1.4.0、apache-maven-3.6.2、mysql-8.0.21
## 3.项目运行
  ### 3.1用idea打开，分别运行分别运行casshirodemo、casshirodemo-two项目
  ### 3.2cas-overlay-template-master配置tomcat、将项目打成war包，配置启动项deployment为target目录下的cas.war文件，Application context为/cas.配置项目访问路径为http://localhost:8080/cas/
  ### 3.3若要使用https，须先使用jdk生成数字证书，放入resources目录下
    
## 4.效果演示
   ### 4.1登录：
   #### 4.1.1访问casshirodemo-two项目跳转到cas-overlay-template-master项目对应casshirodemo-two的登录页
   ![Image text](https://github.com/GalacticSys/image/blob/master/cas-shiro/casshirodemo-two/login.PNG)
   #### 4.1.1访问casshirodemo项目调转到cas-overlay-template-master项目对应casshirodemo的登录页
   ![Image text](https://github.com/GalacticSys/image/blob/master/cas-shiro/casshirodemo-two/login.PNG)
   ### 4.2登陆成功图片
   ![Image text](https://github.com/GalacticSys/image/blob/master/cas-shiro/casshirodemo-two/loginSuccess.PNG)
   ### 4.3刷新casshirodemo项目显示菜单页
   ![Image text](https://github.com/GalacticSys/image/blob/master/cas-shiro/casshirodemo-two/restartOther.PNG)
   ### 4.4退出：
   #### 4.4.1在casshirodemo-two项目中退出，会跳转到登录页，此时，若要刷新casshirodemo项目，也将会跳转到登录页。casshirodemo项目中退出同理
   ![Image text](https://github.com/GalacticSys/image/blob/master/cas-shiro/casshirodemo-two/restartOther.PNG)
    
    
