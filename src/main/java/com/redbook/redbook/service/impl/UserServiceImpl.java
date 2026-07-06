package com.redbook.redbook.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.redbook.redbook.entiy.User;
import com.redbook.redbook.mapper.UserMapper;
import com.redbook.redbook.service.UserService;
import com.redbook.redbook.util.JwtUtil;
import io.jsonwebtoken.lang.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // 发送验证码（存Redis，5分钟过期）
    public void sendCode(String phone) {
        String code = String.format("%06d", new Random().nextInt(999999));
        redisTemplate.opsForValue().set("login:code:" + phone, code, 5, TimeUnit.MINUTES);
        // TODO: 实际项目中调用短信服务发送验证码
        System.out.println("验证码：" + code); // 开发阶段打印到控制台
    }

    // 注册
    public User register(String phone, String password, String code) {
        // 校验验证码
        String redisCode = redisTemplate.opsForValue().get("login:code:" + phone);
        if (redisCode == null || !redisCode.equals(code)) {
            throw new RuntimeException("验证码错误或已过期");
        }

        // 检查手机号是否已注册
//        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.eq(User::getPhone, phone);
//        List<User> users = userMapper.selectList(queryWrapper);
//        if (!Collections.isEmpty(users)){
//            throw new RuntimeException("手机号已注册");
//        }

        if (userMapper.selectByPhone(phone) != null) {
            throw new RuntimeException("手机号已注册");
        }

        // 创建用户
        User user = new User();
        user.setPhone(phone);
        user.setUsername("用户" + phone.substring(7)); // 默认用户名
        user.setPassword(passwordEncoder.encode(password)); // BCrypt加密
        user.setAvatar("https://your-oss-bucket/default-avatar.png"); // 默认头像
        userMapper.insert(user);

        return user;
    }

    // 登录（密码登录）
    public String loginByPassword(String phone, String password) {
        User user = userMapper.selectByPhone(phone);
        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("手机号或密码错误");
        }
        return jwtUtil.generateToken(user.getId());
    }

    // 登录（验证码登录）
    public String loginByCode(String phone, String code) {
        String redisCode = redisTemplate.opsForValue().get("login:code:" + phone);
        if (redisCode == null || !redisCode.equals(code)) {
            throw new RuntimeException("验证码错误或已过期");
        }

        User user = userMapper.selectByPhone(phone);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        return jwtUtil.generateToken(user.getId());
    }

    // 获取用户信息
    public User getUserInfo(Long userId) {
        return userMapper.selectById(userId);
    }

    // 更新用户信息
    public void updateUserInfo(User user) {
        userMapper.updateById(user);
    }
}
