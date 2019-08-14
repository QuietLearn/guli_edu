package com.guli.aliyunoss.controller;

import com.guli.aliyunoss.service.FileService;
import com.guli.aliyunoss.util.ConstantPropertiesUtil;
import com.guli.core.common.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author hyj
 * @since 2019/7/17
 */
@Api(value = "阿里云oss文件管理")
@CrossOrigin
@RestController
@RequestMapping("/admin/oss/file")
public class FileController {

	@Autowired
	private FileService fileService;

	@ApiOperation(value = "文件上传")
	@PostMapping("upload")
	public R upload(
			@ApiParam(name = "file", value = "文件", required = true)
			@RequestParam("file") MultipartFile file,

			@ApiParam(name = "host", value = "文件上传路径(oss路径)", required = false)
			@RequestParam(value = "host", required = false) String host){
		if(!StringUtils.isEmpty(host)){
			ConstantPropertiesUtil.FILE_HOST = host;
		}

		String url = fileService.upload(file);
		return R.ok().message("文件上传成功").data("url", url);
	}
}
