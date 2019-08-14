package com.atguigu.jwt.entity;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
public class Member implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
    private String openid;
    private String mobile;
    private String password;
    private String nickname;
    private String avatar;
}