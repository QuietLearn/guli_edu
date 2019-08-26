package com.guli.edu.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * <p>
 * 课程科目
 * </p>
 *
 * @author heyijie
 * @since 2019-07-12
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@TableName("edu_subject")
@ApiModel(value="Subject对象", description="课程科目")
//java 、 大数据、人工智能、后端开发
public class Subject implements Serializable{

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "课程类别ID")
    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    @JsonIgnore
    @TableField(exist = false)
    private String parentTitle;

    @JsonIgnore
    @TableField(exist = false)
    private Subject parentSubject;

    @ApiModelProperty(value = "类别名称")
    private String title;

    @ApiModelProperty(value = "父ID")
    private String parentId;

    @ApiModelProperty(value = "排序字段")
    private Integer sort;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date gmtCreate;

    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date gmtModified;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subject subject = (Subject) o;
        if (subject.getParentId()!=null){
            return Objects.equals(parentId, subject.parentId) &&
                    Objects.equals(title, subject.title);
        } else {
            return Objects.equals(parentTitle, subject.parentTitle) &&
                    Objects.equals(title, subject.title);
        }

    }

    @Override
    public int hashCode() {
        if (this.parentId!=null){
            return Objects.hash(parentId, title);
        } else {
            return Objects.hash(parentTitle, title);
        }
    }
}
