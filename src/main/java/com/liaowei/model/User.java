package com.liaowei.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @author Zhan Xinjian
 * @date 2020/2/25 21:14
 * <p>
 *     User表
 * </p>
 */@Data
@TableName("tb_user")
public class User{
    /**
     * id,主键自增
     */
    @TableId(value = "id",type = IdType.INPUT)   //指定自增策略
    private Integer id;
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * 性别,0代表男性，1代表女性
     */
    private Integer sex;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 是否是管理员，0代表不是，1代表是
     */
    private Integer isManager;

    public User( String username, String password, Integer sex, String email) {
        this.username = username;
        this.password = password;
        this.sex = sex;
        this.email = email;
    }

    public User(String password) {
        this.password = password;
    }

    public User() {
    }

    public User(Integer id, String username, String password, Integer sex, String email, Date createTime, Integer isManager) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.sex = sex;
        this.email = email;
        this.createTime = createTime;
        this.isManager = isManager;
    }
}
