package com.guli.edu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.guli.core.common.ResponseCode;
import com.guli.core.common.ServerResponse;
import com.guli.core.util.ExcelImportUtil;
import com.guli.edu.entity.Subject;
import com.guli.edu.mapper.SubjectMapper;
import com.guli.edu.service.SubjectService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guli.edu.vo.SubjectNestedVo;
import org.apache.catalina.Server;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.text.MessageFormat;
import java.util.*;

/**
 * <p>
 * 课程科目 服务实现类
 * </p>
 *
 * @author heyijie
 * @since 2019-07-12
 */
@Service
public class SubjectServiceImpl extends ServiceImpl<SubjectMapper, Subject> implements SubjectService {

    @Transactional
    @Override
    public ServerResponse batchImport(MultipartFile file) throws Exception {
        long beginTime = System.currentTimeMillis();
        List<String> errorMsgList = Lists.newArrayList();

        Set<Subject> parentSubjectSet = Sets.newHashSet();
        Set<Subject> childSubjectSet = Sets.newHashSet();
        if (file == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.FILE_UPLOAD_ERROR);
        }
        //创建工具类对象
        ExcelImportUtil excelHSSFUtil = new ExcelImportUtil(file.getInputStream());

        HSSFSheet sheet = excelHSSFUtil.getSheet();
        int physicalNumberOfRows = sheet.getPhysicalNumberOfRows();
        if (physicalNumberOfRows <= 1) {
            return ServerResponse.createByErrorMessage("请填写数据");
        }

        Subject parentSubject = null;
        Subject childSubject = null;
        for (int rowNum = 1; rowNum < physicalNumberOfRows; rowNum++) {
            HSSFRow row = sheet.getRow(rowNum);
            // 行不为空
            if (row != null) {
                int physicalNumberOfCells = row.getPhysicalNumberOfCells();
                for (int cellNum = 0; cellNum < physicalNumberOfCells; cellNum++) {
                    HSSFCell cell = row.getCell(cellNum);

                    if (cell != null) {
                        String cellValue = excelHSSFUtil.getCellValue(cell).trim();
                        if (StringUtils.isBlank(cellValue)) {
                            errorMsgList.add(MessageFormat.format("第{}行，第{}列数据为空", rowNum, cellNum));
                            continue;
                        }
                        if (cellNum == 0) {
                            if (parentSubject == null) {
                                parentSubject = assemSubject("0", "", rowNum, cellValue);
                            } else if (!cellValue.equalsIgnoreCase(parentSubject.getTitle())) {
                                parentSubject = assemSubject("0", "", rowNum, cellValue);
                            }
                            Subject existParentSubject = this.getByTitle(cellValue);
                            if (existParentSubject == null) {
                                parentSubjectSet.add(parentSubject);
                            } else {
                                parentSubject.setId(existParentSubject.getId());
                            }


                        } else if (cellNum > 0) {
                            childSubject = assemSubject(parentSubject.getId(), parentSubject.getTitle(), rowNum, cellValue);
                            childSubject.setParentSubject(parentSubject);
                            childSubjectSet.add(childSubject);
                            childSubject.setParentTitle(null);
                        }
                    }
                }
            }
        }


        QueryWrapper<Subject> queryWrapper = new QueryWrapper<>();

        queryWrapper.ne("parent_id", "0");
        List<Subject> existChildSubjectList = baseMapper.selectList(queryWrapper);
        HashSet<Subject> existChildSubjectSet = Sets.newHashSet(existChildSubjectList);


        boolean b = this.saveBatch(parentSubjectSet);

        for (Subject subject : childSubjectSet) {
            subject.setParentId(subject.getParentSubject().getId());
        }

        Set<Subject> result = new HashSet<>();
        result.addAll(childSubjectSet);
        result.retainAll(existChildSubjectSet);
        /*Collection<List<Subject>> lists = Collections2.orderedPermutations(result, (o1, o2) -> {
            return o1.getSort() - o2.getSort();
            //Comparator.comparing
        });*/
        ArrayList<Subject> subjects = Lists.newArrayList(result);
        Collections.sort(subjects,(o1, o2) -> {
            return o1.getSort() - o2.getSort();
        });


        for (Subject subject : subjects) {
            errorMsgList.add("第" + subject.getSort() + "行第2列数据重复");
        }


        childSubjectSet.removeAll(existChildSubjectSet);

        boolean b1 = this.saveBatch(childSubjectSet);
        if (!CollectionUtils.isEmpty(errorMsgList)) {
            return ServerResponse.createByErrorDataCodeMessage(ResponseCode.EXCEL_DATA_IMPORT_ERROR, errorMsgList);
        }

