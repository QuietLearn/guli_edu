package com.guli.vod.service;

import com.guli.core.common.ServerResponse;
import org.springframework.web.multipart.MultipartFile;

public interface VideoService {

    ServerResponse uploadVideo(MultipartFile file);

    ServerResponse deleteVideos(String videoIds);

    ServerResponse getUploadAuthAndAddress(String title, String fileName);

    ServerResponse refreshUploadAuthAndAddress(String videoId);

    String getVideoPlayAuth(String videoId);
}