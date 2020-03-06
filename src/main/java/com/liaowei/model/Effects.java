package com.liaowei.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author Zhan Xinjian
 * @date 2020/3/6 0:03
 * <p></p>
 */
@TableName("TB_EFFECTS")
@Data
public class Effects {
    /**
     * id，主键自增
     */
    @TableId(value = "id",type = IdType.INPUT)   //指定自增策略
    private Integer id;
    /**
     * 特效，1,2,3三种特效
     */
    private Integer effect;
    /**
     * 照片表id
     */
    private Integer photoId;

    public Effects(Integer effect, Integer photoId) {
        this.effect = effect;
        this.photoId = photoId;
    }
}
