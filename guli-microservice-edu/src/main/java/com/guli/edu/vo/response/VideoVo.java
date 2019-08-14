package com.guli.edu.vo.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value="VideoVo Vo对象", description="VideoVo Vo对象")
public class VideoVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "视频ID")
    private String id;

    @ApiModelProperty(value = "节点名称")
    private String title;

    @ApiModelProperty(value = "云端视频资源")
    private String videoSourceId;

    @ApiModelProperty(value = "原始文件名称")
    private String videoOriginalName;

    @ApiModelProperty(value = "排序字段")
    private Integer sort;

    @ApiModelProperty(value = "播放次数")
    private Long playCount;

    @ApiModelProperty(value = "是否可以试听：0收费 1免费")
    private Boolean free;

    @ApiModelProperty(value = "视频时长（秒）")
    private Float duration;

 /*   @ApiModelProperty(value = "视频状态 草稿还是已发布")
    private String status;*/

    @ApiModelProperty(value = "视频源文件大小（字节）")
    private Long size;


}
