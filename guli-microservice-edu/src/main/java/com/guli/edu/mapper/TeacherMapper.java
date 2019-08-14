package com.guli.edu.mapper;

import com.guli.edu.entity.Teacher;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * <p>
 * 讲师 Mapper 接口
 * </p>
 *
 * @author heyijie
 * @since 2019-07-12
 */
public interface TeacherMapper extends BaseMapper<Teacher> {
/*Class clazz = Teacher.class;
//获取带泛型的父类
Type superclass = clazz.getGenericSuperclass();
//如果父类带泛型了，可以强转为带参数的类型
ParameterizedType pramType = (ParameterizedType) superclass;
//获取到了父类的泛型参数
Type[] typeArguments = pramType.getActualTypeArguments();
//获取唯一的一个泛型参数
System.out.println(((Class)typeArguments[0]).getName());*/


}
