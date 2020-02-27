package com.liaowei.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.liaowei.model.UserPhoto;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户,照片关联表
 * @author zxj
 * @data 2020-02-27 02:33:11
 */
@Mapper
public interface UserPhotoMapper extends BaseMapper<UserPhoto> {



}
