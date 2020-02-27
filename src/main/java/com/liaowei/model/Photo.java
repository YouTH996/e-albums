package com.liaowei.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
*  照片表
* @author zxj 2020-02-27
*/
@SuppressWarnings("ALL")
@TableName("TB_PHOTO")
@Data
public class Photo {

    /**
    * id，主键自增
    */
    @TableId(value = "id",type = IdType.INPUT)   //指定自增策略
    private Integer id;
    /**
    * 照片名
    */
    private String photoName;
    /**
    * 照片类型，0-3，分别代表人物，风景，建筑，美食
    */
    private Integer photoType;
    /**
    * 创建时间
    */
    private Date createTime;
    /**
    * 照片描述
    */
    private String photoDescri;
}