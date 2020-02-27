package com.liaowei.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liaowei.mapper.UserMapper;
import com.liaowei.model.User;
import com.liaowei.service.UserService;
import org.springframework.stereotype.Service;

/**
 * @author Zhan Xinjian
 * @date 2020/2/25 21:21
 * <p></p>
 */
@Service
public class UserServiceImpl extends ServiceImpl <UserMapper,User> implements UserService {
}
