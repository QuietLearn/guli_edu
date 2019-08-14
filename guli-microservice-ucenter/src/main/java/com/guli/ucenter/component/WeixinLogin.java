package com.guli.ucenter.component;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@ConfigurationProperties(prefix = "wx.open")
@Component
@Data
public class WeixinLogin {

    private String appId;
    private String appSecret;
    private String redirectUrl;
    private String state;

    public String getWeixinQrconnectUrl(){
        /*StringBuilder stringBuilder = new StringBuilder();
        String qrconnectUrl = "https://open.weixin.qq.com/connect/qrconnect";
        stringBuilder.append(qrconnectUrl).append("?")
                .append("appid=").append("");*/
        String encodeRedirectUrl="";
        try {
            encodeRedirectUrl = URLEncoder.encode(redirectUrl,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String qrconnectUrl = String.format("https://open.weixin.qq.com/connect/qrconnect?" +
                "appid=%s&redirect_uri=%s&response_type=code&scope=snsapi_login&" +
                "state=%s#wechat_redirect", appId, encodeRedirectUrl, state);

        return qrconnectUrl;
    }

    public String getAccessTokenRequestUrl(String code){
        String accessTokenRequestUrl="https://api.weixin.qq.com/sns/oauth2/access_token?" +
                "appid=%s&secret=%s&code=%s&grant_type=authorization_code";

        String accessTokenUrl = String.format(accessTokenRequestUrl,appId,appSecret,code);

        return accessTokenUrl;
    }

    public String getUserInfoRequestUrl(String accessToken,String openId){
        String userInfoRequestUrl ="https://api.weixin.qq.com/sns/userinfo?" +
                "access_token=%s&openid=%s";

        String userInfoUrl = String.format(userInfoRequestUrl,accessToken,openId);

        return userInfoUrl;
    }

}
