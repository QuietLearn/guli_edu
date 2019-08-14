package com.guli.ucenter.controller;

import com.google.gson.Gson;
import com.guli.core.common.R;
import com.guli.core.common.ResponseCode;
import com.guli.core.exception.GuliException;
import com.guli.core.util.JsonUtil;
import com.guli.ucenter.bo.AccessTokenBo;
import com.guli.ucenter.component.WeixinLogin;
import com.guli.ucenter.entity.Member;
import com.guli.ucenter.service.MemberService;
import com.guli.ucenter.util.HttpClientUtils;
import com.guli.ucenter.util.JwtUtils;
import com.guli.ucenter.vo.LoginInfoVo;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;

@Slf4j
@CrossOrigin
@Controller
@RequestMapping("api/ucenter/wx")
public class WxApiController {

    @Autowired
    private WeixinLogin weixinLogin;
    @Autowired
    private MemberService memberService;

    @GetMapping("login")
    public String wxLogin(HttpSession session){

        session.setAttribute("state",weixinLogin.getState());
        String weixinQrconnectUrl = weixinLogin.getWeixinQrconnectUrl();

        return "redirect:"+weixinQrconnectUrl;
    }

    @GetMapping("callback")
    public String callBack(String code,String state,HttpSession session){
        String sessionState = (String) session.getAttribute("state");
        if (!StringUtils.equals(sessionState,state)||StringUtils.isBlank(code)){
            log.error("state不相等或者code为空");
            throw new GuliException(ResponseCode.ILLEGAL_CALLBACK_REQUEST_ERROR);
        }

        String accessTokenRequestUrl = weixinLogin.getAccessTokenRequestUrl(code);
        String accessTokenStr="";
        try {
            //获取response body的内容
            accessTokenStr = HttpClientUtils.get(accessTokenRequestUrl);
        } catch (Exception e) {
            throw new GuliException(ResponseCode.FETCH_ACCESSTOKEN_FAILD);
        }

        log.info("得到微信响应体内容，accessToken={}",accessTokenStr);
//        Gson
        AccessTokenBo accessTokenBo = JsonUtil.Json2Obj(accessTokenStr, AccessTokenBo.class);
        if (accessTokenBo==null){
            throw new GuliException(ResponseCode.FETCH_ACCESSTOKEN_FAILD);
        }
        String accessToken = accessTokenBo.getAccess_token();
        String openId = accessTokenBo.getOpenid();
        if (StringUtils.isBlank(accessToken)||StringUtils.isBlank(openId)){
            throw new GuliException(ResponseCode.FETCH_ACCESSTOKEN_FAILD);
        }
        Member member = memberService.getByOpenid(openId);
        if (member==null){
            String userInfoRequestUrl = weixinLogin.getUserInfoRequestUrl(accessToken,openId);
            String memberInfo = "";
            try {
                memberInfo = HttpClientUtils.get(userInfoRequestUrl);
            } catch (Exception e) {
                throw new GuliException(ResponseCode.FETCH_USERINFO_ERROR);
            }

            Gson gson = new Gson();
            HashMap<String,String> memberHashmap = gson.fromJson(memberInfo, HashMap.class);
            String openid = memberHashmap.get("openid");
            String avatar = memberHashmap.get("headimgurl");
            String nickname = memberHashmap.get("nickname");
            member = new Member();
            member.setNickname(nickname);
            member.setAvatar(avatar);
            member.setOpenid(openid);
            memberService.save(member);
        }



        String jwtToken = JwtUtils.generateJWT(member);


        return "redirect:http://localhost:3000?token=" + jwtToken;
    }


    @PostMapping("parse-jwt")
    @ResponseBody
    public R getLoginInfoByJwtToken(@RequestBody String jwtToken){
        if (StringUtils.isBlank(jwtToken)){
            return R.error().message("用户未登录");
        }

        Claims claims = JwtUtils.checkJWT(jwtToken);
        if (claims==null){
            return R.error().message("token被篡改或已失效");
        }
        String id = (String)claims.get("id");
        String nickname = (String)claims.get("nickname");
        String avatar = (String)claims.get("avatar");

        LoginInfoVo loginInfoVo = new LoginInfoVo();
        loginInfoVo.setId(id);
        loginInfoVo.setNickname(nickname);
        loginInfoVo.setAvatar(avatar);

        return R.ok().data("loginInfo", loginInfoVo);

    }
}
