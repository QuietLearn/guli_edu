package com.guli.vod.controller.admin;

import com.guli.core.common.ServerResponse;
import com.guli.vod.service.VideoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Api(value = "阿里云视频上传API微服务")
@CrossOrigin
@RestController
@RequestMapping("/admin/vod/video")
public class VideoAdminController {


    @Autowired
    private VideoService videoService;

    @PostMapping("upload")
    public ServerResponse uploadVideo(
            @ApiParam(name = "file", value = "文件", required = true)
            @RequestParam("file") MultipartFile file) {
        return videoService.uploadVideo(file);
    }

    @DeleteMapping("{videoId}")
    public ServerResponse deleteVideo(
            @ApiParam(name = "videoId", value = "阿里云视频id", required = true)
            @PathVariable("videoId") String videoId) {
        return videoService.deleteVideos(videoId);
    }

    /**
     * 获取视频上传地址和凭证
     *
     * @param title
     * @param fileName
     * @return
     */
    @GetMapping("get-upload-auth-and-address/{title}/{fileName}")
    public ServerResponse getUploadAuthAndAddress(
            @ApiParam(name = "title", value = "视频标题", required = true)
            @PathVariable String title,

            @ApiParam(name = "fileName", value = "视频源文件名", required = true)
            @PathVariable String fileName) {

        return videoService.getUploadAuthAndAddress(title, fileName);
    }

    /**
     * 刷新视频上传凭证
     *
     * @param videoId
     * @return
     */
    @GetMapping("refresh-upload-auth-and-address/{videoId}")
    public ServerResponse refreshUploadAuth(
            @ApiParam(name = "videoId", value = "云端视频id", required = true)
            @PathVariable String videoId) {

        return videoService.refreshUploadAuthAndAddress(videoId);
    }
}
