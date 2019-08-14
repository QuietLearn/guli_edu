package com.guli.vod.controller;


import com.guli.core.common.R;
import com.guli.vod.service.VideoService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(value = "阿里云视频播放API微服务")
@CrossOrigin
@RestController
@RequestMapping("/vod/video")
public class VideoController {

    @Autowired
    private VideoService videoService;

    @GetMapping("get-play-auth/{videoId}")
    public R getVideoPlayAuth(@PathVariable("videoId") String videoId) {

        ///得到播放凭证
        String playAuth = videoService.getVideoPlayAuth(videoId);

        //返回结果
        return R.ok().message("获取凭证成功").data("playAuth", playAuth);
    }
}
