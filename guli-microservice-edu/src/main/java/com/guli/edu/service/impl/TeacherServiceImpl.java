package com.guli.edu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.guli.core.common.ServerResponse;
import com.guli.edu.entity.Teacher;
import com.guli.edu.mapper.TeacherMapper;
import com.guli.edu.queryvo.TeacherQuery;
import com.guli.edu.service.TeacherService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 讲师 服务实现类
 * </p>
 *
 * @author heyijie
 * @since 2019-07-12
 */
@Service
public class TeacherServiceImpl extends ServiceImpl<TeacherMapper, Teacher> implements TeacherService {

    @Override
    public void pageQuery(Page<Teacher> pageParam, TeacherQuery teacherQuery) {

        QueryWrapper<Teacher> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("sort");

        if (teacherQuery == null){
            baseMapper.selectPage(pageParam, queryWrapper);
            return;
        }

        String name = teacherQuery.getName();
        Integer level = teacherQuery.getLevel();
        String begin = teacherQuery.getBegin();
        String end = teacherQuery.getEnd();

        if (!StringUtils.isBlank(name)) {
            queryWrapper.like("name", name);
        }

        if (level!=null ) {
            queryWrapper.eq("level", level);
        }

        if (!StringUtils.isEmpty(begin)) {
            queryWrapper.ge("gmt_create", begin);
        }

        if (!StringUtils.isEmpty(end)) {
            queryWrapper.le("gmt_create", end);
        }

        baseMapper.selectPage(pageParam, queryWrapper);
    }

    @Override
    public ServerResponse pageQuery(Integer page,Integer limit,TeacherQuery teacherQuery){
        QueryWrapper<Teacher> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("sort");

        PageHelper.startPage(page,limit);
        List<Teacher> teachers;
        PageInfo pageInfo;
        if (teacherQuery == null){
            teachers  = baseMapper.selectList(null);
            pageInfo = new PageInfo(teachers,5);
            return ServerResponse.createBySuccess("查询成功",pageInfo);
        }

        String name = teacherQuery.getName();
        Integer level = teacherQuery.getLevel();
        String begin = teacherQuery.getBegin();
        String end = teacherQuery.getEnd();

        if (!StringUtils.isBlank(name)) {
            queryWrapper.like("name", name);
        }

        if (level!=null) {
            queryWrapper.eq("level", level);
        }

        if (!StringUtils.isBlank(begin)) {
            queryWrapper.ge("gmt_create", begin);
        }

        if (!StringUtils.isBlank(end)) {
            queryWrapper.le("gmt_create", end);
        }

        teachers  = baseMapper.selectList(queryWrapper);
        pageInfo = new PageInfo(teachers,5);
        return ServerResponse.createBySuccess("查询成功",pageInfo);
    }

    public ServerResponse saveTeacher(Teacher teacher){
        if(teacher==null){
            return ServerResponse.createByErrorMessage("teacher新增数据为空");
        }
        boolean isSave = this.save(teacher);
        if (!isSave){
            return ServerResponse.createByErrorMessage("teacher新增失败");
        }
        return ServerResponse.createBySuccessMessage("  teacher新增失败");
    }

    /**
     * 前台讲师分页查询
     * @param pageParam
     * @return
     */
    @Override
    public Map<String, Object> pageListWeb(Page<Teacher> pageParam) {

        QueryWrapper<Teacher> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("sort");

        baseMapper.selectPage(pageParam, queryWrapper);

        List<Teacher> records = pageParam.getRecords();
        long current = pageParam.getCurrent();
        long pages = pageParam.getPages();
        long size = pageParam.getSize();
        long total = pageParam.getTotal();
        boolean hasNext = pageParam.hasNext();
        boolean hasPrevious = pageParam.hasPrevious();

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("items", records);
        map.put("current", current);
        map.put("pages", pages);
        map.put("size", size);
        map.put("total", total);
        map.put("hasNext", hasNext);
        map.put("hasPrevious", hasPrevious);

        return map;
    }
}
