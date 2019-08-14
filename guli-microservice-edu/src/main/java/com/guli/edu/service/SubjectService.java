package com.guli.edu.service;

import com.guli.core.common.ServerResponse;
import com.guli.edu.entity.Subject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.guli.edu.vo.SubjectNestedVo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 课程科目 服务类
 * </p>
 *
 * @author heyijie
 * @since 2019-07-12
 */
public interface SubjectService extends IService<Subject> {

    ServerResponse batchImport(MultipartFile file) throws Exception;

    List<String> batchImport2(MultipartFile file) throws Exception ;

    /**
     * 获取并封装课程分类列表
     * @return
     */
    ServerResponse nestedList();

    ServerResponse nestedList2();
}
