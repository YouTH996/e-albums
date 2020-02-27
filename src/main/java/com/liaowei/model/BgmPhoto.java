package com.liaowei.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
*  照片,背景音乐关联表
* @author zxj 2020-02-27
*/
@SuppressWarnings("ALL")
@TableName("TB_BGM_PHOTO")
@KeySequence(value = "S_TB_BGM_PHOTO", clazz = Integer.class)
@Data
public class BgmPhoto {

    /**
    * id，主键自增
    */
    @TableId(value = "id",type = IdType.INPUT)   //指定自增策略
    private Integer id;
    /**
    * 背景音乐id
    */
    private Integer bgmId;
    /**
    * 照片id
    */
    private Integer photoId;
}