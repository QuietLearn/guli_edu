package com.guli.edu.controller.admin;

import com.guli.core.common.R;
import com.guli.core.common.ResponseCode;
import com.guli.core.common.ServerResponse;
import com.guli.core.exception.GuliException;
import com.guli.edu.service.SubjectService;
import com.guli.edu.vo.SubjectNestedVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Api(tags = "课程后台管理模块")
@RestController
@RequestMapping("/admin/edu/subject")
@CrossOrigin //跨域
public class SubjectAdminController {

    @Autowired
    private SubjectService subjectService;

    @ApiOperation(value = "Excel批量导入,添加课程种类")
    @PostMapping("category/import")
    public ServerResponse batchImport(@RequestParam("file") MultipartFile file) throws Exception {
        return subjectService.batchImport(file);
    }

    @ApiOperation(value = "Excel批量导入")
    @PostMapping("category/add2")
    public R batchImport2(
            @ApiParam(name = "file", value = "Excel文件", required = true)
            @RequestParam("file") MultipartFile file) throws Exception {
        try{
            List<String> errorMsg = subjectService.batchImport2(file);
            if(errorMsg.size() == 0){
                return R.ok().message("批量导入成功");
            }else{
                return R.error().message("部分数据导入失败").data("errorMsgList", errorMsg);
            }

        }catch (Exception e){
            //无论哪种异常，只要是在excel导入时发生的，一律用转成GuliException抛出
            throw new GuliException(ResponseCode.EXCEL_DATA_IMPORT_ERROR);
        }
    }

    @ApiOperation(value = "课程分类(嵌套数据)列表")
    @GetMapping("list")
    public ServerResponse nestedList(){
//List<SubjectNestedVo> subjectNestedVoList =
        return subjectService.nestedList();
    }

    @ApiOperation(value = "嵌套数据列表")
    @GetMapping("list2")
    public ServerResponse nestedList2(){

        return subjectService.nestedList2();
    }
}