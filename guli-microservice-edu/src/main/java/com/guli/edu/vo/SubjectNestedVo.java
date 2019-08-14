package com.guli.edu.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@JsonIgnoreProperties(value = { "handler" })
public class SubjectNestedVo implements Serializable {

    private String id;
    private String title;
    private List<SubjectNestedVo> children = new ArrayList<>();
}