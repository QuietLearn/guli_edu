package com.guli.edu.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guli.core.common.ServerResponse;
import com.guli.edu.entity.Course;
import com.baomidou.mybatisplus.extension.service.IService;
import com.guli.edu.queryvo.CourseQuery;
import com.guli.edu.vo.response.CoursePublishVo;
import com.guli.edu.vo.request.CourseInfoForm;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author heyijie
 * @since 2019-07-12
 */
public interface CourseService extends IService<Course> {
    /**
     * 保存课程和课程详情信息
     * @param courseInfoForm
     * @return 新生成的课程id
     */
    String saveCourseInfo(CourseInfoForm courseInfoForm);


    CourseInfoForm getCourseInfoFormById(String id);

    void updateCourseInfoById(CourseInfoForm courseInfoForm);

    void pageQuery(Page<Course> pageParam, CourseQuery courseQuery);

    void removeCourseById(String id);

    CoursePublishVo getCoursePublishVoById(String id);

    void publishCourseById(String id);

    /**
     * 前台课程分页
     * @param pageParam
     * @return
     */
    Map<String, Object> pageListWeb(Page<Course> pageParam,CourseQuery courseQuery);

    /**
     * 前台--封装获取课程详情
     * @param courseId
     * @return
     */
    ServerResponse getCourseDetail(String courseId);

    /**
     * 根据讲师id查询讲师所讲课程列表
     * @param teacherId
     * @return
     */
    List<Course> selectByTeacherId(String teacherId);
}
