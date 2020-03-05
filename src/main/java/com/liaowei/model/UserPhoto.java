package com.liaowei.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
*  用户,照片关联表
* @author zxj 2020-02-27
*/
@SuppressWarnings("ALL")
@TableName("TB_USER_PHOTO")

@Data
public class UserPhoto {

    /**
    * id，主键自增
    */
    @TableId(value = "id",type = IdType.INPUT)   //指定自增策略
    private Integer id;
    /**
    * user表id
    */
    private Integer userId;
    /**
    * 照片表id
    */
    private Integer photoId;

    public UserPhoto(Integer userId, Integer photoId) {
        this.userId = userId;
        this.photoId = photoId;
    }
}