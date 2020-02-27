package com.liaowei.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
*  背景音乐表
* @author zxj 2020-02-27
*/
@SuppressWarnings("ALL")
@TableName("TB_BGM")

@Data
public class Bgm  {

    /**
    * id，主键自增
    */
    @TableId(value = "id",type = IdType.INPUT)   //指定自增策略
    private Integer id;
    /**
    * 背景音乐名
    */
    private String bgmName;
    /**
    * bgm链接
    */
    private String bgmUrl;
    /**
    * 创建时间
    */
    private Date createTime;
}