        long endTime = System.currentTimeMillis();
        System.out.println("方法耗费性能" + (endTime - beginTime) + "ms");
        return ServerResponse.createBySuccessMessage("插入subject信息成功");
    }

    public Subject assemSubject(String parentId, String parentTitle, Integer sort, String title) {
        Subject subject = new Subject();
        subject.setParentId(parentId);
        subject.setParentTitle(parentTitle);
        subject.setSort(sort);
        subject.setTitle(title);
        return subject;
    }


    @Override
    public ServerResponse nestedList() {
        //获取一级分类数据记录
        QueryWrapper<Subject>  parentQueryWrapper = new QueryWrapper<>();
        parentQueryWrapper.eq("parent_id","0");
        parentQueryWrapper.orderByAsc("sort", "id");
        List<Subject> parentSubjectList = baseMapper.selectList(parentQueryWrapper);
        if (CollectionUtils.isEmpty(parentSubjectList)){
            return ServerResponse.createBySuccessMessage("现在还没有课程，赶快添加吧");
        }

        List<SubjectNestedVo> subjectNestedVoList = Lists.newArrayList();


        //获取二级分类数据记录
        QueryWrapper<Subject> childQueryWrapper = new QueryWrapper<>();
        childQueryWrapper.ne("parent_id", 0);
        childQueryWrapper.orderByAsc("sort", "id");
        List<Subject> childSubjectList = baseMapper.selectList(childQueryWrapper);

        for (Subject parentSubject : parentSubjectList) {
            SubjectNestedVo parentSubjectNestedVo = new SubjectNestedVo();
            BeanUtils.copyProperties(parentSubject,parentSubjectNestedVo);

            for (Subject childSubject : childSubjectList) {
                if (StringUtils.equals(childSubject.getParentId(),parentSubject.getId()) ){
                    SubjectNestedVo childSubjectNestedVo = new SubjectNestedVo();
                    BeanUtils.copyProperties(childSubject,childSubjectNestedVo);
                    parentSubjectNestedVo.getChildren().add(childSubjectNestedVo);
                }
            }
            subjectNestedVoList.add(parentSubjectNestedVo);
        }

        return ServerResponse.createBySuccess("封装课程分类成功",subjectNestedVoList);

    }

    @Override
    public ServerResponse nestedList2() {
        List<SubjectNestedVo> subjectNestedVoList = baseMapper.selectNestedListByParentId("0");
        if (CollectionUtils.isEmpty(subjectNestedVoList)){
            return ServerResponse.createByErrorMessage("subject分类数据为空");
        }
        return ServerResponse.createBySuccess("查詢课程分类信息成功",subjectNestedVoList);
    }
    /**
     * 根据分类名称查询这个一级分类中否存在
     *
     * @param title
     * @return
     */
    private Subject getByTitle(String title) {

        QueryWrapper<Subject> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("title", title);
        queryWrapper.eq("parent_id", "0");//一级分类
        return baseMapper.selectOne(queryWrapper);
    }

    /**
     * 根据分类名称和父id查询这个二级分类中否存在
     *
     * @param title
     * @return
     */
    private Subject getSubByTitle(String title, String parentId) {

        QueryWrapper<Subject> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("title", title);
        queryWrapper.eq("parent_id", parentId);
        return baseMapper.selectOne(queryWrapper);
    }


    @Transactional
    @Override
    public List<String> batchImport2(MultipartFile file) throws Exception {
        long beginTime = System.currentTimeMillis();

        //错误消息列表
        List<String> errorMsg = new ArrayList<>();

        //创建工具类对象
        ExcelImportUtil excelHSSFUtil = new ExcelImportUtil(file.getInputStream());
        //获取工作表
        Sheet sheet = excelHSSFUtil.getSheet();

        int rowCount = sheet.getPhysicalNumberOfRows();
        if (rowCount <= 1) {
            errorMsg.add("请填写数据");
            return errorMsg;
        }

        for (int rowNum = 1; rowNum < rowCount; rowNum++) {

            Row rowData = sheet.getRow(rowNum);
            if (rowData != null) {// 行不为空

                //获取一级分类
                String levelOneValue = "";
                Cell levelOneCell = rowData.getCell(0);
                if (levelOneCell != null) {
                    levelOneValue = excelHSSFUtil.getCellValue(levelOneCell).trim();
                    if (StringUtils.isEmpty(levelOneValue)) {
                        errorMsg.add("第" + rowNum + "行一级分类为空");
                        continue;
                    }
                }

                //判断一级分类是否重复
                Subject subject = this.getByTitle(levelOneValue);
                String parentId = null;
                if (subject == null) {
                    //将一级分类存入数据库
                    Subject subjectLevelOne = new Subject();
                    subjectLevelOne.setTitle(levelOneValue);
                    subjectLevelOne.setSort(rowNum);
                    baseMapper.insert(subjectLevelOne);//添加
                    parentId = subjectLevelOne.getId();
                } else {
                    parentId = subject.getId();
                }
                //获取二级分类
                String levelTwoValue = "";
                Cell levelTwoCell = rowData.getCell(1);
                if (levelTwoCell != null) {
                    levelTwoValue = excelHSSFUtil.getCellValue(levelTwoCell).trim();
                    if (StringUtils.isEmpty(levelTwoValue)) {
                        errorMsg.add("第" + rowNum + "行二级分类为空");
                        continue;
                    }
                }
                //判断二级分类是否重复
                Subject subjectSub = this.getSubByTitle(levelTwoValue, parentId);
                Subject subjectLevelTwo = null;
                if (subjectSub == null) {
                    //将二级分类存入数据库
                    subjectLevelTwo = new Subject();
                    subjectLevelTwo.setTitle(levelTwoValue);
                    subjectLevelTwo.setParentId(parentId);
                    subjectLevelTwo.setSort(rowNum);
                    baseMapper.insert(subjectLevelTwo);//添加
                }
            }
        }

        long endTime = System.currentTimeMillis();
        System.out.println("方法耗费性能" + (endTime - beginTime) + "ms");
        return errorMsg;
    }
}
