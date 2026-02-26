package com.lhd.tams.module.user.controller;


import com.lhd.tams.module.user.domain.User;
import com.lhd.tams.module.user.service.UserService;
import com.lhd.tams.module.user.utils.Result;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "http://localhost:12011")
public class UserController {
    @Resource
    private UserService userService;

    @PostMapping("/login")
    public Result<User> loginController(@RequestParam String teacherName, @RequestParam String password){
        User user = userService.loginService(teacherName, password);
        if(user!=null){
            return Result.success(user,"登录成功！");
        }else{
            return Result.error("123","账号或密码错误！");
        }
    }


    @PostMapping("/register")
    public Result<User> registController(@RequestBody User newUser){
        User user = userService.registerService(newUser);
        if(user!=null){
            return Result.success(user,"注册成功！");
        }else{
            return Result.error("456","用户名已存在！");
        }
    }
}
