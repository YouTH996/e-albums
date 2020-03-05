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
public class Photo{

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
     * 图片链接
     */
    private String photoUrl;
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
    /**
     * 照片BGM
     */
    private String photoBgm;

    public Photo(String photoName, String photoUrl, Integer photoType, String photoDescri) {
        this.photoName = photoName;
        this.photoUrl = photoUrl;
        this.photoType = photoType;
        this.photoDescri = photoDescri;
    }

    public Photo() {
    }

    public Photo(String photoName, String photoUrl, Integer photoType, String photoDescri, String photoBgm) {
        this.photoName = photoName;
        this.photoUrl = photoUrl;
        this.photoType = photoType;
        this.photoDescri = photoDescri;
        this.photoBgm = photoBgm;
    }

    public Photo(Integer id, String photoName, String photoUrl, Integer photoType, Date createTime, String photoDescri, String photoBgm) {
        this.id = id;
        this.photoName = photoName;
        this.photoUrl = photoUrl;
        this.photoType = photoType;
        this.createTime = createTime;
        this.photoDescri = photoDescri;
        this.photoBgm = photoBgm;
    }
}