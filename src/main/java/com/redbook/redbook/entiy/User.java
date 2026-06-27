package com.redbook.redbook.entiy;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@TableName("user")
@Data
public class User {
    private Long id;
    private String username;
    private String avatar;
    private String phone;
    private String password;
    private Integer gender;
    private String bio;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
