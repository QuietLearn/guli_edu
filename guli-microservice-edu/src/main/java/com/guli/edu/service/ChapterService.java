package com.guli.edu.service;

import com.guli.core.common.ServerResponse;
import com.guli.edu.entity.Chapter;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author heyijie
 * @since 2019-07-12
 */
public interface ChapterService extends IService<Chapter> {

    ServerResponse removeChapterById(String id);

    ServerResponse getChildItemsByCourseId(String courseId);
}
