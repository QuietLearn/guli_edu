package com.guli.edu.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guli.core.common.ServerResponse;
import com.guli.edu.entity.Teacher;
import com.baomidou.mybatisplus.extension.service.IService;
import com.guli.edu.queryvo.TeacherQuery;
import org.apache.catalina.Server;

import java.util.Map;

/**
 * <p>
 * 讲师 服务类
 * </p>
 *
 * @author heyijie
 * @since 2019-07-12
 */
public interface TeacherService extends IService<Teacher> {

    /**
     * 后台条件分页查询  mp自带分页page版本
     * @param pageParam
     * @param teacherQuery
     */
    void pageQuery(Page<Teacher> pageParam, TeacherQuery teacherQuery);

    /**
     * 后台条件分页查询  mp + pagehelper整合版本
     * @param page
     * @param limit
     * @param teacherQuery
     * @return
     */
    ServerResponse pageQuery(Integer page,Integer limit,TeacherQuery teacherQuery);

    /**
     * 后台保存老师信息
     * @param teacher
     * @return
     */
    ServerResponse saveTeacher(Teacher teacher);

    /**
     * 前台讲师列表
     * @param pageParam
     * @return
     */
    public Map<String, Object> pageListWeb(Page<Teacher> pageParam);
}
