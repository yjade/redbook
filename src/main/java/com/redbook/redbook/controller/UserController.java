package com.redbook.redbook.controller;

import com.redbook.redbook.dto.LoginByCodeDTO;
import com.redbook.redbook.dto.LoginDTO;
import com.redbook.redbook.dto.RegisterDTO;
import com.redbook.redbook.dto.SendCodeDTO;
import com.redbook.redbook.entiy.User;
import com.redbook.redbook.service.UserService;
import com.redbook.redbook.vo.Result;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    // 发送验证码
    @PostMapping("/send-code")
    public Result sendCode(@RequestBody  SendCodeDTO sendCodeDTO) {
        userService.sendCode(sendCodeDTO.getPhone());
        return Result.success("验证码已发送");
    }

    // 注册
    @PostMapping("/register")
    public Result register(@RequestBody RegisterDTO dto) {
        User user = userService.register(dto.getPhone(), dto.getPassword(), dto.getCode());
        return Result.success(user);
    }

    // 密码登录
    @PostMapping("/login")
    public Result login(@RequestBody LoginDTO dto) {
        String token = userService.loginByPassword(dto.getPhone(), dto.getPassword());
        return Result.success(Collections.singletonMap("token", token));
    }

    // 验证码登录
    @PostMapping("/login-by-code")
    public Result loginByCode(@RequestBody LoginByCodeDTO dto) {
        String token = userService.loginByCode(dto.getPhone(), dto.getCode());
        return Result.success(Collections.singletonMap("token", token));
    }

    // 获取当前用户信息
    @GetMapping("/info")
    public Result info(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        User user = userService.getUserInfo(userId);
        return Result.success(user);
    }

    // 更新用户信息
    @PostMapping("/update")
    public Result update(@RequestBody User user, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        user.setId(userId);
        userService.updateUserInfo(user);
        return Result.success("更新成功");
    }
}
