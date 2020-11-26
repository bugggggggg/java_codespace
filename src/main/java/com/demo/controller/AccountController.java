package com.demo.controller;

import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.demo.common.lang.Result;
import com.demo.common.lang.dto.LoginDto;
import com.demo.entity.User;
import com.demo.service.UserService;
import com.demo.util.JwtUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
public class AccountController {

    @Autowired
    UserService userService;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/login")
    public Result login(@Validated @RequestBody LoginDto loginDto, HttpServletResponse response){

        //User user=userService.getOne(new QueryWrapper<User>().eq("username",loginDto.getUsername()));
        User user=userService.getOne(new QueryWrapper<User>().eq("email",loginDto.getEmail()));
        Assert.notNull(user,"用户不存在");

        if(!user.getPassword().equals(loginDto.getPassword()))
        {
            return Result.fail("密码不正确");
        }

//        String jwt=jwtUtils.generateToken(user.getId());
//        response.setHeader("Authorization",jwt);
//        response.setHeader("Access-control-Expose-Headers","Authorization");

        return Result.succ(MapUtil.builder()
                .put("userid",user.getId())
                .put("username",user.getUsername())
                .put("avatar",user.getAvatar())
                .put("email",user.getEmail())
                .map()
        );

    }

    @RequiresAuthentication
    @GetMapping("/logout")
    public Result logout(){
        //SecurityUtils.getSubject().logout();
        return Result.succ(null);
    }

}
