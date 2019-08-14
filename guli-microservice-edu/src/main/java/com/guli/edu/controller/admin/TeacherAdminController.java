package com.guli.edu.controller.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guli.core.common.R;
import com.guli.core.common.ResponseCode;
import com.guli.core.common.ServerResponse;
import com.guli.edu.entity.Teacher;
import com.guli.edu.queryvo.TeacherQuery;
import com.guli.edu.service.TeacherService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "教师后台管理模块")
@RestController
@RequestMapping("/admin/edu/teacher")
@CrossOrigin //跨域
public class TeacherAdminController {

    @Autowired
    private TeacherService teacherService;

    @ApiOperation(value = "所有讲师列表")
//    不写list更加符合Rest风格的url
    @GetMapping
    public ServerResponse list(){
        List<Teacher> list = teacherService.list(null);
        if (CollectionUtils.isEmpty(list)){
            return ServerResponse.createByErrorMessage("未查出教师信息列表");
        }
        return ServerResponse.createBySuccess("查询成功",list);
    }

    @ApiOperation(value = "根据ID删除讲师")
    @DeleteMapping("{id}")
    public ServerResponse removeById(
            @ApiParam(name = "id", value = "讲师ID", required = true)
            @PathVariable String id){
        boolean isDelete = teacherService.removeById(id);
        return isDelete?ServerResponse.createBySuccessMessage("删除成功"):ServerResponse.createByErrorMessage("删除失败");
    }

    @ApiOperation(value = "分页讲师列表")
    @GetMapping("{page}/{limit}")
    public Object pageList(
            @ApiParam(name = "page", value = "当前页码", required = true,defaultValue = "0")
            @PathVariable Integer page,

            @ApiParam(name = "limit", value = "每页记录数", required = true,defaultValue = "0")
            @PathVariable Integer limit,

            @ApiParam(name = "teacherQuery", value = "讲师查询对象")
                    TeacherQuery teacherQuery){
        if (page<0||limit<0){
//                 throw new GuliException(ResultCodeEnum.PARAM_ERROR);
            return ServerResponse.createByErrorCodeMessage(ResponseCode.PARAM_ERROR.getCode(),ResponseCode.PARAM_ERROR.getDesc());
        }

       /* Page<Teacher> pageParam = new Page<>(page, limit);
        teacherService.page(pageParam, null);
        List<Teacher> records = pageParam.getRecords();
        long total = pageParam.getTotal();
        return R.ok().data("total", total).data("rows", records);*/

        return teacherService.pageQuery(page, limit, teacherQuery);
    }

    @ApiOperation(value = "新增讲师")
    @PostMapping
    public ServerResponse save(
            @ApiParam(name = "teacher", value = "讲师对象", required = true)
            @RequestBody Teacher teacher){

        return teacherService.saveTeacher(teacher);
    }

    @ApiOperation(value = "根据ID查询讲师")
    @GetMapping("{id}")
    public ServerResponse getById(
            @ApiParam(name = "id", value = "讲师ID", required = true)
            @PathVariable String id){

        Teacher teacher = teacherService.getById(id);
        return ServerResponse.createBySuccess(teacher);
    }

    @ApiOperation(value = "根据ID修改讲师")
    @PutMapping("{id}")
    public ServerResponse updateById(
            @ApiParam(name = "id", value = "讲师ID", required = true)
            @PathVariable String id,

            @ApiParam(name = "teacher", value = "讲师对象", required = true)
            @RequestBody Teacher teacher){

        teacher.setId(id);
        teacherService.updateById(teacher);
        return ServerResponse.createBySuccessMessage("修改成功");
    }
}