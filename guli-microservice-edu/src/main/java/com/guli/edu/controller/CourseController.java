package com.guli.edu.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guli.core.common.R;
import com.guli.core.common.ServerResponse;
import com.guli.edu.entity.Course;
import com.guli.edu.queryvo.CourseQuery;
import com.guli.edu.service.CourseService;
import com.guli.edu.service.SubjectService;
import com.guli.edu.vo.SubjectNestedVo;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author heyijie
 * @since 2019-07-12
 */
@CrossOrigin
@RestController
@RequestMapping("/edu/course")
public class CourseController {
    @Autowired
    private CourseService courseService;

    @Autowired
    private SubjectService subjectService;

    @ApiOperation(value = "分页课程列表")
    @GetMapping("{page}/{limit}")
    public R pageQuery(
            @ApiParam(name = "page", value = "当前页码", required = true)
            @PathVariable Long page,

            @ApiParam(name = "limit", value = "每页记录数", required = true)
            @PathVariable Long limit,

            @ApiParam(name = "courseQuery", value = "查询条件对象", required = false)
//          @RequestBody
            CourseQuery courseQuery
             ){

        Page<Course> pageParam = new Page<>(page, limit);

        Map<String, Object> map = courseService.pageListWeb(pageParam,courseQuery);

        List<SubjectNestedVo> subjectNestedVoList = (List<SubjectNestedVo>) subjectService.nestedList().getData();

        return  R.ok().data(map).data("subjectNestedVoList",subjectNestedVoList);
    }


    @ApiOperation(value = "课程详情")
    @GetMapping("{courseId}")
    public ServerResponse pageQuery(
            @ApiParam(name = "courseId", value = "课程id", required = true)
            @PathVariable String courseId ){

        return courseService.getCourseDetail(courseId);
    }

}

