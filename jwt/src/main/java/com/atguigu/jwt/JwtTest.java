package com.atguigu.jwt;

import com.atguigu.jwt.entity.Member;
import com.atguigu.jwt.util.JwtUtils;
import io.jsonwebtoken.Claims;
import org.junit.Test;

import java.util.LinkedHashMap;

public class JwtTest {

    @Test
    public void testGenerateJWT(){

        Member member = new Member();
        member.setId("10000");
        member.setNickname("Helen");
        member.setAvatar("1.png");
        String jwt = JwtUtils.generateJWT(member);
        System.out.println(jwt);
    }


    @Test
    public void testCheckJWT(){
        String jwtToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJndWxpLXVzZXIiLCJpZ I6IjEwMDAwIiwibmlja25hbWUiOiJIZWxlbiIsImF2YXRhciI6IjEucG5nIiwibWVtYmVyIjp7ImlkIjoiMTAwMDAiLCJvcGVuaWQiOm51bGwsIm1vYmlsZSI6bnVsbCwicGFzc3dvcmQiOm51bGwsIm5pY2tuYW1lIjoiSGVsZW4iLCJhdmF0YXIiOiIxLnBuZyJ9LCJpYXQiOjE1NjQ0ODc0OTQsImV4cCI6MTU2NDQ4OTI5NH0.VgKmy3m3oojsjIKz-Rg6Xkom7wrdpjnLT2Vl1lm3_uw";
        Claims claims = JwtUtils.checkJWT(jwtToken);

        String nickname = (String)claims.get("nickname");
        String avatar = (String)claims.get("avatar");
        LinkedHashMap memebr = (LinkedHashMap)claims.get("member");

        System.out.println(memebr);
        System.out.println(nickname);
        System.out.println(avatar);
    }
}