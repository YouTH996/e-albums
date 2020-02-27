package com.liaowei.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liaowei.mapper.PhotoMapper;
import com.liaowei.model.Photo;
import com.liaowei.service.PhotoService;
import org.springframework.stereotype.Service;

/**
 * 照片表
 * @author zxj
 * @data 2020-02-27 02:32:58
 */
@Service
public class PhotoServiceImpl extends ServiceImpl<PhotoMapper, Photo> implements PhotoService {


}
