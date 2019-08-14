package com.guli.aliyunoss.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author helen
 * @since 2019/7/17
 */
public interface FileService {

	/**
	 * 文件上传
	 * @param file 上传的文件
	 * @return 文件的url地址
	 */
	String upload(MultipartFile file);
}
