package com.liaowei.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.liaowei.model.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Zhan Xinjian
 * @date 2020/2/25 21:19
 * <p></p>
 */
@Mapper
public interface UserMapper extends BaseMapper <User>{
}
