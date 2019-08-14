package com.guli.edu.mapper;

import com.guli.edu.entity.Subject;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guli.edu.vo.SubjectNestedVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 课程科目 Mapper 接口
 * </p>
 *
 * @author heyijie
 * @since 2019-07-12
 */
public interface SubjectMapper extends BaseMapper<Subject> {

    int batchInsert(@Param("subjectSet") Set<Subject> subjectSet);

    List<SubjectNestedVo> selectNestedListByParentId(String parentId);
}
