package com.guli.edu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Lists;
import com.guli.core.common.ServerResponse;
import com.guli.edu.entity.Chapter;
import com.guli.edu.entity.Video;
import com.guli.edu.mapper.ChapterMapper;
import com.guli.edu.mapper.VideoMapper;
import com.guli.edu.service.ChapterService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guli.edu.vo.response.ChapterVo;
import com.guli.edu.vo.response.VideoVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author heyijie
 * @since 2019-07-12
 */
@Service
public class ChapterServiceImpl extends ServiceImpl<ChapterMapper, Chapter> implements ChapterService {

    @Autowired
    private VideoMapper videoMapper;

    @Transactional
    @Override
    public ServerResponse removeChapterById(String id) {
//        todo 把阿里云vod的视频也删除掉
        //先把依赖该章节的视频全部删掉，不然外键依赖
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("chapter_id",id);
        int videoDeleteCount = videoMapper.delete(queryWrapper);
        if (videoDeleteCount<=0){
            return ServerResponse.createByErrorMessage("该章节相关联视频数据删除失败");
        }
        int deleteCount = baseMapper.deleteById(id);
        if (deleteCount<=0){
            return ServerResponse.createByErrorMessage("该章节删除失败");
        }

        return ServerResponse.createBySuccessMessage("删除成功");
    }

    @Override
    public ServerResponse getChildItemsByCourseId(String courseId) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("course_id",courseId);
        queryWrapper.orderByAsc("sort", "id");
        List<Chapter> chapterList = this.list(queryWrapper);
        if (CollectionUtils.isEmpty(chapterList)){
            return ServerResponse.createByErrorMessage("当前课程还没有任何章节，请及时添加");
        }

        List<Video> videoList = videoMapper.selectList(queryWrapper);

        List<ChapterVo> chapterVoList = Lists.newArrayList();
        for (Chapter chapter : chapterList) {
            ChapterVo chapterVo = new ChapterVo();
            BeanUtils.copyProperties(chapter,chapterVo);
            List<Video> chapterChildVideoList = Lists.newArrayList();
            for (Video video : videoList) {
                if (StringUtils.equals(video.getChapterId(),chapter.getId())) {
                    chapterChildVideoList.add(video);
                }
            }

            List<VideoVo> videoVoList = assemVideoVoList(chapterChildVideoList);

            chapterVo.setVideoVoList(videoVoList);

            chapterVoList.add(chapterVo);
        }

        return ServerResponse.createBySuccess("查询成功",chapterVoList);
    }

    public List<VideoVo> assemVideoVoList(List<Video> videoList){
        List<VideoVo> videoVoList = Lists.newArrayList();
        for (Video video : videoList) {
            VideoVo videoVo = new VideoVo();
            BeanUtils.copyProperties(video,videoVo);
            videoVoList.add(videoVo);
        }
        return videoVoList;
    }
}
