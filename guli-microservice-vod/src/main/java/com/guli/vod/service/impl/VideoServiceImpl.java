package com.guli.vod.service.impl;

import com.aliyun.vod.upload.impl.UploadVideoImpl;
import com.aliyun.vod.upload.req.UploadStreamRequest;
import com.aliyun.vod.upload.resp.UploadStreamResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.vod.model.v20170321.*;
import com.guli.core.common.ResponseCode;
import com.guli.core.common.ServerResponse;
import com.guli.core.exception.GuliException;
import com.guli.vod.service.VideoService;
import com.guli.vod.util.AliVodSDKUtils;
import com.guli.vod.util.ConstantPropertiesUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.RescaleOp;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class VideoServiceImpl implements VideoService {

    @Override
    public ServerResponse uploadVideo(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        String title = fileName.substring(0,fileName.lastIndexOf("."));
        InputStream inputStream = null;
        try {
            inputStream = file.getInputStream();
        } catch (IOException e) {
            //e.printStackTrace();
            throw new GuliException(ResponseCode.VIDEO_UPLOAD_TOMCAT_ERROR);
        }
        UploadStreamResponse response = AliVodSDKUtils.vodStreamUpload(ConstantPropertiesUtil.ACCESS_KEY_ID, ConstantPropertiesUtil.ACCESS_KEY_SECRET, title, fileName, inputStream);
        if (response==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.PARAM_ERROR);
        }
        if (response.isSuccess()) {
            log.info("videoId={}",response.getVideoId());
            return ServerResponse.createBySuccess("视频上传成功",response.getVideoId());
        } else { //如果设置回调URL无效，不影响视频上传，可以返回VideoId同时会返回错误码。其他情况上传失败时，VideoId为空，此时需要根据返回错误码分析具体错误原因
            log.error("VideoId={}，ErrorCode={}，ErrorMessage={}",response.getVideoId(),response.getCode(),response.getMessage());
            if (StringUtils.isBlank(response.getVideoId())){
                return ServerResponse.createByErrorMessage("上传视频失败");
            }
            return ServerResponse.createByErrorMessage("回调失败");
        }
    }


    public ServerResponse deleteVideos(String videoIds){
        DeleteVideoResponse response;
        try {
            response = AliVodSDKUtils.deleteVideo(videoIds);
        } catch (Exception e) {
            log.error("ErrorMessage = " + e.getLocalizedMessage());
            throw new GuliException(ResponseCode.VIDEO_DELETE_ALIYUN_ERROR);
        }
        System.out.print("RequestId = " + response.getRequestId() + "\n");

        return ServerResponse.createBySuccessMessage("删除阿里云视频成功");
    }

    @Override
    public ServerResponse getUploadAuthAndAddress(String title, String fileName) {
        CreateUploadVideoResponse response;
        try {
            response = AliVodSDKUtils.createUploadVideo(title, fileName);
        } catch (ClientException e) {
            throw new GuliException(ResponseCode.FETCH_VIDEO_UPLOAD_PLAYAUTH_ERROR);
        }
       /* private String requestId;
        private String videoId;
        private String uploadAddress;
        private String uploadAuth;*/
       if (response==null){
           return ServerResponse.createByErrorCodeMessage(ResponseCode.FETCH_VIDEO_UPLOAD_PLAYAUTH_ERROR);
       }
        String requestId = response.getRequestId();
        String uploadAddress = response.getUploadAddress();
        String uploadAuth = response.getUploadAuth();
        String videoId = response.getVideoId();
        log.info("requestId={},videoId={},uploadAddress={},uploadAuth={}",
                requestId,uploadAddress,uploadAuth,videoId);

        Map<String,String> map = new HashMap();
        map.put("uploadAddress",uploadAddress);
        map.put("uploadAuth",uploadAuth);
        map.put("videoId",videoId);

        return ServerResponse.createBySuccess("获取视频上传地址和凭证成功",map);
    }

    @Override
    public ServerResponse refreshUploadAuthAndAddress(String videoId) {
        RefreshUploadVideoResponse response;
        try {
            response = AliVodSDKUtils.refreshUploadVideo(videoId);
        } catch (Exception e) {
            throw new GuliException(ResponseCode.REFRESH_VIDEO_UPLOAD_PLAYAUTH_ERROR);
        }
        String requestId = response.getRequestId();
        String uploadAddress = response.getUploadAddress();
        String uploadAuth = response.getUploadAuth();
        videoId = response.getVideoId();
        log.info("requestId={},videoId={}, uploadAddress={},uploadAuth={}",
                requestId,uploadAddress,uploadAuth,videoId);

        Map<String,String> map = new HashMap();
        map.put("uploadAuth",uploadAuth);
        return ServerResponse.createBySuccess("刷新视频上传地址和凭证成功",map);
    }

    @Override
    public String getVideoPlayAuth(String videoId) {
        try {
            GetVideoPlayAuthResponse response = AliVodSDKUtils.getVideoPlayAuth(videoId);
            //得到播放凭证
            String playAuth = response.getPlayAuth();

            return playAuth;
        } catch (ClientException e) {
            throw new GuliException(ResponseCode.REFRESH_VIDEO_PLAYAUTH_ERROR);
        }
    }
}