package com.redbook.redbook.service;

import com.redbook.redbook.entiy.User;

public interface UserService {
    void sendCode(String phone);


    User register(String phone, String password, String code);


    public String loginByPassword(String phone, String password);


    public String loginByCode(String phone, String code);


    public User getUserInfo(Long userId);


    public void updateUserInfo(User user);

}
