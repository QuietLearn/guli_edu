package com.guli.ucenter.bo;

import lombok.Data;

@Data
public class AccessTokenBo {

    private String access_token;
    private String expires_in;
    private String refresh_token;
    private String openid;
    private String scope;
    private String unionid;
}
