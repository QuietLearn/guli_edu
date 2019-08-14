package com.guli.edu.vo.response;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

@ApiModel(value = "课程发布信息")
@Data
public class CoursePublishVo  implements Serializable {

    private static final long serialVersionUID = 1L;

    private String title;
    private String cover;
    //课时
    private Integer lessonNum;
    //课程父分类
    private String subjectLevelOne;
    //课程子分类
    private String subjectLevelTwo;
    //课程讲师名
    private String teacherName;
    //价格
    private String price;//只用于显示
}