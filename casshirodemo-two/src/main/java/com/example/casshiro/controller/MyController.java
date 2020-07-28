package com.example.casshiro.controller;

import com.example.casshiro.dao.UserDao;
import com.example.casshiro.entity.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.annotation.Resources;
import javax.servlet.http.HttpSession;

@Controller
public class MyController {

    @Autowired
    UserDao userDao;

    @RequestMapping("/addProduct")
    public String addProduct(){

        return "success";
    }

    @RequestMapping("/deleteProduct")
    public String deleteProduct(){

        return "success";
    }

    @RequestMapping("/editProduct")
    public String editProduct(){

        return "success";
    }

    @RequestMapping("/updateProduct")
    public String updateProduct(){

        return "success";
    }

    @RequestMapping("/listProduct")
    public String listProduct(){

        return "success";
    }

    @RequestMapping("/deleteOrder")
    public String deleteOrder(){

        return "success";
    }

}
